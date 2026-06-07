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
            @RequestBody DirectChannelDto directChannelDto,
            @RequestBody UUID userId
    ){
        DirectChannelDto dto =
                directChannelService.createDirectChannel(directChannelDto, userId);
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

    @GetMapping("/get-direct-channel/{directChannelId}")
    public ResponseEntity<DirectChannelDto> getDirectChannelRoom(
            @PathVariable UUID directChannelId
    ){
        DirectChannelDto channelDto = directChannelService.getDirectChannel(directChannelId);

        log.info("Channel Found! {}", channelDto);
        return ResponseEntity.ok(channelDto);
    }

    @PatchMapping("/update-read-messages/{directChannelId}")
    public void updateReadMessagesInChannel(@PathVariable UUID directChannelId){
        directChannelService.updateReadMessagesInChannel(directChannelId);
    }
}
