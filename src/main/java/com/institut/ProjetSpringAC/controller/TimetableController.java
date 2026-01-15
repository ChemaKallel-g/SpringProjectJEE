package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.entity.Student;
import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.repository.CourseSessionRepository;
import com.institut.ProjetSpringAC.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

        // Find student by username (which is email? or we need to look up
        // CustomUserDetails)
        // Assuming username corresponds to Student's email or identifier used in
        // UserDetailService.
        // My CustomUserDetailsService loads by username which matches Users table.
        // I need to link Users to Student.
        // Currently, 'Student' entity has 'email'. 'Users' entity has 'username'.
        // Assuming they are same. or I need to find Student by Email = username.

        // Wait, 'Student' entity doesn't have a direct link to 'Users' table explicitly
        // in the entity model shown before?
        // Let's check Student.java.
        // If not, I'll search by email assuming username == email.

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

        List<CourseSession> sessions = courseSessionRepository.findByCourseIn(courses);
        model.addAttribute("sessions", sessions);
        model.addAttribute("student", student);

        return "timetable";
    }
}
