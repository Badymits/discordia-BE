package com.example.discordia.service.ServerMessages;


import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.model.ServerMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.UUID;

@Service
public interface ServerMessagesService {

    @Transactional
    ServerMessageDto createMessage(ServerMessageDto messageDto);
    ServerMessageDto toDto (ServerMessage message);
    ArrayList<ServerMessageDto> getMessagesByChannelId(UUID channelId);
    ServerMessageDto updateServerMessage(UUID messageId, UUID channelId, String message);
    void deleteServerMessage(UUID messageId, UUID channelId);


}
