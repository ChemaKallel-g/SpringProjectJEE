package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.entity.Inscription;
import com.institut.ProjetSpringAC.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionRestController {

    private final InscriptionService inscriptionService;

    @Autowired
    public InscriptionRestController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @GetMapping
    public List<Inscription> getAllInscriptions() {
        return inscriptionService.getAllInscriptions();
    }

    @PostMapping
    public Inscription createInscription(@RequestBody Inscription inscription) {
        return inscriptionService.saveInscription(inscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
}
