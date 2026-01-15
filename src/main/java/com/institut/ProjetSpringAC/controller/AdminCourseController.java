package com.institut.ProjetSpringAC.controller;

import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.service.CourseService;
import com.institut.ProjetSpringAC.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    private final CourseService courseService;
    private final TrainerService trainerService;
    private final com.institut.ProjetSpringAC.service.StudentGroupService groupService;

    @Autowired
    public AdminCourseController(CourseService courseService, TrainerService trainerService,
            com.institut.ProjetSpringAC.service.StudentGroupService groupService) {
        this.courseService = courseService;
        this.trainerService = trainerService;
        this.groupService = groupService;
    }

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses/list";
    }

    @GetMapping("/new")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin/courses/form";
    }

    @PostMapping
    public String saveCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("trainers", trainerService.getAllTrainers());
            model.addAttribute("groups", groupService.getAllGroups());
            return "admin/courses/form";
        } else {
            return "redirect:/admin/courses";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cours supprimé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}
