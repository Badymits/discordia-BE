package com.example.discordia.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class ServerCategoryDto {

    private UUID categoryId;
    private UUID serverId;
    private String categoryName;
    private Boolean isRootFolder;
    private ArrayList<ServerChannelDto> categoryChannels;
}
