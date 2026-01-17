package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Session;
import com.institut.ProjetSpringAC.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/sessions")
public class AdminSessionController {

    private final SessionService sessionService;

    @Autowired
    public AdminSessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "admin/sessions/list";
    }

    @GetMapping("/new")
    public String createSessionForm(Model model) {
        model.addAttribute("academicSession", new Session());
        model.addAttribute("academicYears", sessionService.getAcademicYears());
        return "admin/sessions/form";
    }

    @PostMapping
    public String saveSession(@ModelAttribute("academicSession") Session session,
            RedirectAttributes redirectAttributes) {
        try {
            sessionService.saveSession(session);
            redirectAttributes.addFlashAttribute("successMessage", "Session enregistrée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/admin/sessions";
    }

    @GetMapping("/edit/{id}")
    public String editSessionForm(@PathVariable Long id, Model model) {
        Optional<Session> session = sessionService.getSessionById(id);
        if (session.isPresent()) {
            model.addAttribute("academicSession", session.get());
            model.addAttribute("academicYears", sessionService.getAcademicYears());
            return "admin/sessions/form";
        } else {
            return "redirect:/admin/sessions";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sessionService.deleteSession(id);
            redirectAttributes.addFlashAttribute("successMessage", "Session supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/sessions";
    }
}
