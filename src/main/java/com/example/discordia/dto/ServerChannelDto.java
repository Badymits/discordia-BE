package com.example.discordia.dto;


import com.example.discordia.model.ServerCategory;
import com.example.discordia.model.ServerMembers;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ServerChannelDto {

    private UUID channelId;
    private String channelName;
    private String channelTopic;
    private UUID categoryId;
    private String icon;
    private String channelType;
    private List<ServerMembers> voiceChannelMembers;
}
