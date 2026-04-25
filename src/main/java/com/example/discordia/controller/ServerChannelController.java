package com.example.discordia.controller;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.repository.ServerCategoryRepository;
import com.example.discordia.repository.ServerChannelRepository;
import com.example.discordia.service.ServerChannel.ServerChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/channels")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ServerChannelController {

    private final ServerChannelService channelService;

    @PostMapping("/create-channel")
    public ResponseEntity<ServerChannelDto> createChannel (
        @RequestBody ServerChannelDto channelDto
    ){
        log.info("Channel dto received: {}", channelDto);
        ServerChannelDto dto = channelService.createChannel(channelDto);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("{channelId}")
    public ResponseEntity<ServerChannelDto> getChannel(
            @PathVariable UUID channelId
    ){
        log.info("Received id: {}", channelId);
        ServerChannelDto dto = channelService.getChannelById(channelId);

        return ResponseEntity.ok(dto);
    }

}
