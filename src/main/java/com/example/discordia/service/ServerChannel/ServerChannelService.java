package com.example.discordia.service.ServerChannel;


import com.example.discordia.dto.ServerChannelDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ServerChannelService {

    ServerChannelDto createChannel(ServerChannelDto dto);
    ServerChannelDto getChannelById(UUID channelId);
    void updateChannel(UUID channelId, ServerChannelDto channelDto);
    void deleteChannel(UUID channelId);
}
