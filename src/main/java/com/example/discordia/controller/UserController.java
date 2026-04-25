package com.example.discordia.controller;



import com.example.discordia.dto.UserDto;
import com.example.discordia.service.UserService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @RequestBody UserDto userDto
    ){
        log.info("Received data: ${}", userDto.toString());
        UserDto savedUser = userService.saveUser(userDto);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable UUID userId
    ){
        log.info("Received id: {}", userId);
        UserDto user = userService.findByUserId(userId);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @RequestPart("userData") UserDto userDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ){
        UserDto dto = userService.updateUser(userDto, image);
        return ResponseEntity.ok(dto);
    }

}
