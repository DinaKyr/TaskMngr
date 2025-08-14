package com.example.TaskMngr.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.models.User;
import com.example.TaskMngr.repositories.RepositoryUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

@Component
public class UserSetupRunner implements CommandLineRunner {

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.default.username}")
    private String defaultUsername;

    @Value("${admin.default.password}")
    private String defaultPassword;

    @Value("${admin.default.role}")
    private String defaultRole;

    @Override
    public void run(String... args) throws Exception {
        boolean adminExists = repositoryUser.existsByRole(Role.ADMIN);
        if (!adminExists) {
            if (!defaultRole.equals("ADMIN")) {
                throw new IllegalArgumentException("Startup failed: ADMIN_DEFAULT_ROLE must be 'ADMIN'. Found: " + defaultRole);
            }
            User user = new User();
            user.setUsername(defaultUsername);
            user.setRole(Role.valueOf(defaultRole)); 
            user.setPassword(passwordEncoder.encode(defaultPassword));
            repositoryUser.save(user);
            System.out.println("Created admin user: " + defaultUsername);;
        }
        else {
            System.out.println("Admin user already exists.");
        }
    }
}