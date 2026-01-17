package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.entity.SessionStatus;
import com.institut.ProjetSpringAC.repository.CourseSessionRepository;
import com.institut.ProjetSpringAC.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TimetableController {

    private final StudentRepository studentRepository;
    private final CourseSessionRepository courseSessionRepository;

    @Autowired
    public TimetableController(StudentRepository studentRepository, CourseSessionRepository courseSessionRepository) {
        this.studentRepository = studentRepository;
        this.courseSessionRepository = courseSessionRepository;
    }

    @GetMapping("/timetable")
    public String viewTimetable(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student student = studentRepository.findByEmail(username).orElse(null);

        if (student == null) {
            model.addAttribute("error", "Étudiant non trouvé pour l'utilisateur connecté.");
            return "timetable";
        }

        StudentGroup group = student.getGroup();
        if (group == null) {
            model.addAttribute("message", "Vous n'êtes assigné à aucun groupe.");
            return "timetable";
        }

        List<Course> courses = group.getCourses();
        if (courses.isEmpty()) {
            model.addAttribute("message", "Aucun cours pour votre groupe.");
            return "timetable";
        }

        List<CourseSession> sessions = courseSessionRepository.findByGroupId(group.getId());

        List<Map<String, Object>> sessionData = sessions.stream()
                .filter(cs -> {
                    com.institut.ProjetSpringAC.entity.Session academicSession = group.getSession();
                    if (academicSession == null)
                        return true;
                    return academicSession.getStatus() != SessionStatus.PLANNED;
                })
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", s.getId());
                    map.put("course", Map.of("title", s.getCourse().getTitle(),
                            "trainer",
                            s.getCourse().getTrainer() != null ? s.getCourse().getTrainer().getName() : "N/A"));
                    map.put("startTime", s.getStartTime().toString());
                    map.put("endTime", s.getEndTime().toString());
                    map.put("room", s.getRoom() != null ? Map.of("name", s.getRoom().getName()) : null);
                    return map;
                })
                .collect(Collectors.toList());

        model.addAttribute("sessions", sessionData);
        model.addAttribute("student", student);

        return "timetable";
    }
}
