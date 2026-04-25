package com.example.discordia.service.ServerChannel;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.dto.ServerMessageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ServerChannelService {

    ServerChannelDto createChannel(ServerChannelDto dto);
    ServerChannelDto getChannelById(UUID channelId);

}
