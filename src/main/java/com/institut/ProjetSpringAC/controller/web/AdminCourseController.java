package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Course;
import com.institut.ProjetSpringAC.service.CourseService;
import com.institut.ProjetSpringAC.service.TrainerService;
import com.institut.ProjetSpringAC.service.StudentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    private final CourseService courseService;
    private final TrainerService trainerService;
    private final StudentGroupService groupService;

    @Autowired
    public AdminCourseController(CourseService courseService, TrainerService trainerService,
            StudentGroupService groupService) {
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
    public String saveCourse(@ModelAttribute("course") Course course,
            @RequestParam(value = "trainerId", required = false) Long trainerId,
            @RequestParam(value = "groupIds", required = false) java.util.List<Long> groupIds) {

        if (course.getId() != null) {
            // Modification
            courseService.getCourseById(course.getId()).ifPresent(existing -> {
                existing.setCode(course.getCode());
                existing.setTitle(course.getTitle());
                existing.setDescription(course.getDescription());

                // Trainer
                if (trainerId != null) {
                    trainerService.getTrainerById(trainerId).ifPresent(existing::setTrainer);
                } else {
                    existing.setTrainer(null);
                }

                // Groups (ManyToMany)
                java.util.List<com.institut.ProjetSpringAC.entity.StudentGroup> selectedGroups = new java.util.ArrayList<>();
                if (groupIds != null) {
                    groupIds.forEach(id -> groupService.getGroupById(id).ifPresent(selectedGroups::add));
                }
                existing.setGroups(selectedGroups);

                courseService.saveCourse(existing);
            });
        } else {
            // Création
            if (trainerId != null) {
                trainerService.getTrainerById(trainerId).ifPresent(course::setTrainer);
            }

            java.util.List<com.institut.ProjetSpringAC.entity.StudentGroup> selectedGroups = new java.util.ArrayList<>();
            if (groupIds != null) {
                groupIds.forEach(id -> groupService.getGroupById(id).ifPresent(selectedGroups::add));
            }
            course.setGroups(selectedGroups);

            courseService.saveCourse(course);
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("trainers", trainerService.getAllTrainers());
            model.addAttribute("groups", groupService.getAllGroups());
            // Pour faciliter la sélection multiple dans le template
            java.util.List<Long> currentGroupIds = course.get().getGroups().stream()
                    .map(com.institut.ProjetSpringAC.entity.StudentGroup::getId)
                    .collect(java.util.stream.Collectors.toList());
            model.addAttribute("currentGroupIds", currentGroupIds);
            return "admin/courses/form";
        } else {
            return "redirect:/admin/courses";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
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
