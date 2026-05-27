package com.example.discordia.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ServerMetaDataDto {

    private UUID serverId;
    private String serverName;
    private String serverIcon;

    private LocalDateTime createdDate;
    private Integer serverMemberCount;
}
