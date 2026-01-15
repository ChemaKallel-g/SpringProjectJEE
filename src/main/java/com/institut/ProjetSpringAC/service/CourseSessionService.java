package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.entity.Trainer;
import com.institut.ProjetSpringAC.repository.CourseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CourseSessionService {

    private final CourseSessionRepository courseSessionRepository;

    @Autowired
    public CourseSessionService(CourseSessionRepository courseSessionRepository) {
        this.courseSessionRepository = courseSessionRepository;
    }

    public List<CourseSession> getAllCourseSessions() {
        return courseSessionRepository.findAll();
    }

    public Optional<CourseSession> getCourseSessionById(Long id) {
        return courseSessionRepository.findById(id);
    }

    public CourseSession saveCourseSession(CourseSession session) throws Exception {
        // Validation Logic
        validateSession(session);
        return courseSessionRepository.save(session);
    }

    private void validateSession(CourseSession session) throws Exception {
        // 1. Check Room Conflict
        List<CourseSession> roomConflicts = courseSessionRepository.findConflictingSessionsByRoom(
                session.getRoom(), session.getStartTime(), session.getEndTime());
        for (CourseSession conflict : roomConflicts) {
            if (!conflict.getId().equals(session.getId())) {
                throw new Exception("La salle " + session.getRoom() + " est déjà occupée.");
            }
        }

        // 2. Check Trainer Conflict
        Trainer trainer = session.getCourse().getTrainer();
        if (trainer != null) {
            List<CourseSession> trainerConflicts = courseSessionRepository.findConflictingSessionsByTrainer(
                    trainer, session.getStartTime(), session.getEndTime());
            for (CourseSession conflict : trainerConflicts) {
                if (!conflict.getId().equals(session.getId())) {
                    throw new Exception("Le formateur " + trainer.getName() + " a déjà un cours à cette heure.");
                }
            }
        }

        // 3. Check Group Conflict
        List<StudentGroup> newGroups = session.getCourse().getGroups();
        if (newGroups != null && !newGroups.isEmpty()) {
            List<CourseSession> timeOverlaps = courseSessionRepository.findByStartTimeLessThanAndEndTimeGreaterThan(
                    session.getEndTime(), session.getStartTime());

            for (CourseSession existing : timeOverlaps) {
                if (existing.getId().equals(session.getId()))
                    continue;

                List<StudentGroup> existingGroups = existing.getCourse().getGroups();
                if (!Collections.disjoint(newGroups, existingGroups)) {
                    throw new Exception(
                            "Un ou plusieurs groupes (" + session.getCourse().getTitle() + ") ont déjà cours.");
                }
            }
        }
    }

    public void deleteCourseSession(Long id) {
        courseSessionRepository.deleteById(id);
    }
}
