package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.service.CourseSessionService;
import com.institut.ProjetSpringAC.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/planning")
public class AdminPlanningController {

    private final CourseSessionService sessionService;
    private final CourseService courseService;

    @Autowired
    public AdminPlanningController(CourseSessionService sessionService, CourseService courseService) {
        this.sessionService = sessionService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.getAllCourseSessions());
        return "admin/planning/list";
    }

    @GetMapping("/new")
    public String createSessionForm(Model model) {
        model.addAttribute("session", new CourseSession());
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/planning/form";
    }

    @PostMapping
    public String saveSession(@ModelAttribute("session") CourseSession session, RedirectAttributes redirectAttributes) {
        try {
            sessionService.saveCourseSession(session);
            redirectAttributes.addFlashAttribute("successMessage", "Séance planifiée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur de planification : " + e.getMessage());
            // It would be better to return to the form with errors, but redirect works for
            // simple error feedback
        }
        return "redirect:/admin/planning";
    }

    @GetMapping("/edit/{id}")
    public String editSessionForm(@PathVariable Long id, Model model) {
        Optional<CourseSession> session = sessionService.getCourseSessionById(id);
        if (session.isPresent()) {
            model.addAttribute("session", session.get());
            model.addAttribute("courses", courseService.getAllCourses());
            return "admin/planning/form";
        } else {
            return "redirect:/admin/planning";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sessionService.deleteCourseSession(id);
            redirectAttributes.addFlashAttribute("successMessage", "Séance supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/planning";
    }
}
