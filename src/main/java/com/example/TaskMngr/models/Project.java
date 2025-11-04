package com.example.TaskMngr.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
//import java.time.Instant;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
//import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Getter 
@Setter
@Entity
@Table(name ="projects")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Boolean isComplete;
    private LocalDate deadline;

    @ManyToMany(mappedBy="projects")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy="project", cascade = CascadeType.ALL)
    private List<Task> tasks=new ArrayList<>();

    @Column(nullable=false)
    private boolean archived = false;

    public Project(){}

    public Project(String name, String description, Boolean isComplete, LocalDate deadline) {
        this.name = name;
        this.description = description;
        this.isComplete = isComplete;
        this.deadline = deadline;
    
    }
    public void addTask(Task task){
        tasks.add(task);
        if (task.getProject() != this) {
            task.setProject(this);
        }
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        if (task.getProject()==this) {
            task.setProject(null);
        }
    }


}
