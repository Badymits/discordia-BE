package com.example.discordia.service.DirectMessages;


import com.example.discordia.dto.DirectMessageDto;
import com.example.discordia.dto.NotificationPayloadDto;

import java.util.List;
import java.util.UUID;

public interface DirectMessagesService {

    DirectMessageDto createMessage(DirectMessageDto message);
    List<DirectMessageDto> getDirectChannelMessages(UUID directChannelId);
    DirectMessageDto updateDirectMessage(UUID messageId, String message);
    UUID deleteDirectMessage(UUID messageId);

    NotificationPayloadDto getNotificationDto(DirectMessageDto dto);

}
