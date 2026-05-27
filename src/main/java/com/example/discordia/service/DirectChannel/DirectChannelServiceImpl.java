package com.example.discordia.service.DirectChannel;


import com.example.discordia.dto.DirectChannelDto;
import com.example.discordia.mappers.DirectChannelMapper;
import com.example.discordia.model.DirectChannel;
import com.example.discordia.model.UserModel;
import com.example.discordia.repository.DirectChannelRepository;
import com.example.discordia.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectChannelServiceImpl implements DirectChannelService {

    private final DirectChannelRepository directChannelRepository;
    private final DirectChannelMapper directChannelMapper;
    private final UserRepository userRepository;


    @Transactional
    public DirectChannelDto createDirectChannel(DirectChannelDto dto){

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


    public List<DirectChannelDto> getDirectChannels(UUID userId){

        UserModel existingUser = userRepository
                .findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        return directChannelRepository
                .findDirectChannelsByUserId(existingUser)
                .stream()
                .sorted(Comparator.comparing(DirectChannel::getChannelCreated))
                .map(directChannelMapper::directChannelModelToDto)
                .toList();
    }

    // Since the channel participants are marked as Lazy Loading
    // this ensures that the mapper won't throw a LazyInitializationException
    // if it tries to access the list outside the transaction
    @Transactional(readOnly = true)
    @Override
    public DirectChannelDto getDirectChannel(UUID directChannelId) {
        return
                directChannelMapper.directChannelModelToDto(
                    directChannelRepository
                    .findByDirectChannelId(directChannelId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Channel not Found!"))
                );
    }

}
