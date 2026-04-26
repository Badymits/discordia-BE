package com.example.discordia.dto;


import com.example.discordia.model.ServerCategory;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.model.ServerMembers;
import com.example.discordia.model.UserModel;
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

    private List<ServerMembers> serverMembers;
    private List<ServerCategoryDto> serverCategories;
}
