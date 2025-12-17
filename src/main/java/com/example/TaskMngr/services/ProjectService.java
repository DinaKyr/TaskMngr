package com.example.TaskMngr.services;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.TaskMngr.dto.DtoProject;
import com.example.TaskMngr.dto.DtoTask;
import com.example.TaskMngr.dto.ProjectStats;
import com.example.TaskMngr.mappers.ProjectMapper;
//import com.example.TaskMngr.dto.DtoTask;
//import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.mappers.TaskMapper;
import com.example.TaskMngr.models.Project;
import com.example.TaskMngr.models.Task;
import com.example.TaskMngr.models.User;
import com.example.TaskMngr.repositories.RepositoryProject;
//import com.example.TaskMngr.repositories.RepositoryUser;
import com.example.TaskMngr.repositories.RepositoryTask;

@Service

public class ProjectService {

    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private RepositoryProject repositoryProject;
    private RepositoryTask repositoryTask;


    public ProjectService(UserService userService,ProjectMapper projectMapper,RepositoryProject repositoryProject,
            TaskMapper taskMapper, RepositoryTask repositoryTask) {
        this.repositoryTask = repositoryTask;
        this.userService = userService;
        this.projectMapper=projectMapper;
        this.repositoryProject = repositoryProject;
        this.taskMapper = taskMapper;

    }

    @Transactional
    public List<DtoProject> getAllProjects() {
        List<Project> projects=repositoryProject.findAll();
        return projects.stream()
                .map(projectMapper::toDto) //projectMapper from Project to DtoProject
                .collect(Collectors.toList());
    }

    @Transactional
    public DtoProject getProjectById(Long id) { 
        Project project =repositoryProject.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        return projectMapper.toDto(project); // projectmapper same
    }

    @Transactional
    public List<DtoProject> getProjectById(List<Long> ids) {
        List<Project> projects=repositoryProject.findAllById(ids);
        
        if (projects.size() != ids.size()) {
            throw new RuntimeException("One or more projects not found for the provided IDs");
        }
    
       /*return projects.stream()
                .map(project -> new DtoProject(
                        project.getName(), 
                        project.getDescription(),
                        project.getIsComplete(),
                        project.getDeadline(),
                        taskMapper.toDtoList(project.getTasks())) // Use mapDtoTask to convert Task to DtoTask
                )
                .collect(Collectors.toList());*/
                return projects.stream()
                .map(projectMapper::toDto) 
                .collect(Collectors.toList());
    }

    @Transactional
    public Project createProject(DtoProject dtoProject , List<Long> userIds){

        List<User> users =userService.getUsersByIds(userIds);
        Project project =new Project(
            dtoProject.getName(),
            dtoProject.getDescription(),  
            dtoProject.getIsComplete(),
            dtoProject.getDeadline()
            );
        
        project.setUsers(users); 
        return repositoryProject.save(project);

    }

    @Transactional
    public Project updateProject(Long id, DtoProject dtoProject) {
        Project project=repositoryProject.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(dtoProject.getName());
        project.setDescription(dtoProject.getDescription());
        project.setIsComplete(dtoProject.getIsComplete());
        project.setDeadline(dtoProject.getDeadline());

        return repositoryProject.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId){
        Project project =repositoryProject.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        //1: Break the link between this project and all users
        List<User> users = new ArrayList<>(project.getUsers());
        for (User user : users) {
            user.getProjects().remove(project);
        }
        project.getUsers().clear(); //clean up both sides

        //2: Delete tasks associated with the project (if cascade/orphanRemoval isn't doing it)
        for (Task task : new ArrayList<>(project.getTasks())) {
            task.setProject(null);
            repositoryTask.delete(task);
        }
        project.getTasks().clear(); // Optional safety

        //3: delete
        repositoryProject.delete(project);
    }

    @Transactional
    public void addTaskToProject(Long id, DtoTask dtoTask) {
        Project project =repositoryProject.findById(id).orElseThrow(() ->new RuntimeException("Project not found"));
        Task task=taskMapper.toEntity(dtoTask);
        project.addTask(task);
        repositoryProject.save(project); 
    }

    @Transactional
    public void removeTaskFromProject(Long projectId, Long taskId) {
        Project project =repositoryProject.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Task task=repositoryTask.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        project.getTasks().remove(task);
        task.setProject(null);

        repositoryProject.save(project);
    }

    public List<ProjectStats> getCompletedProjectsPerUser() {
        return repositoryProject.countCompletedProjectsPerUser();
    }
}
