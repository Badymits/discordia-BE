package com.example.discordia.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServerMessageDto extends MessageDto {

    private UUID channelId;
    private UUID userId;
    private UUID serverId;

}
