package com.example.discordia.controller;


import com.example.discordia.service.DirectMessages.DirectMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/direct-messages")
@RequiredArgsConstructor
@Slf4j
public class DirectMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DirectMessagesService directMessagesService;
}
