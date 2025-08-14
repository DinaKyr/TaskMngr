package com.example.TaskMngr.mappers;

import java.util.List;

import com.example.TaskMngr.dto.DtoProject;
import com.example.TaskMngr.models.Project;

public interface ProjectMapper {
    DtoProject toDto(Project project);
    Project toEntity(DtoProject dtoProject);
    List<DtoProject> toDtoList(List<Project> projects);
    List<Project> toEntityList(List<DtoProject> dtoProjects);
}
