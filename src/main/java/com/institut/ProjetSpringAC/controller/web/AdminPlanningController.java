package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.CourseSession;
import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.service.CourseSessionService;
import com.institut.ProjetSpringAC.service.StudentGroupService;
import com.institut.ProjetSpringAC.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/groups/{groupId}/planning")
public class AdminPlanningController {

    private final CourseSessionService sessionService;
    private final StudentGroupService groupService;
    private final RoomService roomService;

    @Autowired
    public AdminPlanningController(CourseSessionService sessionService,
            StudentGroupService groupService,
            RoomService roomService) {
        this.sessionService = sessionService;
        this.groupService = groupService;
        this.roomService = roomService;
    }

    @GetMapping
    public String listSessions(@PathVariable Long groupId, Model model) {
        Optional<StudentGroup> group = groupService.getGroupById(groupId);
        if (group.isPresent()) {
            model.addAttribute("group", group.get());

            List<CourseSession> sessions = sessionService.getSessionsByGroupId(groupId);

            // Map to simple structure for JS inlining
            List<Map<String, Object>> sessionData = sessions.stream().map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", s.getId());
                map.put("course", Map.of("title", s.getCourse().getTitle(),
                        "trainer", s.getCourse().getTrainer() != null ? s.getCourse().getTrainer().getName() : "N/A"));
                map.put("startTime", s.getStartTime().toString());
                map.put("endTime", s.getEndTime().toString());
                map.put("room", s.getRoom() != null ? Map.of("name", s.getRoom().getName()) : null);
                return map;
            }).collect(Collectors.toList());

            model.addAttribute("sessions", sessionData);
            return "admin/planning/list";
        }
        return "redirect:/admin/groups";
    }

    @GetMapping("/new")
    public String createSessionForm(@PathVariable Long groupId, Model model) {
        Optional<StudentGroup> groupOpt = groupService.getGroupById(groupId);
        if (groupOpt.isPresent()) {
            StudentGroup group = groupOpt.get();
            CourseSession session = new CourseSession();
            session.setGroup(group);
            model.addAttribute("session", session);
            model.addAttribute("group", group);
            model.addAttribute("courses", group.getCourses());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "admin/planning/form";
        }
        return "redirect:/admin/groups";
    }

    @PostMapping
    public String saveSession(@PathVariable Long groupId, @ModelAttribute("session") CourseSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<StudentGroup> group = groupService.getGroupById(groupId);
            group.ifPresent(session::setGroup);
            sessionService.saveCourseSession(session);
            redirectAttributes.addFlashAttribute("successMessage", "Séance planifiée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur de planification : " + e.getMessage());
        }
        return "redirect:/admin/groups/" + groupId + "/planning";
    }

    @GetMapping("/edit/{id}")
    public String editSessionForm(@PathVariable Long groupId, @PathVariable Long id, Model model) {
        Optional<CourseSession> session = sessionService.getCourseSessionById(id);
        Optional<StudentGroup> groupOpt = groupService.getGroupById(groupId);

        if (session.isPresent() && groupOpt.isPresent()) {
            StudentGroup group = groupOpt.get();
            model.addAttribute("session", session.get());
            model.addAttribute("group", group);
            model.addAttribute("courses", group.getCourses());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "admin/planning/form";
        }
        return "redirect:/admin/groups/" + groupId + "/planning";
    }

    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long groupId, @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            sessionService.deleteCourseSession(id);
            redirectAttributes.addFlashAttribute("successMessage", "Séance supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/groups/" + groupId + "/planning";
    }
}
