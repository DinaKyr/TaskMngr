package com.example.TaskMngr.controllers;


import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.TaskMngr.dto.DtoProject;
import com.example.TaskMngr.dto.DtoTask;
import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.dto.OnCreate;
import com.example.TaskMngr.dto.OnUpdate;
import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.models.User;
import com.example.TaskMngr.repositories.RepositoryUser;
import com.example.TaskMngr.services.UserService;
import com.example.TaskMngr.services.ProjectService;
import com.example.TaskMngr.services.TaskService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")  // applies to all methods in this class
public class AdminController {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final RepositoryUser repositoryUser;

    public AdminController(UserService userService , ProjectService projectService, TaskService taskService, RepositoryUser repositoryUser) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.repositoryUser = repositoryUser;
    }

    @GetMapping("/create-user")
    public String showCreateUserForm(Model model) {
    model.addAttribute("dtouser", new DtoUser());
    model.addAttribute("roles", Role.values());

    return "new-user";
}

    @PostMapping("/user")
    public String createUser(@Validated(OnCreate.class) @ModelAttribute("dtouser") DtoUser dtoUser, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("dtouser", dtoUser);
            model.addAttribute("roles", Role.values()); //important!!

            return "new-user"; // Return to the form with validation errors
        }

        if (repositoryUser.existsByUsername(dtoUser.getUsername())) {
            result.rejectValue("username", "error.dtouser", "Username already exists");
            model.addAttribute("roles", Role.values());
            return "new-user";
        }
        userService.createUser(dtoUser);
        return "redirect:/";
    }

    @GetMapping("/users/{userId}/assign-project")
    @PreAuthorize("hasRole('ADMIN')")
    public String showProjectAssignmentForm(@PathVariable Long userId, Model model) {
        List<DtoProject> existingProjects = projectService.getAllProjects(); // You can filter here if needed

        model.addAttribute("userId", userId);
        model.addAttribute("dtoProject", new DtoProject());
        model.addAttribute("existingProjects", existingProjects);
        return "assign-project";
    }

    @PostMapping("/users/{userId}/assign-project")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignProjectToUser(
            @PathVariable Long userId,
            @ModelAttribute DtoProject dtoProject,
            Principal principal) {

        Long adminId = userService.findByUsername(principal.getName()).getId();
        userService.addProjectToUser(adminId, userId, dtoProject);
        return "redirect:/"; 
    }

    @GetMapping("/users/{userId}/edit")
    public String showUpdateUserForm(@PathVariable Long userId, Model model){
        DtoUser dtoUser = userService.getUserById(userId);
        model.addAttribute("dtoUser", dtoUser);
        model.addAttribute("userId", userId);
        return "edit-user";  // returns the view name for editing user
    }


    @PutMapping("/users/{userId}/edit")
    public String updateUser(@PathVariable Long userId, @Validated(OnUpdate.class) @ModelAttribute("dtoUser") @Valid DtoUser dtoUser, BindingResult bindingResult,Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            // CHECKS IF THERE ARE VALIDATION ERRORS
            // If there are errors, redirect back to the edit form with error messages
            model.addAttribute("dtoUser", dtoUser);
            System.out.println("Validation errors: " + bindingResult);
            System.out.println("Validation errors: " + bindingResult);

        return "edit-user";
    }


        try {
            User currentUser = userService.findByUsername(userDetails.getUsername()); // get full user object
            userService.updateUser(userId, dtoUser, currentUser);
        } catch (IllegalArgumentException e) {
            model.addAttribute("dtoUser", dtoUser);
            model.addAttribute("updateError", e.getMessage());
            return "edit-user"; // show error message on the same page
        }

        return "redirect:/";
    }

    @DeleteMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user.");
        }
        return "redirect:/"; // Or your user list page
    }

