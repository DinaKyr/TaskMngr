package com.example.TaskMngr.dto;

import lombok.Getter;
import lombok.Setter;

//import java.time.Instant;
import java.time.LocalDate;



//import jakarta.persistence.Table;

@Getter 
@Setter

public class DtoTask{

    private Long id;
    private String description;
    private Boolean isComplete;
    private Priority priority= Priority.LOW;
    private LocalDate deadline;
    private String status;

    public DtoTask() {}

    public DtoTask(Long id ,String description, Boolean isComplete ,Priority priority,  LocalDate deadline ,String status) {
        this.id = id;
        this.description = description;
        this.isComplete = isComplete;
        this.priority = priority;
        this.deadline=deadline;
        this.status = status;

    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}

