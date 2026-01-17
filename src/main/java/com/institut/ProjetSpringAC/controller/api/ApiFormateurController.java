package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.dto.TrainerDto;
import com.institut.ProjetSpringAC.entity.Trainer;
import com.institut.ProjetSpringAC.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
public class ApiFormateurController {

    private final TrainerService trainerService;

    public ApiFormateurController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public List<TrainerDto> getAllTrainers() {
        return trainerService.getAllTrainers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public ResponseEntity<TrainerDto> getTrainerById(@PathVariable Long id) {
        return trainerService.getTrainerById(id)
                .map(trainer -> ResponseEntity.ok(convertToDto(trainer)))
                .orElse(ResponseEntity.notFound().build());
    }

    private TrainerDto convertToDto(Trainer trainer) {
        return new TrainerDto(
                trainer.getId(),
                trainer.getName(),
                trainer.getEmail(),
                trainer.getSpecialty());
    }
}
