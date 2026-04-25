package com.example.discordia.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ServerMessageDto {

    private String message;
    private String displayName;
    private String userAvatar;
    private String messageImgUrl;

    private Boolean isContentWithImg;
    private Boolean isReply;
    private LocalDateTime dateTimestamp;

    private UUID channelId;
    private UUID userId;
    private UUID messageId;
    private UUID serverId;

}
