package com.example.TaskMngr.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TaskMngr.dto.ProjectStats;
import com.example.TaskMngr.services.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectControllerRest {

    private final ProjectService projectService;

    public ProjectControllerRest(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/stats")
    public List<ProjectStats> getProjectStats() {
        return projectService.getCompletedProjectsPerUser();
    }
}
