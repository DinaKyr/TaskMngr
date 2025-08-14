package com.example.TaskMngr.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.models.User;

@Component
public class UserMapperImp implements UserMapper {

    private ProjectMapper projectMapper;

    @Autowired
    public UserMapperImp(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public DtoUser toDto(User user) {
        if (user == null) return null;
        return new DtoUser(user.getId(),user.getUsername(), user.getEmail(), null, user.getRole(),projectMapper.toDtoList(user.getProjects())); // No password in DTO
    }

    @Override
    public User toEntity(DtoUser dtoUser) {
        if (dtoUser == null) return null;
        return new User(dtoUser.getUsername(), dtoUser.getEmail(), dtoUser.getPassword(), dtoUser.getRole());
    }

    @Override
    public List<DtoUser> toDtoList(List<User> users) {
        return users.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
    }

    @Override
    public List<User> toEntityList(List<DtoUser> dtoUsers) {
        return dtoUsers.stream()
                       .map(this::toEntity)
                       .collect(Collectors.toList());
    }
}
