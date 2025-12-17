package com.example.TaskMngr.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.TaskMngr.models.User;
import com.example.TaskMngr.repositories.RepositoryUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private RepositoryUser repositoryUser;
    public CustomUserDetailsService(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repositoryUser.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        System.out.println("Loading user: " + user.getUsername());
        System.out.println("Password (hashed): "+user.getPassword());
        System.out.println("Role: " + user.getRole());

        System.out.println("Finding user by username: " + username);
        Optional<User> opt=repositoryUser.findByUsername(username);
        System.out.println("User present: " + opt.isPresent());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }


    
}
