package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.Session;
import com.institut.ProjetSpringAC.entity.SessionStatus;
import com.institut.ProjetSpringAC.entity.SessionType;
import com.institut.ProjetSpringAC.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = sessionRepository.findByType(SessionType.ACADEMIC_YEAR);
        sessions.forEach(this::updateStatusRecursive);
        return sessions;
    }

    private void updateStatusRecursive(Session session) {
        session.updateStatus();
        if (session.getChildren() != null) {
            session.getChildren().forEach(this::updateStatusRecursive);
        }
    }

    public List<Session> getVisibleSessions() {
        // Students and trainers can only see ACTIVE and ARCHIVED sessions
        List<Session> sessions = sessionRepository
                .findByStatusIn(Arrays.asList(SessionStatus.ACTIVE, SessionStatus.ARCHIVED));
        sessions.forEach(Session::updateStatus);
        return sessions;
    }

    public List<Session> getAcademicYears() {
        return sessionRepository.findByType(SessionType.ACADEMIC_YEAR);
    }

    public List<Session> getAllSemesters() {
        return sessionRepository.findByType(SessionType.SEMESTER);
    }

    public Optional<Session> getSessionById(Long id) {
        Optional<Session> session = sessionRepository.findById(id);
        session.ifPresent(Session::updateStatus);
        return session;
    }

    public Session saveSession(Session session) {
        session.updateStatus();
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}
