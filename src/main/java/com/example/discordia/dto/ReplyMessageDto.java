package com.example.discordia.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class ReplyMessageDto {

    private UUID messageId;
    private String message;

    private UUID userId;
    private String displayName;
    private String imgUrl;
}
