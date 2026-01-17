package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Role;
import com.institut.ProjetSpringAC.entity.User;
import com.institut.ProjetSpringAC.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
