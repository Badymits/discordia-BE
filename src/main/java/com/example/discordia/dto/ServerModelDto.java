package com.example.discordia.dto;

import java.util.UUID;
import lombok.Data;

import java.util.List;

@Data
public class ServerModelDto {

    private UUID serverId;
    private UUID userId;
    private String serverOwner;
    private String serverName;
    private String serverIcon;
    private String serverDescription;
    private String serverInviteCode;

    private List<ServerMemberDto> serverMembers;
    private List<ServerCategoryDto> serverCategories;
}
