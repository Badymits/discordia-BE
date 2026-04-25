package com.example.discordia.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class UploadImageDto {

    private UUID messageId;
    private String messageImgUrl;
}
