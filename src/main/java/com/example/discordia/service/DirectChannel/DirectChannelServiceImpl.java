package com.example.discordia.service.DirectChannel;


import com.example.discordia.dto.DirectChannelDto;
import com.example.discordia.mappers.DirectChannelMapper;
import com.example.discordia.model.DirectChannel;
import com.example.discordia.model.DirectMessage;
import com.example.discordia.model.UserModel;
import com.example.discordia.jparepository.JpaDirectChannelRepository;
import com.example.discordia.jparepository.JpaDirectMessagesRepository;
import com.example.discordia.jparepository.JpaUserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectChannelServiceImpl implements DirectChannelService {

    private final JpaDirectChannelRepository directChannelRepository;
    private final JpaDirectMessagesRepository directMessagesRepository;

    private final DirectChannelMapper directChannelMapper;
    private final JpaUserRepository userRepository;


    @Transactional
    @CacheEvict(
            value = "directChannelsCache",
            key = "#userId",
            condition = "#userId != null"
    )
    public DirectChannelDto createDirectChannel(DirectChannelDto dto, UUID userId){

        if (dto.getDirectChannelParticipants().isEmpty()){
            throw new EntityNotFoundException("Cannot proceed with empty room");
        }

        log.info(dto.getDirectChannelParticipants().toString());

        List<UserModel> participants = dto
                .getDirectChannelParticipants()
                .stream()
                .map(user ->
                        userRepository.
                                findByUserId(user.getUserId())
                                .orElseThrow(() -> new EntityNotFoundException("Error"))
                )
                .toList();

        UserModel user1 = participants.getFirst();
        UserModel user2 = participants.getLast();

        DirectChannel directChannel = directChannelRepository
                .findByDirectChannelParticipants(user1, user2)
                .orElse(
                        directChannelMapper.dtoToDirectChannelModel(dto)
                );

        if (directChannel.getDirectChannelId() == null){
            participants.forEach(
                    userModel -> userModel.setDirectChannel(directChannel)
            );
            directChannel.setDirectChannelParticipants(participants);
            directChannelRepository.saveAndFlush(directChannel);

        }

        return directChannelMapper.directChannelModelToDto(directChannel);

    }

    @Cacheable(value = "directChannelsCache", key = "{#a0}")
    public ArrayList<DirectChannelDto> getDirectChannels(UUID userId){

        UserModel existingUser = userRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        return directChannelRepository
                .findDirectChannelsByUserId(existingUser)
                .stream()
                .sorted(Comparator.comparing(DirectChannel::getChannelCreated))
                .map(directChannelMapper::directChannelModelToDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Since the channel participants are marked as Lazy Loading
    // this ensures that the mapper won't throw a LazyInitializationException
    // if it tries to access the list outside the transaction
    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "directChannelDetailsCache", key = "#directChannelId")
    public DirectChannelDto getDirectChannel(UUID directChannelId) {
        return
                directChannelMapper.directChannelModelToDto(
                    directChannelRepository
                    .findByDirectChannelId(directChannelId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Channel not Found!"))
                );
    }

    @Transactional
    @CachePut(
            value = "directChannelDetailsCache",
            key = "{#a0}",
            condition = "#a0 != null"
    )
    public void updateReadMessagesInChannel(UUID channelId){

        List<DirectMessage> unreadMessages =
                directMessagesRepository.findUnreadMessagesByChannelId(channelId)
                .orElse(Collections.emptyList());

        unreadMessages.forEach(message -> message.setIsRead(true));

        directMessagesRepository.saveAllAndFlush(unreadMessages);
    }

}
