package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.dto.InscriptionDto;
import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.entity.Inscription;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.entity.User;
import com.institut.ProjetSpringAC.repository.CourseRepository;
import com.institut.ProjetSpringAC.repository.StudentRepository;
import com.institut.ProjetSpringAC.repository.UserRepository;
import com.institut.ProjetSpringAC.service.InscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inscriptions")
public class ApiInscriptionController {

    private final InscriptionService inscriptionService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ApiInscriptionController(InscriptionService inscriptionService,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            UserRepository userRepository) {
        this.inscriptionService = inscriptionService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public InscriptionDto createInscription(@RequestBody InscriptionDto dto) {
        Long userId = dto.getStudentId();
        Long courseId = dto.getCourseId();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

        // Trouver ou créer l'étudiant lié à l'utilisateur
        Student student = studentRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Student newStudent = new Student();
                    newStudent.setUser(user);
                    newStudent.setFirstName(user.getUsername());
                    newStudent.setLastName("");
                    newStudent.setEmail(user.getUsername() + "@example.com");
                    newStudent.setRegistrationNumber("REG-" + System.currentTimeMillis());
                    newStudent.setRegistrationDate(LocalDate.now());
                    return studentRepository.save(newStudent);
                });

        Inscription inscription = new Inscription();
        inscription.setStudent(student);
        inscription.setCourse(course);
        inscription.setDate(LocalDate.now());
        inscription.setStatus(com.institut.ProjetSpringAC.entity.InscriptionStatus.PENDING);

        return convertToDto(inscriptionService.saveInscription(inscription));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/etudiant/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public List<InscriptionDto> getInscriptionsByStudent(@PathVariable Long id) {
        // 'id' est le userId (ID de l'utilisateur connecté)
        Long studentEntityId = studentRepository.findByUserId(id)
                .map(Student::getId)
                .orElse(-1L); // Si pas d'étudiant trouvé, renvoyer une liste vide

        return inscriptionService.getInscriptionsByStudentId(studentEntityId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InscriptionDto convertToDto(Inscription inscription) {
        return new InscriptionDto(
                inscription.getId(),
                inscription.getDate(),
                inscription.getStudent().getId(),
                inscription.getStudent().getFirstName() + " " + inscription.getStudent().getLastName(),
                inscription.getCourse().getId(),
                inscription.getCourse().getTitle(),
                inscription.getStatus().name());
    }
}
