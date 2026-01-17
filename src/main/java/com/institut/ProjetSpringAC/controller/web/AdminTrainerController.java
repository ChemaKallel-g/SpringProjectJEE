package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Trainer;
import com.institut.ProjetSpringAC.service.TrainerService;
import com.institut.ProjetSpringAC.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/trainers")
public class AdminTrainerController {

    private final TrainerService trainerService;
    private final SpecialtyService specialtyService;

    @Autowired
    public AdminTrainerController(TrainerService trainerService,
            SpecialtyService specialtyService) {
        this.trainerService = trainerService;
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "admin/trainers/list";
    }

    @GetMapping("/new")
    public String createTrainerForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        model.addAttribute("specialties", specialtyService.getAllSpecialties());
        return "admin/trainers/form";
    }

    @PostMapping
    public String saveTrainer(@ModelAttribute("trainer") Trainer trainer,
            @RequestParam(value = "specialtyId", required = false) Long specialtyId) {
        if (trainer.getId() != null) {
            // Modification
            trainerService.getTrainerById(trainer.getId()).ifPresent(existing -> {
                existing.setName(trainer.getName());
                existing.setEmail(trainer.getEmail());

                if (specialtyId != null) {
                    specialtyService.getSpecialtyById(specialtyId).ifPresent(s -> {
                        existing.setSpecialtyEntity(s);
                        existing.setSpecialty(s.getName());
                    });
                } else {
                    existing.setSpecialtyEntity(null);
                    existing.setSpecialty(trainer.getSpecialty());
                }

                // On garde l'ancien 'user' et les 'courses'
                trainerService.saveTrainer(existing);
            });
        } else {
            // Création
            if (specialtyId != null) {
                specialtyService.getSpecialtyById(specialtyId).ifPresent(s -> {
                    trainer.setSpecialtyEntity(s);
                    trainer.setSpecialty(s.getName());
                });
            }
            trainerService.saveTrainer(trainer);
        }
        return "redirect:/admin/trainers";
    }

    @GetMapping("/edit/{id}")
    public String editTrainerForm(@PathVariable Long id, Model model) {
        Optional<Trainer> trainer = trainerService.getTrainerById(id);
        if (trainer.isPresent()) {
            model.addAttribute("trainer", trainer.get());
            model.addAttribute("specialties", specialtyService.getAllSpecialties());
            return "admin/trainers/form";
        } else {
            return "redirect:/admin/trainers";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            trainerService.deleteTrainer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Formateur supprimé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/trainers";
    }
}
