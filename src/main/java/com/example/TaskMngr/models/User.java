package com.example.TaskMngr.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
//import java.time.Instant;
import java.util.List;

//import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;


@Getter 
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private Long id;
    private String username;
    private String password;
    private String email;
    // @Column(nullable = false)
    private boolean forcePasswordChange = false;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
        name="user_projects",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="project_id")
    )
    private List<Project> projects = new ArrayList<>();//maybe hashset
    


    public User(){}

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.BASIC; //BASIC if role is null
    }

    public void addProject(Project project) {
    if (!projects.contains(project)) {
        projects.add(project);
        }
    if (!project.getUsers().contains(this)) {
        project.getUsers().add(this);
        }
    }

    public void removeProject(Project project) {
    if (projects.contains(project)) {
        projects.remove(project);
        }
    if (project.getUsers().contains(this)) {
        project.getUsers().remove(this);
        }
    }

}

