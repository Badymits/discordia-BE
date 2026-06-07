package com.example.discordia.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class DirectChannelDto {

    private UUID directChannelId;
    private List<UserDto> directChannelParticipants;
    private LocalDateTime channelCreated;

    private Boolean isRead;
}
