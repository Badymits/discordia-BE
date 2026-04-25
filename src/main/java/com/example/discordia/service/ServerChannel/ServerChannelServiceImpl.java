package com.example.discordia.service.ServerChannel;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.model.ServerCategory;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.model.ServerMessage;
import com.example.discordia.repository.ServerCategoryRepository;
import com.example.discordia.repository.ServerChannelRepository;
import com.example.discordia.repository.ServerMessagesRepository;
import com.example.discordia.service.ServerMessages.ServerMessagesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerChannelServiceImpl implements ServerChannelService{

    private final ServerCategoryRepository categoryRepository;
    private final ServerChannelRepository channelRepository;

    @Override
    public ServerChannelDto createChannel(ServerChannelDto dto) {

        ServerChannel channel = new ServerChannel();
        ServerCategory existingCategory =
                categoryRepository.findByCategoryId(dto.getCategoryId());


        channel.setChannelName(dto.getChannelName());
        channel.setServerCategory(existingCategory);
        channel.setServerModel(existingCategory.getServerModel());
        channel.setIcon(dto.getIcon());
        channel.setDateCreated(
                LocalDateTime.now()
        );
        existingCategory.getCategoryChannels().add(channel);

        channelRepository.save(channel);

        return toChannelDto(channel);
    }


    public ServerChannelDto getChannelById(UUID channelId){

        ServerChannel channel = channelRepository.findByChannelId(channelId)
                        .orElseThrow(() -> new EntityNotFoundException("Channel Not Found"));

        return toChannelDto(channel);
    }

    public ServerChannelDto toChannelDto(ServerChannel entity){

        ServerChannelDto channelDto = new ServerChannelDto();

        channelDto.setChannelId(entity.getChannelId());
        channelDto.setChannelName(entity.getChannelName());
        channelDto.setCategoryId(
                entity.getServerCategory().getCategoryId()
        );
        channelDto.setIcon(
                entity.getIcon()
        );
        // same values
        channelDto.setChannelType(
                entity.getIcon()
        );

        return channelDto;
    }
}
