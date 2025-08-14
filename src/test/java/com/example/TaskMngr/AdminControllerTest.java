package com.example.TaskMngr;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.example.TaskMngr.controllers.AdminController;
import com.example.TaskMngr.dto.DtoProject;
import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.models.User;
import com.example.TaskMngr.services.ProjectService;
import com.example.TaskMngr.services.TaskService;
import com.example.TaskMngr.services.UserService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)


    public class AdminControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private ProjectService projectService;

        @MockBean
        private TaskService taskService;

        Role role = Role.BASIC;
        List<DtoProject> projects ;
        
        @WithMockUser(username = "admin", roles = {"ADMIN"})

        @Test
        void shoulReturnUpdatedUser() throws Exception {
            User currentUser = new User("admin", "admin@example.com", "encodedPassword",role);
            currentUser.setId(1L);
            DtoUser dtoUser = new DtoUser(1L,"testUser", "test@example.com", "password123",role,projects);


            // Mock the userService to return the user when findById is called
            Mockito.when(userService.findByUsername("admin")).thenReturn(currentUser);

            Mockito.when(userService.updateUser(1L,dtoUser,currentUser)).thenReturn(currentUser);

            // Perform the request and verify the response
            mockMvc.perform(put("/admin/users/1/edit")
            .with(csrf())
            .param("id", "1")
            .param("username", "testUser")
            .param("email", "test@example.com")
            .param("password", "password123")
            .param("role", "BASIC"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/")); //redirect of updateuser


        } 
}
