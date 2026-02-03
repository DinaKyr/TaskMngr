package com.example.TaskMngr.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.TaskMngr.services.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.dto.OnCreate;
import com.example.TaskMngr.models.Invite;
import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.repositories.RepositoryInvite;


@Controller
public class AuthController {

    private final UserService userService;
    private final RepositoryInvite inviteRepository;

    public AuthController(UserService userService, RepositoryInvite inviteRepository) {
        this.userService = userService;
        this.inviteRepository = inviteRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(name = "token", required = false) String token, Model model) {
        DtoUser dtoUser = new DtoUser();

        if (token != null) {
            // Lookup the invite
            Invite invite = inviteRepository.findByToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

            // Check if token is valid
            if (invite.isUsed() || invite.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Token expired or already used");
            }

            // Pre-fill email
            dtoUser.setEmail(invite.getEmail());
            model.addAttribute("inviteToken", token); // store token for POST
        }

        model.addAttribute("user",dtoUser);
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
