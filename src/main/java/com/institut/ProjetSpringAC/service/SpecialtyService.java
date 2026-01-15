package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.Specialty;
import com.institut.ProjetSpringAC.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Autowired
    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    public Optional<Specialty> getSpecialtyById(Long id) {
        return specialtyRepository.findById(id);
    }

    public Specialty saveSpecialty(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    public void deleteSpecialty(Long id) {
        specialtyRepository.deleteById(id);
    }
}
