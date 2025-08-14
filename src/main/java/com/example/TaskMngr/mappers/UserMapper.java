package com.example.TaskMngr.mappers;

import java.util.List;

import com.example.TaskMngr.dto.DtoUser;
import com.example.TaskMngr.models.User;

public interface UserMapper {
    DtoUser toDto(User user);
    User toEntity(DtoUser dtoUser);
    List<DtoUser> toDtoList(List<User> users);
    List<User> toEntityList(List<DtoUser> dtoUsers);
}
