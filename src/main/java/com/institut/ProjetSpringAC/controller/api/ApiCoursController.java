package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.dto.CourseDto;
import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class ApiCoursController {

    private final CourseService courseService;

    public ApiCoursController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TRAINER')")
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TRAINER')")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(convertToDto(course)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CourseDto createCourse(@RequestBody Course course) {
        return convertToDto(courseService.saveCourse(course));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseService.getCourseById(id)
                .map(existingCourse -> {
                    existingCourse.setTitle(courseDetails.getTitle());
                    existingCourse.setCode(courseDetails.getCode());
                    existingCourse.setDescription(courseDetails.getDescription());
                    existingCourse.setTrainer(courseDetails.getTrainer());
                    return ResponseEntity.ok(convertToDto(courseService.saveCourse(existingCourse)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    private CourseDto convertToDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getDescription(),
                course.getTrainer() != null ? course.getTrainer().getId() : null,
                course.getTrainer() != null ? course.getTrainer().getName() : "Non assigné");
    }
}
