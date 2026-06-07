package com.example.discordia.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class NotificationPayloadDto {

    private UUID userId;
    private UUID directChannelId;

    private String displayName;
    private String userAvatar;

    private Integer unreadMessages;
}
