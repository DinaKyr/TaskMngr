package com.example.TaskMngr.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.dto.OnUpdate;
//import com.example.TaskMngr.dto.OnCreate;
//import com.example.TaskMngr.models.Role;
//import com.example.TaskMngr.repositories.RepositoryUser;
import com.example.TaskMngr.services.UserService;
import org.springframework.ui.Model;
//import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    //@Autowired
   // private RepositoryUser repositoryUser;

    

    @GetMapping("/edit")
    public String editMyAccount(Model model, Principal principal) {
        String currentUsername = principal.getName();
        DtoUser dtoUser = userService.findDtoByUsername(currentUsername);
        model.addAttribute("dtoUser", dtoUser);
        model.addAttribute("canEditRole", false);
        return "edit-own-account"; // new Thymeleaf page
    }

    @PutMapping("/user/edit")
    public String updateMyAccount(@ModelAttribute("dtoUser") @Validated(OnUpdate.class) DtoUser dtoUser,
                                BindingResult bindingResult,
                                Principal principal,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-own-account";
        }

        try {
            userService.updateOwnAccount(principal.getName(), dtoUser);
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("dtoUser", dtoUser);
            model.addAttribute("canEditRole", false);
            model.addAttribute("errorMessage", ex.getMessage()); // e.g. "Passwords do not match"
            return "edit-own-account";
        }
    }

    @DeleteMapping("/user/delete")
    public String deleteOwnAccount(Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            Long userId = userService.findByUsername(username).getId();
            userService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("success", "Account deleted.");
            return "redirect:/logout"; // or homepage
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete account.");
            return "redirect:/user/edit";
        }
    }

    
}
