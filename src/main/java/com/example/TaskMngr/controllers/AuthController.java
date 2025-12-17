package com.example.TaskMngr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.TaskMngr.services.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.dto.OnCreate;
import com.example.TaskMngr.models.Role;


@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user",new DtoUser());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Validated(OnCreate.class) @ModelAttribute("user") DtoUser dtoUser,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", dtoUser);
            return "register"; // show form again with errors
        }
        
        try {
            dtoUser.setRole(Role.BASIC); //lways BASIC on register
            userService.createUser(dtoUser);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", e.getMessage());
            return "register";
        
        }
        return "redirect:/login";
}
}
