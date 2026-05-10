package com.example.discordia.controller;


import com.example.discordia.dto.DirectChannelDto;
import com.example.discordia.service.DirectChannel.DirectChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/direct-channel")
@RequiredArgsConstructor
@Slf4j
public class DirectChannelController {

    private final DirectChannelService directChannelService;


    @PostMapping("/create-direct-channel")
    public ResponseEntity<DirectChannelDto> createDirectChannel(
            @RequestBody DirectChannelDto directChannelDto
    ){
        DirectChannelDto dto = directChannelService.createDirectChannel(directChannelDto);
        log.info("Checking dto created: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/get-direct-channels/{userId}")
    public ResponseEntity<List<DirectChannelDto>> getDirectChannels(
            @PathVariable UUID userId
        ){
        List<DirectChannelDto> channelDtoList = directChannelService.getDirectChannels(userId);

        log.info("All direct channels: {}", channelDtoList);
        return ResponseEntity.ok(channelDtoList);
    }
}
