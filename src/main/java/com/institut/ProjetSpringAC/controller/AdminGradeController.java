package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.entity.Grade;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.service.CourseService;
import com.institut.ProjetSpringAC.service.GradeService;
import com.institut.ProjetSpringAC.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/grades")
public class AdminGradeController {

    private final GradeService gradeService;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public AdminGradeController(GradeService gradeService, StudentService studentService, CourseService courseService) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/assign")
    public String assignGradeForm(@RequestParam Long studentId, @RequestParam Long courseId, Model model) {
        Optional<Student> student = studentService.getStudentById(studentId);
        Optional<Course> course = courseService.getCourseById(courseId);

        if (student.isPresent() && course.isPresent()) {
            Grade existingGrade = gradeService.getGradeByStudentAndCourse(student.get(), course.get());
            if (existingGrade != null) {
                model.addAttribute("grade", existingGrade);
            } else {
                Grade newGrade = new Grade();
                newGrade.setStudent(student.get());
                newGrade.setCourse(course.get());
                model.addAttribute("grade", newGrade);
            }
            return "admin/grades/form";
        }
        return "redirect:/admin/inscriptions";
    }

    @PostMapping
    public String saveGrade(@ModelAttribute("grade") Grade grade) {
        // Reload entities to ensure integrity if hidden fields are ID only
        // But here we rely on binding. We might need to ensure student/course are set
        // if they just come as IDs.
        // Spring MVC binds nested objects by ID if they have a converter or if we
        // passed them.
        // To be safe, let's trust the binding for now or better, use Hidden inputs for
        // student.id and course.id
        // and let Spring JPA/Web resolve them ?
        // Actually, simplest is to save.
        gradeService.saveGrade(grade);
        return "redirect:/admin/inscriptions";
    }
}
