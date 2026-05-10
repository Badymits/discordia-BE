package com.example.discordia.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class DirectMessageDto extends MessageDto {

    private UUID directMessageId;
    private UserDto recipient;


}
