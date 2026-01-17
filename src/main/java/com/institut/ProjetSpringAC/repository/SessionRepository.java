package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.Session;
import com.institut.ProjetSpringAC.entity.SessionStatus;
import com.institut.ProjetSpringAC.entity.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByStatusIn(List<SessionStatus> statuses);

    List<Session> findByType(SessionType type);
}
