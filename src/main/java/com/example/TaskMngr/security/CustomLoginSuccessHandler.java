package com.example.TaskMngr.security;

import java.io.IOException;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.TaskMngr.models.User;
import com.example.TaskMngr.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomLoginSuccessHandler(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException {

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        String contextPath = request.getContextPath(); 
    if (user.isForcePasswordChange()) {
        response.sendRedirect(contextPath +"/edit?forcePasswordChange=true");
    } else {
        response.sendRedirect(contextPath + "/");
        }
    }
}//you dont know