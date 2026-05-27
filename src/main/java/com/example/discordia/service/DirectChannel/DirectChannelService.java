package com.example.discordia.service.DirectChannel;


import com.example.discordia.dto.DirectChannelDto;

import java.util.List;
import java.util.UUID;

public interface DirectChannelService {

    DirectChannelDto createDirectChannel(DirectChannelDto dto);
    DirectChannelDto getDirectChannel(UUID directChannelId);
    List<DirectChannelDto> getDirectChannels(UUID userId);
}
