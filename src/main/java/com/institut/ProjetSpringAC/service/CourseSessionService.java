package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.entity.Trainer;
import com.institut.ProjetSpringAC.repository.CourseSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseSessionService {

    private final CourseSessionRepository courseSessionRepository;

    @Autowired
    public CourseSessionService(CourseSessionRepository courseSessionRepository) {
        this.courseSessionRepository = courseSessionRepository;
    }

    public List<CourseSession> getSessionsByGroupId(Long groupId) {
        return courseSessionRepository.findByGroupId(groupId);
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
        // 0. Check Time Logic
        if (session.getStartTime() == null || session.getEndTime() == null) {
            throw new Exception("Les horaires de début et de fin sont obligatoires.");
        }
        if (!session.getStartTime().isBefore(session.getEndTime())) {
            throw new Exception("L'heure de début doit être antérieure à l'heure de fin.");
        }

        // 1. Check Room Conflict
        if (session.getRoom() != null) {
            List<CourseSession> roomConflicts = courseSessionRepository.findConflictingSessionsByRoom(
                    session.getRoom(), session.getStartTime(), session.getEndTime());
            for (CourseSession conflict : roomConflicts) {
                // If it's a different session, it's a conflict
                if (session.getId() == null || !conflict.getId().equals(session.getId())) {
                    throw new Exception(
                            "La salle " + session.getRoom().getName() + " est déjà occupée sur ce créneau.");
                }
            }

            // 1b. Check Room Capacity
            StudentGroup group = session.getGroup();
            // Ensure group and students are loaded
            if (group != null && group.getStudents() != null && session.getRoom().getCapacity() != null) {
                if (group.getStudents().size() > session.getRoom().getCapacity()) {
                    throw new Exception("Capacité de la salle (" + session.getRoom().getCapacity()
                            + ") insuffisante pour le groupe " + group.getName()
                            + " (" + group.getStudents().size() + " étudiants).");
                }
            }
        }

        // 2. Check Trainer Conflict
        Trainer trainer = null;
        if (session.getCourse() != null) {
            trainer = session.getCourse().getTrainer();
        }

        if (trainer != null) {
            List<CourseSession> trainerConflicts = courseSessionRepository.findConflictingSessionsByTrainer(
                    trainer, session.getStartTime(), session.getEndTime());
            for (CourseSession conflict : trainerConflicts) {
                // If it's a different session, it's a conflict
                if (session.getId() == null || !conflict.getId().equals(session.getId())) {
                    throw new Exception("Le formateur " + trainer.getName() + " a déjà un cours sur ce créneau.");
                }
            }
        }

        // 3. Check Group Conflict
        StudentGroup group = session.getGroup();
        if (group != null) {
            List<CourseSession> groupSessions = courseSessionRepository.findByGroupId(group.getId());
            for (CourseSession existing : groupSessions) {
                if (session.getId() != null && existing.getId().equals(session.getId()))
                    continue;

                if (existing.getStartTime().isBefore(session.getEndTime()) &&
                        existing.getEndTime().isAfter(session.getStartTime())) {
                    throw new Exception("Le groupe " + group.getName() + " a déjà un cours à cette heure ("
                            + existing.getCourse().getTitle() + ").");
                }
            }
        }
    }

    public void deleteCourseSession(Long id) {
        courseSessionRepository.deleteById(id);
    }

    public List<CourseSession> getSessionsByTrainerId(Long trainerId) {
        return courseSessionRepository.findByCourseTrainerId(trainerId);
    }
}
