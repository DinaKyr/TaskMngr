package com.example.TaskMngr.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Boolean isComplete;
    private Priority priority;
    private LocalDate deadline;
    private String status;

    @ManyToOne
    @JoinColumn(name = "project_id")  
    private Project project;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public Task(){}

    public Task(String description, Boolean isComplete ,Priority priority,  LocalDate deadline ,String status) {
       
        this.description = description;
        this.isComplete = isComplete;
        this.priority = priority;
        this.deadline=deadline;
        this.status = status;

    }

}
