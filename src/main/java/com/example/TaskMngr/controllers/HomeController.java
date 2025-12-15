package com.example.TaskMngr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.TaskMngr.services.ProjectService;
import com.example.TaskMngr.services.TaskService;
import com.example.TaskMngr.services.UserService;

@Controller
public class HomeController {

    private final UserService userService;
    private final TaskService taskService;
    private final ProjectService projectService;

    public HomeController(UserService userService, TaskService taskService, ProjectService projectService) {
        this.userService =userService;
        this.taskService =taskService;
        this.projectService =projectService;
    }
    
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("users", userService.getAllUsers());
        modelAndView.addObject("tasks", taskService.getAllTasks());
        modelAndView.addObject("projects", projectService.getAllProjects());
        return modelAndView;
    }


}
