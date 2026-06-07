package com.example.discordia.service.ServerChannel;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.model.ServerCategory;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.jparepository.JpaServerCategoryRepository;
import com.example.discordia.jparepository.JpaServerChannelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerChannelServiceImpl implements ServerChannelService{

    private final JpaServerCategoryRepository categoryRepository;
    private final JpaServerChannelRepository channelRepository;

    @Override
    @Transactional
    @CacheEvict(
            value = "serverDetailsCache",
            key = "#serverId",
            condition = "#serverId != null"
    )
    public ServerChannelDto createChannel(ServerChannelDto dto, UUID serverId) {

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

    @Transactional
    @CacheEvict(
            value = "serverDetailsCache",
            key = "#serverId",
            condition = "#serverId != null"
    )
    public void updateChannel(
            UUID channelId,
            UUID serverId,
            ServerChannelDto channelDto
    ){

        if (channelDto.getChannelName() != null){
            channelRepository.updateChannel(
                    channelId,
                    channelDto.getChannelName(),
                    channelDto.getChannelTopic()
            );
        } else {
            throw new RuntimeException("Cannot update fields");
        }
    }
    // @Transactional annotation. The transaction ensures that when .clear() accesses the lazy list,
    // Hibernate can successfully run the SQL SELECT to load the messages into memory before clearing them
    @Transactional
    @CacheEvict(
            value = "serverDetailsCache",
            key = "#serverId",
            condition = "#serverId != null"
    )
    public void deleteChannel(UUID channelId, UUID serverId){

        ServerChannel channel = channelRepository
                .findByChannelId(channelId).orElseThrow(() -> new EntityNotFoundException("Channel Not Found"));

        channel.getServerMessages().clear();

        channelRepository.delete(channel);
    }

    // I'll keep this here just in case
    @Transactional
    public void detachChannelsFromCategory(UUID categoryId){

        List<ServerChannel> channels =
                channelRepository.findChannelsByCategoryId(categoryId)
                        .orElseThrow(() -> new EntityNotFoundException("Channel list not found"));

        channels.forEach(channelRepository::delete);

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
        channelDto.setChannelTopic(
                entity.getChannelTopic()
        );

        return channelDto;
    }
}
