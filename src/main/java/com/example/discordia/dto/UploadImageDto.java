package com.example.discordia.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

// generated methods will include the fields from the parent (super) class in their calculations
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadImageDto extends MessageDto {

    private UUID messageId;
    private UUID userId;
    private String messageImgUrl;
}
