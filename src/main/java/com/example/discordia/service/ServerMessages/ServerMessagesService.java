package com.example.discordia.service.ServerMessages;


import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.model.ServerMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ServerMessagesService {

    @Transactional
    ServerMessageDto createMessage(ServerMessageDto messageDto);
    ServerMessageDto toDto (ServerMessage message);
    List<ServerMessageDto> getMessagesByChannelId(UUID channelId);
    UploadImageDto uploadMessageImage(
            UUID messageId,
            UUID serverId,
            UUID channelId,
            MultipartFile image
    );


}
