package com.example.discordia.controller;


import com.example.discordia.dto.ServerModelDto;
import com.example.discordia.dto.UserDto;
import com.example.discordia.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import com.example.discordia.service.ServerModel.ServerModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.discordia.service.UserService.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/server")
@RequiredArgsConstructor
@Slf4j
public class ServerModelController {

    private final ServerModelService serverModelService;
    private final UserService userService;

    @PostMapping("/create-server")
    public ResponseEntity<ServerModelDto> createServer(
            @RequestBody ServerModelDto createServerRequest
    ){
        log.info("Incoming create server request: {}", createServerRequest.toString());
        ServerModelDto serverDto = serverModelService.createServer(createServerRequest);
        return ResponseEntity.ok(serverDto);
    }

    @GetMapping("{serverId}")
    public ResponseEntity<ServerModelDto> getServer(
            @PathVariable UUID serverId
    ){
        log.info("Fetching Server By ID: {}", serverId);
        ServerModelDto serverModel = serverModelService.findByServerID(serverId);
        return ResponseEntity.ok(serverModel);
    }


    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ServerModelDto>> getServersByUserId(
            @PathVariable UUID userId
    ){
        UserDto user = userService.findByUserId(userId);

        log.info("found user: {}", user);

        List<ServerModelDto> list =
                serverModelService.getServersByUserId(userId, user.getUserName());

        return ResponseEntity.ok(list);
    }


}
