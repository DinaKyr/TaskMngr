package com.example.TaskMngr.services;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.TaskMngr.dto.DtoProject;

import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.mappers.UserMapper;
import com.example.TaskMngr.mappers.ProjectMapper;

import com.example.TaskMngr.models.Project;
import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.models.User;
import com.example.TaskMngr.repositories.RepositoryProject;

import com.example.TaskMngr.repositories.RepositoryUser;

import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private RepositoryUser repositoryUser;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private RepositoryProject repositoryProject;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(RepositoryUser repositoryUser, UserMapper userMapper,
                       ProjectMapper projectMapper, RepositoryProject repositoryProject, PasswordEncoder passwordEncoder) {
        this.repositoryUser = repositoryUser;
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
        this.repositoryProject = repositoryProject;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<DtoUser> getAllUsers() {
        List<User> users =repositoryUser.findAll();
        return userMapper.toDtoList(users);
    }

    @Transactional(readOnly = true)
    public List<DtoUser> getAllUsersByRole(String role) {
        List<User> users=repositoryUser.findByRole(role);
        return userMapper.toDtoList(users);
    }

    @Transactional
    public User createUser(DtoUser dtoUser) {
    if (repositoryUser.existsByUsername(dtoUser.getUsername())) {
        throw new IllegalArgumentException("Username '"+dtoUser.getUsername()+"' already exists");
        }

        User user=userMapper.toEntity(dtoUser);
        user.setPassword(passwordEncoder.encode(dtoUser.getPassword()));
        return repositoryUser.save(user);
    }


    // @Transactional
    // public User createUser(DtoUser dtoUser) {
    //     // Encrypt password before saving the user
    //     String encodedPassword = passwordEncoder.encode(dtoUser.getPassword());

    //     //Role role = dtoUser.getRole() != null ? dtoUser.getRole() : Role.BASIC;

    //     // Convert DTO to entity
    //     User user = new User(dtoUser.getUsername(), dtoUser.getEmail(), encodedPassword,dtoUser.getRole());

    //     // Save to repository using the instance of repositoryUser
    //     return repositoryUser.save(user);
    // }

    public List<User> getUsersByIds(List<Long> ids) {
        List<User> users =repositoryUser.findAllById(ids);
        if (users.size() != ids.size()) {
            throw new RuntimeException("One or more users not found for the provided IDs");
        }
        return users; //return entities
    }

    public User findByUsername(String username) {
        return repositoryUser.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " +username));
    }

    @Transactional(readOnly = true)
    public DtoUser findDtoByUsername(String username) {
    User user = repositoryUser.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException("User not found: "+username));

    return userMapper.toDto(user);
    }

    // public List<DtoUser> getUserDtosByIds(List<Long> ids) {
    //     List<User> users = repositoryUser.findAllById(ids);
    //     if (users.size() != ids.size()) {
    //         throw new RuntimeException("One or more users not found for the provided IDs");
    //     }
    //     return userMapper.toDtoList(users);
    // }


    @Transactional
    public DtoUser getUserById(Long id) {
        User user=repositoryUser.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }


    @Transactional
    public User updateUser(Long id, DtoUser dtoUser, User currentUser){
        User user =repositoryUser.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setUsername(dtoUser.getUsername());
        user.setEmail(dtoUser.getEmail());

        boolean isSelfEdit =currentUser.getId().equals(user.getId());
        boolean isAdmin =currentUser.getRole()==Role.ADMIN;
        if (isAdmin && !isSelfEdit) {
            user.setForcePasswordChange(dtoUser.isForcePasswordChange());
        }
            user.setRole(dtoUser.getRole());

        return repositoryUser.save(user);

    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = repositoryUser.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        //make a copy to avoid ConcurrentModificationException
        List<Project> projects = new ArrayList<>(user.getProjects());

        for (Project project : projects) {
            //Remove the user from the project
            project.getUsers().remove(user);
            user.getProjects().remove(project); // optional if using orphanRemoval

            //If the project now has no users, delete it
            if (project.getUsers().isEmpty()) {
                repositoryProject.delete(project);
            } else {
                repositoryProject.save(project); // saves the updated user list
            }
        }

        repositoryUser.deleteById(userId);
    }


    @Transactional
    public void addProjectToUser(Long adminid,Long basicid, DtoProject dtoproject) {

        User adminUser = repositoryUser.findById(adminid).orElseThrow(() -> new RuntimeException("Admin user not found"));
        User basicUser = repositoryUser.findById(basicid).orElseThrow(() -> new RuntimeException("Basic user not found"));

        if (adminUser.getRole()!=Role.ADMIN) {
            throw new RuntimeException("Only admin users can add projects to other users");
        }
        if (basicUser.getRole() !=Role.BASIC) {
            throw new RuntimeException("Projects can only be assigned to basic users.");
        }
        Project project = projectMapper.toEntity(dtoproject);
        basicUser.addProject(project);
        //project.getUsers().add(basicUser);
        repositoryProject.save(project); 
    }

    @Transactional
    public void removeProjectFromUser(Long basicId, Long adminId, Long projectId) {
        User adminUser =repositoryUser.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin user not found"));
        User basicUser =repositoryUser.findById(basicId)
            .orElseThrow(() -> new RuntimeException("Basic user not found"));
        Project project=repositoryProject.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        if (adminUser.getRole()!=Role.ADMIN) {
            throw new RuntimeException("Only admin users can remove projects from other users");
        }
        if (basicUser.getRole()!=Role.BASIC) {
            throw new RuntimeException("Projects can only be removed from basic users.");
        }

        basicUser.removeProject(project);
        repositoryUser.save(basicUser);

        if (project.getUsers()==null || project.getUsers().isEmpty()) {
            project.setArchived(true);
        }

        repositoryProject.save(project);
    }



    // @Transactional(readOnly = true)
    // public List<DtoProject> getProjectsByUserId(Long userId) {
    //     User user = repositoryUser.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    //     if (user.getRole() != Role.BASIC) {
    //         throw new RuntimeException("Projects can only be retrieved for basic users.");
    //     }
    //     return projectMapper.toDtoList(user.getProjects());
    // }

    @Transactional
    public void assignExistingProjectToUser(Long userId, Long projectId) {
        User user =repositoryUser.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Project project =repositoryProject.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        project.getUsers().add(user);

        user.getProjects().add(project);

        //save the updated entities
        repositoryProject.save(project); //needed if project owns the relation
        repositoryUser.save(user);
    }

        public Long getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = findByUsername(username); 
        return user.getId();
    }

    public Long countUsers() {
        return repositoryUser.count();

    }

    @Transactional
    public User updateOwnAccount(String username, DtoUser dtoUser) {
        User user = repositoryUser.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dtoUser.getUsername());
        user.setEmail(dtoUser.getEmail());

        String newPassword = dtoUser.getPassword();
        String confirmPassword = dtoUser.getConfirmPassword();

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (newPassword.length() < 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setForcePasswordChange(false);
        }

        return repositoryUser.save(user);
    }

}
    //private Optional<DtoUser> getById(Long id){
    //    return repositoryUser.findById(id);
    //}

    //public User save(User user)

    








