package com.example.discordia.mappers;


import com.example.discordia.dto.UserDto;
import com.example.discordia.model.UserModel;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userModelToDto(UserModel user);
    UserModel dtoToEntity(UserDto dto);

    UserModel map(UUID userId);
    UUID map(UserModel user);
}
