package com.institut.ProjetSpringAC.repository;

import com.institut.ProjetSpringAC.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Grade findByStudentAndCourse(com.institut.ProjetSpringAC.entity.Student student,
            com.institut.ProjetSpringAC.entity.Course course);

    java.util.List<Grade> findByStudentId(Long studentId);

    java.util.List<Grade> findByCourseId(Long courseId);
}
