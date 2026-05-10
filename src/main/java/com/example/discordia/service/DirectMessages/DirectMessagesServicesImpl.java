package com.example.discordia.service.DirectMessages;


import com.example.discordia.dto.DirectMessageDto;
import com.example.discordia.model.DirectMessage;
import com.example.discordia.repository.DirectMessagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectMessagesServicesImpl implements DirectMessagesService {


    private final DirectMessagesRepository directMessagesRepository;

    @Transactional
    @Async
    public void createMessage(DirectMessageDto messageDto){

        DirectMessage message = new DirectMessage();

        //message.
    }
}
