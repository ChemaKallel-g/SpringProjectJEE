package com.institut.ProjetSpringAC.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.institut.ProjetSpringAC.service.*;
import com.institut.ProjetSpringAC.entity.*;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;
    private final TrainerService trainerService;
    private final CourseService courseService;
    private final InscriptionService inscriptionService;
    private final SpecialtyService specialtyService;
    private final SessionService sessionService;
    private final StudentGroupService groupService;

    public AdminController(StudentService studentService,
            TrainerService trainerService,
            CourseService courseService,
            InscriptionService inscriptionService,
            SpecialtyService specialtyService,
            SessionService sessionService,
            StudentGroupService groupService) {
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.courseService = courseService;
        this.inscriptionService = inscriptionService;
        this.specialtyService = specialtyService;
        this.sessionService = sessionService;
        this.groupService = groupService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Counts
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("trainerCount", trainerService.getAllTrainers().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("inscriptionCount", inscriptionService.getAllInscriptions().size());
        model.addAttribute("pendingInscriptionCount", inscriptionService.countPendingInscriptions());
        model.addAttribute("specialtyCount", specialtyService.getAllSpecialties().size());
        model.addAttribute("sessionCount", sessionService.getAllSessions().size());
        model.addAttribute("groupCount", groupService.getAllGroups().size());

        // Chart Data: Students per Course & Avg Grade
        List<Course> courses = courseService.getAllCourses();
        List<String> courseNames = new ArrayList<>();
        List<Integer> studentsPerCourse = new ArrayList<>();
        List<Double> avgGrades = new ArrayList<>();

        for (Course course : courses) {
            courseNames.add(course.getTitle());
            studentsPerCourse.add(course.getInscriptions().size());

            // Calculate Avg Grade
            if (!course.getGrades().isEmpty()) {
                double sum = 0;
                for (Grade g : course.getGrades()) {
                    sum += g.getValue();
                }
                avgGrades.add(sum / course.getGrades().size());
            } else {
                avgGrades.add(0.0);
            }
        }

        model.addAttribute("courseNames", courseNames);
        model.addAttribute("studentsPerCourse", studentsPerCourse);
        model.addAttribute("avgGrades", avgGrades);

        // Chart Data: Courses per Specialty
        Map<String, Integer> coursesPerSpecialtyMap = new HashMap<>();
        for (Course course : courses) {
            String specialtyName = "Non défini";
            if (course.getTrainer() != null && course.getTrainer().getSpecialty() != null) {
                specialtyName = course.getTrainer().getSpecialty();
            }
            coursesPerSpecialtyMap.put(specialtyName, coursesPerSpecialtyMap.getOrDefault(specialtyName, 0) + 1);
        }

        model.addAttribute("specialtyLabels", new ArrayList<>(coursesPerSpecialtyMap.keySet()));
        model.addAttribute("coursesPerSpecialtyData", new ArrayList<>(coursesPerSpecialtyMap.values()));

        return "admin/dashboard";
    }
}
