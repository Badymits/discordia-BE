package com.example.discordia.controller;

import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.service.Images.ImagesService;
import com.example.discordia.service.ServerMessages.ServerMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/server-messages")
@RequiredArgsConstructor
@Slf4j
public class ServerMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ServerMessagesService serverMessagesService;
    private final ImagesService imagesService;

    @MessageMapping("/sendMessage") // Endpoint matching the JavaScript destination
    public void sendMessage(@Payload ServerMessageDto message){
        System.out.println("Testing message: " + message.getMessage());

        // Broadcast to subscribers of this topic
        String destination = "/topic/" + message.getServerId() + "/" + message.getChannelId();
        System.out.println("Destination: " + destination);
        System.out.println("Message object: " + message);

        //messagesService.createMessage(message);
        ServerMessageDto messageDto = serverMessagesService.createMessage(message);
        messagingTemplate.convertAndSend(destination, messageDto);
    }

    @GetMapping("{channelId}")
    public ResponseEntity<List<ServerMessageDto>> getMessagesByChannelId(
            @PathVariable UUID channelId){
        List<ServerMessageDto> list = serverMessagesService.getMessagesByChannelId(channelId);

        return ResponseEntity.ok(list);
    }

    // returns Img URL string converted by cloudinary service
    @PostMapping("/upload-image/{messageId}")
    public ResponseEntity<UploadImageDto> uploadMessageImage(
        @PathVariable UUID messageId,
        @RequestPart("channelId") UUID channelId,
        @RequestPart(value = "image", required = false) MultipartFile image
        ){

        UploadImageDto dto = imagesService.uploadMessageImage(messageId, channelId, "server", image);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/update-server-message/{messageId}")
    public ResponseEntity<ServerMessageDto> updateServerMessage(
            @PathVariable UUID messageId,
            @RequestParam UUID channelId,
            @RequestBody String message
    ){
        ServerMessageDto dto = serverMessagesService.updateServerMessage(messageId, channelId, message);

        String destination = "/topic/" + dto.getServerId() + "/" + dto.getChannelId();
        messagingTemplate.convertAndSend(destination, dto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{serverId}/{channelId}/{messageId}")
    public void deleteServerMessage(
        @PathVariable UUID messageId,
        @PathVariable UUID channelId,
        @PathVariable UUID serverId
    ){

        serverMessagesService.deleteServerMessage(messageId, channelId);

        String destination = "/topic/" + serverId + channelId;
        messagingTemplate.convertAndSend(destination, messageId);
    }


}
