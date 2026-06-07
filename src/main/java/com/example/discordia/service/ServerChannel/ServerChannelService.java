package com.example.discordia.service.ServerChannel;


import com.example.discordia.dto.ServerChannelDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ServerChannelService {

    ServerChannelDto createChannel(ServerChannelDto dto, UUID serverId);
    ServerChannelDto getChannelById(UUID channelId);
    void updateChannel(UUID channelId, UUID serverId, ServerChannelDto channelDto);
    void deleteChannel(UUID channelId, UUID serverId);

    void detachChannelsFromCategory(UUID categoryId);
}
