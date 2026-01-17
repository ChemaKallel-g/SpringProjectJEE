package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.entity.Specialty;
import com.institut.ProjetSpringAC.service.SpecialtyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class ApiSpecialtyController {

    private final SpecialtyService specialtyService;

    public ApiSpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<Specialty> getAllSpecialties() {
        return specialtyService.getAllSpecialties();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public Specialty getSpecialtyById(@PathVariable Long id) {
        return specialtyService.getSpecialtyById(id).orElse(null);
    }
}
