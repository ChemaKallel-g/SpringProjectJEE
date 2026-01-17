package com.institut.ProjetSpringAC.controller.api;

import com.institut.ProjetSpringAC.entity.StudentGroup;
import com.institut.ProjetSpringAC.service.StudentGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class ApiGroupController {

    private final StudentGroupService groupService;

    public ApiGroupController(StudentGroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<StudentGroup> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public StudentGroup getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id).orElse(null);
    }
}
