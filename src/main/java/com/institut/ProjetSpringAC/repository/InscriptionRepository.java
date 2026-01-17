package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    @EntityGraph(attributePaths = { "student", "course" })
    List<Inscription> findAll();

    @EntityGraph(attributePaths = { "student", "course" })
    @Query("SELECT i FROM Inscription i WHERE i.id = :id")
    Optional<Inscription> findByIdWithAssociations(@Param("id") Long id);

    List<Inscription> findByStudentId(Long studentId);

    long countByStatus(com.institut.ProjetSpringAC.entity.InscriptionStatus status);
}
