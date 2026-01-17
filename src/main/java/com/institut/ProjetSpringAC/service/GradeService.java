package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.entity.Grade;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public Grade getGradeByStudentAndCourse(Student student, Course course) {
        return gradeRepository.findByStudentAndCourse(student, course);
    }

    public Grade saveGrade(Grade grade) {
        if (grade.getDate() == null) {
            grade.setDate(java.time.LocalDate.now());
        }
        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }
}
