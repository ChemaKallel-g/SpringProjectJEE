package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final StudentService studentService;
    private final com.institut.ProjetSpringAC.service.StudentGroupService groupService;

    @Autowired
    public AdminStudentController(StudentService studentService,
            com.institut.ProjetSpringAC.service.StudentGroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "admin/students/list";
    }

    @GetMapping("/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin/students/form";
    }

    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("groups", groupService.getAllGroups());
            return "admin/students/form";
        } else {
            return "redirect:/admin/students";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Étudiant supprimé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/students";
    }
}
