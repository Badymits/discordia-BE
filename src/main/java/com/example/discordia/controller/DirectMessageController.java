package com.example.discordia.controller;


import com.example.discordia.dto.DirectMessageDto;
import com.example.discordia.dto.NotificationPayloadDto;
import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.service.DirectMessages.DirectMessagesService;
import com.example.discordia.service.Images.ImagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/direct-messages")
@RequiredArgsConstructor
@Slf4j
public class DirectMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DirectMessagesService directMessagesService;
    private final ImagesService imagesService;

    @MessageMapping("/sendDirectMessage")
    @Async
    public void sendDirectMessage(
            @Payload DirectMessageDto messageDto
            //@RequestBody Boolean isRecipientActive
    ){
        // sends message to the direct Channel room
        DirectMessageDto message = directMessagesService.createMessage(messageDto);
        String destination = "/topic/" + message.getDirectChannelId();

        messagingTemplate.convertAndSend(destination, message);

        // responsible for sending notification to user when they send message to the recipient
        // it will show up on top of the server list on the left
        String notificationUrl = "/direct/" + message.getRecipientId();
        NotificationPayloadDto notifDto = directMessagesService.getNotificationDto(message);

        log.info("The notif DTO: {}", notifDto);
        log.info("Notification url: {}", notificationUrl);
        messagingTemplate.convertAndSend(notificationUrl, notifDto);
    }

    @GetMapping("/get-direct-messages/{directChannelId}")
    public ResponseEntity<List<DirectMessageDto>> getDirectChannelMessages(
            @PathVariable UUID directChannelId
    ){
        log.info("Connection alright");
        List<DirectMessageDto> channelMessages =
                directMessagesService.getDirectChannelMessages(directChannelId);
        log.info("Messages found: {}", channelMessages);
        return ResponseEntity.ok(channelMessages);
    }

    @PatchMapping("/update-direct-message/{messageId}")
    public ResponseEntity<DirectMessageDto> updateMessage(
            @PathVariable UUID messageId,
            @RequestBody String message
    ){

        DirectMessageDto updatedMessage = directMessagesService.updateDirectMessage(messageId, message);

        String destination = "/topic/" + updatedMessage.getDirectChannelId();
        messagingTemplate.convertAndSend(destination, updatedMessage);
        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("{directChannelId}/{messageId}")
    public void deleteMessage(
            @PathVariable UUID messageId,
            @PathVariable UUID directChannelId
    ){
        directMessagesService.deleteDirectMessage(messageId);

        String destination = "/topic/" + directChannelId;
        messagingTemplate.convertAndSend(destination, messageId);
    }

    @PostMapping("/upload-image/{messageId}")
    public ResponseEntity<UploadImageDto> uploadDirectMessageImage(
            @PathVariable UUID messageId,
            @RequestPart("directChannelId") UUID directChannelId,
            @RequestPart(value = "image") MultipartFile image
            ){
        log.info("data: {}", directChannelId);
        log.info("checking image: {}", image);
        UploadImageDto imageDto =
                imagesService.uploadMessageImage(messageId, directChannelId, "direct", image);

        String destination = "/topic/" + directChannelId;

        messagingTemplate.convertAndSend(destination, imageDto);
        return ResponseEntity.ok(imageDto);
    }
}
