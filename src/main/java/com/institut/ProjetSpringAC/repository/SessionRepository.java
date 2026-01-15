package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}
