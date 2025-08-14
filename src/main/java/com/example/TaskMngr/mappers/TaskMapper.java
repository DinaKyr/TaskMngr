package com.example.TaskMngr.mappers;

import java.util.List;

import com.example.TaskMngr.dto.DtoTask;
import com.example.TaskMngr.models.Task;

public interface TaskMapper {

    DtoTask toDto(Task task);
    Task toEntity(DtoTask dtoTask);
    List<DtoTask> toDtoList(List<Task> tasks);
    List<Task> toEntityList(List<DtoTask> dtoTasks);
}


