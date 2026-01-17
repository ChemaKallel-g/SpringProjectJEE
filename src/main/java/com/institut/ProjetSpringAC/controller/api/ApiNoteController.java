package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.dto.GradeDto;
import com.institut.ProjetSpringAC.entity.Grade;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.repository.StudentRepository;
import com.institut.ProjetSpringAC.service.GradeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
public class ApiNoteController {

    private final GradeService gradeService;
    private final StudentRepository studentRepository;

    public ApiNoteController(GradeService gradeService, StudentRepository studentRepository) {
        this.gradeService = gradeService;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('FORMATEUR')")
    public GradeDto saveGrade(@RequestBody Grade grade) {
        return convertToDto(gradeService.saveGrade(grade));
    }

    @GetMapping("/etudiant/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public List<GradeDto> getGradesByStudent(@PathVariable Long id) {
        // 'id' est le userId
        Long studentEntityId = studentRepository.findByUserId(id)
                .map(Student::getId)
                .orElse(-1L);

        return gradeService.getGradesByStudentId(studentEntityId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/cours/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
    public List<GradeDto> getGradesByCourse(@PathVariable Long id) {
        return gradeService.getGradesByCourseId(id).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GradeDto convertToDto(Grade grade) {
        String periodStr = "-";
        if (grade.getSession() != null) {
            String semester = grade.getSession().getName();
            String academicYear = (grade.getSession().getParent() != null) ? grade.getSession().getParent().getName()
                    : "";
            periodStr = semester + " de l'année académique " + academicYear;
        } else if (grade.getDate() != null) {
            periodStr = grade.getDate().toString();
        }

        return new GradeDto(
                grade.getId(),
                grade.getValue(),
                grade.getStudent().getId(),
                grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(),
                grade.getCourse().getId(),
                grade.getCourse().getTitle(),
                periodStr,
                grade.getComment());
    }
}
