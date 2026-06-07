package com.example.discordia.service.DirectChannel;


import com.example.discordia.dto.DirectChannelDto;

import java.util.ArrayList;
import java.util.UUID;

public interface DirectChannelService {

    DirectChannelDto createDirectChannel(DirectChannelDto dto, UUID userId);
    DirectChannelDto getDirectChannel(UUID directChannelId);
    ArrayList<DirectChannelDto> getDirectChannels(UUID userId);

    void updateReadMessagesInChannel(UUID channelId);
}
