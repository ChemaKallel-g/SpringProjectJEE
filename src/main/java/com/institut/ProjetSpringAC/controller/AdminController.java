package com.institut.ProjetSpringAC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final com.institut.ProjetSpringAC.service.StudentService studentService;
    private final com.institut.ProjetSpringAC.service.TrainerService trainerService;
    private final com.institut.ProjetSpringAC.service.CourseService courseService;
    private final com.institut.ProjetSpringAC.service.InscriptionService inscriptionService;
    private final com.institut.ProjetSpringAC.service.SpecialtyService specialtyService;
    private final com.institut.ProjetSpringAC.service.SessionService sessionService;
    private final com.institut.ProjetSpringAC.service.StudentGroupService groupService;

    public AdminController(com.institut.ProjetSpringAC.service.StudentService studentService,
            com.institut.ProjetSpringAC.service.TrainerService trainerService,
            com.institut.ProjetSpringAC.service.CourseService courseService,
            com.institut.ProjetSpringAC.service.InscriptionService inscriptionService,
            com.institut.ProjetSpringAC.service.SpecialtyService specialtyService,
            com.institut.ProjetSpringAC.service.SessionService sessionService,
            com.institut.ProjetSpringAC.service.StudentGroupService groupService) {
        this.studentService = studentService;
        this.trainerService = trainerService;
        this.courseService = courseService;
        this.inscriptionService = inscriptionService;
        this.specialtyService = specialtyService;
        this.sessionService = sessionService;
        this.groupService = groupService;
    }

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.ui.Model model) {
        // Counts
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("trainerCount", trainerService.getAllTrainers().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("inscriptionCount", inscriptionService.getAllInscriptions().size());
        model.addAttribute("specialtyCount", specialtyService.getAllSpecialties().size());
        model.addAttribute("sessionCount", sessionService.getAllSessions().size());
        model.addAttribute("groupCount", groupService.getAllGroups().size());

        // Chart Data: Students per Course & Avg Grade
        java.util.List<com.institut.ProjetSpringAC.entity.Course> courses = courseService.getAllCourses();
        java.util.List<String> courseNames = new java.util.ArrayList<>();
        java.util.List<Integer> studentsPerCourse = new java.util.ArrayList<>();
        java.util.List<Double> avgGrades = new java.util.ArrayList<>();

        for (com.institut.ProjetSpringAC.entity.Course course : courses) {
            courseNames.add(course.getTitle());
            studentsPerCourse.add(course.getInscriptions().size());

            // Calculate Avg Grade
            if (!course.getGrades().isEmpty()) {
                double sum = 0;
                for (com.institut.ProjetSpringAC.entity.Grade g : course.getGrades()) {
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
        java.util.Map<String, Integer> coursesPerSpecialtyMap = new java.util.HashMap<>();
        for (com.institut.ProjetSpringAC.entity.Course course : courses) {
            String specialtyName = "Non défini";
            if (course.getTrainer() != null && course.getTrainer().getSpecialty() != null) {
                specialtyName = course.getTrainer().getSpecialty();
            }
            coursesPerSpecialtyMap.put(specialtyName, coursesPerSpecialtyMap.getOrDefault(specialtyName, 0) + 1);
        }

        model.addAttribute("specialtyLabels", new java.util.ArrayList<>(coursesPerSpecialtyMap.keySet()));
        model.addAttribute("coursesPerSpecialtyData", new java.util.ArrayList<>(coursesPerSpecialtyMap.values()));

        return "admin/dashboard";
    }
}
