package com.example.TaskMngr.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.TaskMngr.dto.DtoTask;
import com.example.TaskMngr.models.Task;

@Component
public class TaskMapperImp implements TaskMapper {

    @Override
    public DtoTask toDto(Task task) {
        if (task == null) return null;
        return new DtoTask(
            task.getId(),
            task.getDescription(),
            task.getIsComplete(),
            DtoTask.Priority.valueOf(task.getPriority().name()),
            task.getDeadline(),
            task.getStatus()
        );
    }

    @Override
    public Task toEntity(DtoTask dtoTask) {
        if (dtoTask == null) return null;
        Task task = new Task();
        task.setDescription(dtoTask.getDescription());
        task.setDeadline(dtoTask.getDeadline());
        task.setIsComplete(dtoTask.getIsComplete());
        task.setPriority(Task.Priority.valueOf(dtoTask.getPriority().name()));
        task.setStatus(dtoTask.getStatus());
        return task;
    }

    @Override
    public List<DtoTask> toDtoList(List<Task> tasks) {
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Task> toEntityList(List<DtoTask> dtoTasks) {
        return dtoTasks.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
