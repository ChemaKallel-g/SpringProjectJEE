package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Role;
import com.institut.ProjetSpringAC.entity.User;
import com.institut.ProjetSpringAC.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users/list";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @RequestParam("username") String username,
            @RequestParam("role") Role role) {
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only update the role, keep username and password unchanged
        existingUser.setRole(role);
        userService.updateUser(existingUser);

        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