//---------------------For Projects-------------------------------------------------------

    @GetMapping("/projects/{projectId}/edit")
    public String showUpdateProjectForm(@PathVariable Long projectId, Model model){
        DtoProject dtoProject = projectService.getProjectById(projectId);
        model.addAttribute("dtoProject", dtoProject);
        model.addAttribute("projectId", projectId);

        return "edit-project";  // returns the view name for editing project
    }

    @PutMapping("/projects/{projectId}/edit")
    public String updateProject(@PathVariable Long projectId, @ModelAttribute("dtoProject") @Valid DtoProject dtoProject, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // CHECKS IF THERE ARE VALIDATION ERRORS
            // If there are errors, redirect back to the edit form with error messages
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.dtoProject", bindingResult);
            redirectAttributes.addFlashAttribute("dtoProject", dtoProject);
            System.out.println("Validation errors: " + bindingResult);

        return "redirect:/admin/projects/" + projectId + "/edit";
    }


        try {
            projectService.updateProject(projectId, dtoProject);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/projects/" + projectId + "/edit?error=" + e.getMessage();
        }

        return "redirect:/";
    }

    @PostMapping("/users/{userId}/assign-existing-project")
    public String assignExistingProjectToUser(@PathVariable Long userId, @RequestParam Long projectId) {
        userService.assignExistingProjectToUser(userId, projectId);
        return "redirect:/";
    }

    @DeleteMapping("/projects/{projectId}/delete")
    public String deleteProjectCompletely(@PathVariable Long projectId, RedirectAttributes redirectAttributes) {
        projectService.deleteProject(projectId);
        redirectAttributes.addFlashAttribute("message", "Project deleted for all users.");
        return "redirect:/";
    }


    @PostMapping("/users/{userId}/projects/{projectId}/remove")
    public String removeProjectFromUser(@PathVariable Long userId, @PathVariable Long projectId, Principal principal) {
        Long adminId = userService.getUserIdFromPrincipal(principal); // Or however you track current user
        userService.removeProjectFromUser(userId, adminId, projectId);
        return "redirect:/"; 
    }
//---------------------For Tasks-------------------------------------------------------
    @GetMapping("/projects/{projectId}/add-task")
    public String showAddTaskForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("dtoTask", new DtoTask());
        return "add-task"; // name of the Thymeleaf view you’ll create
    }

    @PostMapping("/projects/{projectId}/add-task")
    public String addTaskToProject(
            @PathVariable Long projectId,
            @ModelAttribute("dtoTask") @Valid DtoTask dtoTask,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.dtoTask", bindingResult);
            redirectAttributes.addFlashAttribute("dtoTask", dtoTask);
            return "redirect:/admin/projects/" + projectId + "/add-task";
        }

        try {
            projectService.addTaskToProject(projectId, dtoTask);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/projects/" + projectId + "/add-task?error=" + e.getMessage();
        }

        return "redirect:/"; // or redirect back to project detail
    }

    @GetMapping("/tasks/{taskId}/edit")
    public String showEditTaskForm(@PathVariable Long taskId, Model model) {
        DtoTask dtoTask = taskService.getTaskById(taskId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("dtoTask", dtoTask);
        return "edit-task"; 
    }

    @PutMapping("/tasks/{taskId}/edit")
    public String updateTask(@PathVariable Long taskId, @ModelAttribute("dtoTask") @Valid DtoTask dtoTask, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // CHECKS IF THERE ARE VALIDATION ERRORS
            // If there are errorsredirect back to the edit form with error messages
            
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.dtoTask", bindingResult);
            redirectAttributes.addFlashAttribute("taskId", taskId);
            redirectAttributes.addFlashAttribute("dtoTask", dtoTask);
            System.out.println("Validation errors: " + bindingResult);

        return "redirect:/admin/tasks/" + taskId + "/edit";
    }


        try {
            taskService.updateTask(taskId, dtoTask);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/tasks/" + taskId + "/edit?error=" + e.getMessage();
        }

        return "redirect:/";
    }

    @DeleteMapping("/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable Long taskId, RedirectAttributes redirectAttributes) {
        taskService.deleteTaskById(taskId);
        redirectAttributes.addFlashAttribute("message", "Task deleted successfully.");
        return "redirect:/";
    }

    


}
