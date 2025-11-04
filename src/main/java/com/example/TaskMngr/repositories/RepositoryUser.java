package com.example.TaskMngr.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.models.User;


@Repository
public interface RepositoryUser extends JpaRepository<User,Long> {

    //to find users by role
    List<User> findByRole(String role);

    Optional<User> findByUsername(String username);

    //to check if a user exists by role
    boolean existsByRole(Role role);

    boolean existsByUsername(String username);



}
