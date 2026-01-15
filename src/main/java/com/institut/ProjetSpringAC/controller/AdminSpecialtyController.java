package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.Specialty;
import com.institut.ProjetSpringAC.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/specialties")
public class AdminSpecialtyController {

    private final SpecialtyService specialtyService;

    @Autowired
    public AdminSpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public String listSpecialties(Model model) {
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        return "admin/specialties/list";
    }

    @GetMapping("/new")
    public String createSpecialtyForm(Model model) {
        model.addAttribute("specialty", new Specialty());
        return "admin/specialties/form";
    }

    @PostMapping
    public String saveSpecialty(@ModelAttribute("specialty") Specialty specialty,
            RedirectAttributes redirectAttributes) {
        try {
            specialtyService.saveSpecialty(specialty);
            redirectAttributes.addFlashAttribute("successMessage", "Spécialité enregistrée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/admin/specialties";
    }

    @GetMapping("/edit/{id}")
    public String editSpecialtyForm(@PathVariable Long id, Model model) {
        Optional<Specialty> specialty = specialtyService.getSpecialtyById(id);
        if (specialty.isPresent()) {
            model.addAttribute("specialty", specialty.get());
            return "admin/specialties/form";
        } else {
            return "redirect:/admin/specialties";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSpecialty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            specialtyService.deleteSpecialty(id);
            redirectAttributes.addFlashAttribute("successMessage", "Spécialité supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible de supprimer cette spécialité car elle est utilisée par des formateurs ou des groupes.");
        }
        return "redirect:/admin/specialties";
    }
}
