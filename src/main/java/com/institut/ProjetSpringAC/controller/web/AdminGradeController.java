package com.institut.ProjetSpringAC.controller.web;

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
    private final com.institut.ProjetSpringAC.service.SessionService sessionService;

    @Autowired
    public AdminGradeController(GradeService gradeService, StudentService studentService, CourseService courseService,
            com.institut.ProjetSpringAC.service.SessionService sessionService) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.sessionService = sessionService;
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
            model.addAttribute("semesters", sessionService.getAllSemesters());
            return "admin/grades/form";
        }
        return "redirect:/admin/inscriptions";
    }

    @PostMapping
    public String saveGrade(@ModelAttribute("grade") Grade grade, @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId,
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        Student student = studentService.getStudentById(studentId).orElseThrow();
        Course course = courseService.getCourseById(courseId).orElseThrow();
        grade.setStudent(student);
        grade.setCourse(course);

        if (sessionId != null) {
            sessionService.getSessionById(sessionId).ifPresent(grade::setSession);
        }

        gradeService.saveGrade(grade);
        return "redirect:/admin/inscriptions";
    }
}
