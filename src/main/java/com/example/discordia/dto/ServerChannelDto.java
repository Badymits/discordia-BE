package com.example.discordia.dto;


import com.example.discordia.model.ServerMembers;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class ServerChannelDto {

    private UUID channelId;
    private UUID categoryId;

    private String channelName;
    private String channelTopic;
    private String icon;
    private String channelType;

    private ArrayList<ServerMembers> voiceChannelMembers;
}
