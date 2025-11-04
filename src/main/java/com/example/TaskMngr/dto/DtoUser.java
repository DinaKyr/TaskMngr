package com.example.TaskMngr.dto;

import java.util.List;

import com.example.TaskMngr.models.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//import java.time.Instant;


//import jakarta.persistence.Table;

@Getter 
@Setter

public class DtoUser {

    private Long id;

    @NotBlank(message="Name is required", groups = {OnCreate.class, OnUpdate.class})
    @Size(min=2, max =50, message="Name must be between 2 and 50 characters", groups= {OnCreate.class, OnUpdate.class})
    private String username;

    @Email(message ="Email should be valid", groups= {OnCreate.class, OnUpdate.class})
    @NotBlank(message ="Email is required", groups= {OnCreate.class, OnUpdate.class})
    private String email;


    @NotBlank(message ="Password is required" ,groups=  OnCreate.class)
    @Size(min=8, message ="Password must be at least 8 characters", groups= OnCreate.class)
    private String password;

    private Role role = Role.BASIC; // Default role is BASIC
    private List<DtoProject> projects; 

    //@Size(min = 8, message = "Password must be at least 8 characters")
    private String confirmPassword;

    private boolean forcePasswordChange;
    public DtoUser() {}

    public DtoUser(Long id,String username, String email, String password, Role role, List<DtoProject> projects) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.projects = projects;
    }
}

