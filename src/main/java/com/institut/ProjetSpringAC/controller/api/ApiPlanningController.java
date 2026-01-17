package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.dto.CourseSessionDto;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.entity.Trainer;
import com.institut.ProjetSpringAC.repository.StudentRepository;
import com.institut.ProjetSpringAC.repository.TrainerRepository;
import com.institut.ProjetSpringAC.service.CourseSessionService;
import com.institut.ProjetSpringAC.service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;

@RestController
@RequestMapping("/api/planning")
public class ApiPlanningController {

        private final CourseSessionService sessionService;
        private final StudentRepository studentRepository;
        private final TrainerRepository trainerRepository;
        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
        private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE", Locale.FRENCH);

        public ApiPlanningController(CourseSessionService sessionService,
                        StudentRepository studentRepository,
                        TrainerRepository trainerRepository) {
                this.sessionService = sessionService;
                this.studentRepository = studentRepository;
                this.trainerRepository = trainerRepository;
        }

        @GetMapping("/etudiant/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
        public List<CourseSessionDto> getPlanningByStudent(@PathVariable Long id) {
                return studentRepository.findByUserId(id)
                                .map(Student::getGroup)
                                .map(group -> sessionService.getSessionsByGroupId(group.getId()).stream()
                                                .map(this::convertToDto)
                                                .collect(Collectors.toList()))
                                .orElse(List.of());
        }

        @GetMapping("/formateur/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'FORMATEUR')")
        public List<CourseSessionDto> getPlanningByTrainer(@PathVariable Long id) {
                Long trainerEntityId = trainerRepository.findByUserId(id)
                                .map(Trainer::getId)
                                .orElse(-1L);

                return sessionService.getSessionsByTrainerId(trainerEntityId).stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
        }

        @GetMapping("/groupe/{id}")
        @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'FORMATEUR')")
        public List<CourseSessionDto> getPlanningByGroup(@PathVariable Long id) {
                return sessionService.getSessionsByGroupId(id).stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList());
        }

        private CourseSessionDto convertToDto(com.institut.ProjetSpringAC.entity.CourseSession s) {
                return new CourseSessionDto(
                                s.getId(),
                                s.getCourse() != null ? s.getCourse().getTitle() : "Sans titre",
                                s.getStartTime() != null ? s.getStartTime().format(DAY_FORMATTER) : "-",
                                s.getStartTime() != null ? s.getStartTime().format(TIME_FORMATTER) : "-",
                                s.getEndTime() != null ? s.getEndTime().format(TIME_FORMATTER) : "-",
                                s.getRoom() != null ? s.getRoom().getName() : "N/A");
        }
}
