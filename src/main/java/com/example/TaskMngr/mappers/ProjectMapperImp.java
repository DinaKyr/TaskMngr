package com.example.TaskMngr.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.TaskMngr.dto.DtoProject;
import com.example.TaskMngr.models.Project;
import com.example.TaskMngr.models.User;

@Component
public class ProjectMapperImp implements ProjectMapper{

    private TaskMapper taskMapper;

    @Autowired
    public ProjectMapperImp(TaskMapper taskMapper) {
        this.taskMapper =taskMapper;
    }

    @Override
    public DtoProject toDto(Project project){
        if (project==null) return null;
        return new DtoProject(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getIsComplete(),
            project.getDeadline(),
            // project.getUsers(),
            taskMapper.toDtoList(project.getTasks()),
            project.getUsers().stream()
               .map(User::getUsername)
               .collect(Collectors.toList()) // List<String> usernames
    );
        
        

    }


    @Override
    public Project toEntity(DtoProject dtoProject) {
        if (dtoProject==null) return null;
        Project project=new Project();
        project.setName(dtoProject.getName());
        project.setDescription(dtoProject.getDescription());
        project.setIsComplete(dtoProject.getIsComplete());
        project.setDeadline(dtoProject.getDeadline());
        project.setTasks(taskMapper.toEntityList(dtoProject.getTasks()));
        return project;

    }

    @Override
    public List<DtoProject> toDtoList(List<Project> projects) {
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Project> toEntityList(List<DtoProject> dtoProjects) {
        return dtoProjects.stream().map(this::toEntity).collect(Collectors.toList());

    }

}