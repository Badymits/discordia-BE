package com.example.discordia.service.UserService;

import com.example.discordia.model.UserModel;
import com.example.discordia.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    UserModel findByUsername(String username);
    UserDto findByUserId(UUID userId);

    UserDto saveUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, MultipartFile image);

    List<UserDto> getUsers(String searchTerm, UUID userId);

}
