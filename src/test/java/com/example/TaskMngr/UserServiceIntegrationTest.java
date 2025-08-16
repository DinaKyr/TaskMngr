package com.example.TaskMngr;

import com.example.TaskMngr.models.User;
import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.repositories.RepositoryUser;
import com.example.TaskMngr.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryUser repositoryUser;

    @Test
    public void testCreateUserIntegration() {
        // Given
        Role role=Role.BASIC;
        DtoUser dtoUser = new DtoUser(
                null,
                "integrationUser",
                "integration@example.com",
                "password123",
                role,
                null //empty list of projects
        );

        // When
        User createdUser = userService.createUser(dtoUser);

        // Then
        assertNotNull(createdUser.getId(), "ID should be generated");
        assertEquals("integrationUser", createdUser.getUsername());
        assertEquals("integration@example.com", createdUser.getEmail());
        assertNotEquals("password123", createdUser.getPassword(), "Password should be encoded");

        // saved in db
        User found = repositoryUser.findByUsername("integrationUser").orElse(null);
        assertNotNull(found, "User should be saved in DB");
        assertEquals(createdUser.getId(), found.getId());
    }
}
