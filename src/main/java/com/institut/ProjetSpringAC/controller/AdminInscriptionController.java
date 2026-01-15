package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.Inscription;
import com.institut.ProjetSpringAC.service.CourseService;
import com.institut.ProjetSpringAC.service.InscriptionService;
import com.institut.ProjetSpringAC.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/inscriptions")
public class AdminInscriptionController {

    private final InscriptionService inscriptionService;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public AdminInscriptionController(InscriptionService inscriptionService, StudentService studentService,
            CourseService courseService) {
        this.inscriptionService = inscriptionService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listInscriptions(Model model) {
        model.addAttribute("inscriptions", inscriptionService.getAllInscriptions());
        return "admin/inscriptions/list";
    }

    @GetMapping("/new")
    public String createInscriptionForm(Model model) {
        model.addAttribute("inscription", new Inscription());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/inscriptions/form";
    }

    @PostMapping
    public String saveInscription(@ModelAttribute("inscription") Inscription inscription) {
        if (inscription.getDate() == null) {
            inscription.setDate(LocalDate.now());
        }
        inscriptionService.saveInscription(inscription);
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/delete/{id}")
    public String deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return "redirect:/admin/inscriptions";
    }
}
