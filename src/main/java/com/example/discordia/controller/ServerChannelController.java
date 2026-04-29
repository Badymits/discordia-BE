package com.example.discordia.controller;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.service.ServerChannel.ServerChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/update/{channelId}")
    public ResponseEntity<String> updateChannel(
            @PathVariable UUID channelId,
            @RequestBody ServerChannelDto channelDto
    ){
        log.info("Received data: {}", channelDto);
        channelService.updateChannel(channelId, channelDto);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("{channelId}")
    public int deleteChannel(
            @PathVariable UUID channelId
    ){
        log.info("Received confirmation to delete");
        return channelService.deleteChannel(channelId);

    }
}
