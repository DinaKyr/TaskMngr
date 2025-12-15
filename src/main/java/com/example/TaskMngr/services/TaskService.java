package com.example.TaskMngr.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.TaskMngr.dto.DtoTask;
import com.example.TaskMngr.mappers.TaskMapper;

import com.example.TaskMngr.models.Task;

import com.example.TaskMngr.repositories.RepositoryTask;

@Service
public class TaskService {
    //private final TaskMapper taskMapper;
    private RepositoryTask repositoryTask;
    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper, RepositoryTask repositoryTask) {
        this.taskMapper =taskMapper;
        this.repositoryTask =repositoryTask;
    }

    public List<DtoTask> getAllTasks() {
        List<Task> tasks =repositoryTask.findAll();
        return taskMapper.toDtoList(tasks);
    }

    @Transactional
    public void deleteTaskById(Long id) {
        repositoryTask.deleteById(id);
    }

    @Transactional
    public Task createTask(DtoTask dtoTask) {
        Task task =taskMapper.toEntity(dtoTask);
        return repositoryTask.save(task);
    }
    public DtoTask getTaskById(Long id) {
        Task task =repositoryTask.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (task != null) {
            return taskMapper.toDto(task);
        } else {
            return null; // or throw an exception
        }
    }

    @Transactional
    public Task updateTask(Long id, DtoTask dtoTask){
        Task task=repositoryTask.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setDescription(dtoTask.getDescription());  
        task.setDeadline(dtoTask.getDeadline());
        task.setIsComplete(dtoTask.getIsComplete());
        task.setPriority(Task.Priority.valueOf(dtoTask.getPriority().name()));
        task.setStatus(dtoTask.getStatus());
        return repositoryTask.save(task);

    }

    
}
