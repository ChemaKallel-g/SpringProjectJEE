package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.service.StudentGroupService;
import com.institut.ProjetSpringAC.service.SpecialtyService;
import com.institut.ProjetSpringAC.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/groups")
public class AdminGroupController {

    private final StudentGroupService groupService;
    private final SpecialtyService specialtyService;
    private final SessionService sessionService;

    @Autowired
    public AdminGroupController(StudentGroupService groupService, SpecialtyService specialtyService,
            SessionService sessionService) {
        this.groupService = groupService;
        this.specialtyService = specialtyService;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String listGroups(Model model) {
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin/groups/list";
    }

    @GetMapping("/new")
    public String createGroupForm(Model model) {
        model.addAttribute("group", new StudentGroup());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        model.addAttribute("sessions", sessionService.getAllSessions());
        return "admin/groups/form";
    }

    @PostMapping
    public String saveGroup(@ModelAttribute("group") StudentGroup group, RedirectAttributes redirectAttributes) {
        try {
            groupService.saveGroup(group);
            redirectAttributes.addFlashAttribute("successMessage", "Groupe enregistré avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/admin/groups";
    }

    @GetMapping("/edit/{id}")
    public String editGroupForm(@PathVariable Long id, Model model) {
        Optional<StudentGroup> group = groupService.getGroupById(id);
        if (group.isPresent()) {
            model.addAttribute("group", group.get());
            model.addAttribute("specialties", specialtyService.getAllSpecialties());
            model.addAttribute("sessions", sessionService.getAllSessions());
            return "admin/groups/form";
        } else {
            return "redirect:/admin/groups";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            groupService.deleteGroup(id);
            redirectAttributes.addFlashAttribute("successMessage", "Groupe supprimé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible de supprimer ce groupe car il contient des étudiants ou est lié à des cours.");
        }
        return "redirect:/admin/groups";
    }
}
