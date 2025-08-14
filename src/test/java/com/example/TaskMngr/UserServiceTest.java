package com.example.TaskMngr;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.TaskMngr.dto.DtoProject;
import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.mappers.UserMapper;
import com.example.TaskMngr.models.User;
import com.example.TaskMngr.repositories.RepositoryUser;
import com.example.TaskMngr.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.TaskMngr.models.Role;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private RepositoryUser repositoryUser;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;
    Role role = Role.BASIC; 
    List<DtoProject> projects ;

    @Test
    public void testCreateUser() {
        
        // Given
        DtoUser dtoUser = new DtoUser(1L,"testUser", "test@example.com", "password123",role,projects);
        User user = new User("testUser", "test@example.com", "encodedPassword",role);

        Mockito.when(passwordEncoder.encode(dtoUser.getPassword())).thenReturn("encodedPassword");
        Mockito.when(repositoryUser.existsByUsername(dtoUser.getUsername())).thenReturn(false);
        Mockito.when(repositoryUser.save(Mockito.any(User.class))).thenReturn(user);

        // When
        User createdUser = userService.createUser(dtoUser);

        // Then
        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(role, createdUser.getRole());

        System.out.println("testCreateUser PASSED");

    }

    @Test 
    public void testcreateUser_userExists(){
        // Given
        DtoUser dtoUser = new DtoUser(1L,"testUser", "test@example.com", "password123",role,projects);
        //Mockito.when(passwordEncoder.encode(dtoUser.getPassword())).thenReturn("encodedPassword");
        Mockito.when(repositoryUser.existsByUsername(dtoUser.getUsername())).thenReturn(true);

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
        userService.createUser(dtoUser);
    });

    }

    @Test
    public void testUpdateUser() {
        // Given
        User user = new User("testUser", "test@example.com", "encodedPassword", role);
        Long id = 1L;
        user.setId(id);
        DtoUser dtoUser = new DtoUser(1L, "newtestUser", "newtest@example.com", "newpassword123", role, projects);
        dtoUser.setConfirmPassword("newpassword123");
        User currentUser = new User("currentUser", "cur@example.com", "currentPassword", Role.BASIC);

        Mockito.when(repositoryUser.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(dtoUser.getPassword())).thenReturn("newencodedPassword");
        Mockito.when(repositoryUser.save(Mockito.any(User.class))).thenReturn(user);

        // When
        User updatedUser = userService.updateUser(id, dtoUser, currentUser);

        // Then
        assertNotNull(updatedUser);
        assertEquals("newtestUser", updatedUser.getUsername());
        assertEquals("newtest@example.com", updatedUser.getEmail());
        assertEquals("newencodedPassword", updatedUser.getPassword());
        assertEquals(role, updatedUser.getRole());

        System.out.println("testUpdateUser PASSED");
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;
        User user = new User("testUser", "testexamp@.com" , "encodedPassword",role);
        user.setId(id);

        userService.deleteUser(id);
        // Verify that the delete method was called with the correct ID
        Mockito.verify(repositoryUser, Mockito.times(1)).deleteById(id);

        System.out.println("testDeleteUser PASSED");

    }

    // @Test
    // public void getUserDtosByIds() {
    //     List<Long> ids = new ArrayList<Long>();
    //     for (int i = 0; i < ids.size(); i++) {        
    //         User user = new User("testUser", "testexamp@.com" , "encodedPassword",role);
    //         user.setId(ids.get(i));

    //         DtoUser dtoUser = new DtoUser(1L,"testUser", "testexamp@.com" , "1234567890",role,projects);
    //         Mockito.when(repositoryUser.findById(ids.get(i))).thenReturn(Optional.of(user));
    //         Mockito.when(userMapper.toDto(user)).thenReturn(dtoUser);

    //     }
    //     // When 
    //     List<DtoUser> result = userService.getUserDtosByIds(ids);

    //     // Then
    //     assertNotNull(result);
    //     for(int i = 0; i < ids.size(); i++) {
    //         assertEquals("testUser", result.get(i).getUsername());
    //         assertEquals("testexamp@.com", result.get(i).getEmail());
    //         assertEquals("encodedPassword", result.get(i).getPassword());
    //         assertEquals(role, result.get(i).getRole());
    //     }   

    //     System.out.println("getUserDtosByIds PASSED");
    // }

} 




    
