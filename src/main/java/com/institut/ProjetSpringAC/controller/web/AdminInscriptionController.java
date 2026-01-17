package com.institut.ProjetSpringAC.controller.web;

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
        Inscription inscription = new Inscription();
        inscription.setStatus(com.institut.ProjetSpringAC.entity.InscriptionStatus.PENDING);
        model.addAttribute("inscription", inscription);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("statuses", com.institut.ProjetSpringAC.entity.InscriptionStatus.values());
        return "admin/inscriptions/form";
    }

    @PostMapping
    public String saveInscription(@ModelAttribute("inscription") Inscription inscription,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        if (inscription.getId() != null) {
            // Cas de la MODIFICATION
            inscriptionService.getInscriptionById(inscription.getId()).ifPresent(existing -> {
                existing.setStatus(inscription.getStatus());
                existing.setDate(inscription.getDate());
                inscriptionService.saveInscription(existing);
            });
        } else {
            // Cas de la CRÉATION
            if (inscription.getDate() == null)
                inscription.setDate(LocalDate.now());

            // On récupère manuellement l'étudiant et le cours car le binding auto d'entité
            // peut faillir
            if (studentId != null)
                studentService.getStudentById(studentId).ifPresent(inscription::setStudent);
            if (courseId != null)
                courseService.getCourseById(courseId).ifPresent(inscription::setCourse);

            if (inscription.getStatus() == null) {
                inscription.setStatus(com.institut.ProjetSpringAC.entity.InscriptionStatus.PENDING);
            }
            inscriptionService.saveInscription(inscription);
        }
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/edit/{id}")
    public String editInscriptionForm(@PathVariable Long id, Model model) {
        return inscriptionService.getInscriptionById(id).map(inscription -> {
            model.addAttribute("inscription", inscription);
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("statuses", com.institut.ProjetSpringAC.entity.InscriptionStatus.values());
            return "admin/inscriptions/form";
        }).orElse("redirect:/admin/inscriptions");
    }

    @GetMapping("/delete/{id}")
    public String deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/accept/{id}")
    public String acceptInscription(@PathVariable Long id) {
        inscriptionService.getInscriptionById(id).ifPresent(inscription -> {
            inscription.setStatus(com.institut.ProjetSpringAC.entity.InscriptionStatus.ACCEPTED);
            inscriptionService.saveInscription(inscription);
        });
        return "redirect:/admin/inscriptions";
    }

    @GetMapping("/reject/{id}")
    public String rejectInscription(@PathVariable Long id) {
        inscriptionService.getInscriptionById(id).ifPresent(inscription -> {
            inscription.setStatus(com.institut.ProjetSpringAC.entity.InscriptionStatus.REJECTED);
            inscriptionService.saveInscription(inscription);
        });
        return "redirect:/admin/inscriptions";
    }
}
