package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import com.institut.ProjetSpringAC.entity.Room;

@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {

        // Check Room Conflict
        @Query("SELECT cs FROM CourseSession cs WHERE cs.room = :room AND " +
                        "((cs.startTime < :endTime AND cs.endTime > :startTime))")
        List<CourseSession> findConflictingSessionsByRoom(@Param("room") Room room,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        // Check Trainer Conflict (via Course)
        @Query("SELECT cs FROM CourseSession cs WHERE cs.course.trainer = :trainer AND " +
                        "((cs.startTime < :endTime AND cs.endTime > :startTime))")
        List<CourseSession> findConflictingSessionsByTrainer(@Param("trainer") Trainer trainer,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        // Check Student Conflict (via ID, not Group directly here easiest to check
        // Groups in Service)
        // Actually, we can check if any course of the session has overlapping groups.
        // Query: Select sessions where session.course.groups intersect with
        // newSession.course.groups
        // This is hard in JPQL.

        // Alternative: Find all sessions overlapping time, then filter in Java.
        List<CourseSession> findByStartTimeLessThanAndEndTimeGreaterThan(LocalDateTime endTime,
                        LocalDateTime startTime);

        List<CourseSession> findByCourseIn(List<com.institut.ProjetSpringAC.entity.Course> courses);

        List<CourseSession> findByGroupId(Long groupId);

        List<CourseSession> findByCourseTrainerId(Long trainerId);
}
