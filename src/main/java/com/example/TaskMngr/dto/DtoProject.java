package com.example.TaskMngr.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//import java.time.Instant;


//import jakarta.persistence.Table;

@Getter 
@Setter

public class DtoProject {

    private Long id;

    private String name;
    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    @Column(length = 255)
    private String description;
    private Boolean isComplete;
    private LocalDate deadline;

    private List<DtoTask> tasks=new ArrayList<>();

    public DtoProject() {}

    public DtoProject(Long id,String name, String description, Boolean isComplete , LocalDate deadline ,List<DtoTask> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isComplete = isComplete;
        this.deadline=deadline;
        this.tasks = tasks;
    }
}

