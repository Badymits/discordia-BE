package com.example.discordia.dto;

import com.example.discordia.model.ServerChannel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ServerCategoryDto {

    private UUID categoryId;
    private UUID serverId;
    private String categoryName;
    private List<ServerChannelDto> categoryChannels;
}
