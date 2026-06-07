package com.example.discordia.service.DirectMessages;


import com.example.discordia.dto.DirectMessageDto;
import com.example.discordia.dto.NotificationPayloadDto;
import com.example.discordia.mappers.DirectMessageMapper;
import com.example.discordia.model.DirectMessage;
import com.example.discordia.model.UserModel;
import com.example.discordia.jparepository.JpaDirectMessagesRepository;

import com.example.discordia.jparepository.JpaServerModelRepository;
import com.example.discordia.jparepository.JpaUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectMessagesServicesImpl implements DirectMessagesService {

    private final JpaUserRepository userRepository;
    private final JpaServerModelRepository serverModelRepository;
    private final JpaDirectMessagesRepository directMessagesRepository;
    private final DirectMessageMapper directMessageMapper;

    @Transactional
    @CachePut(value = "directChannelMessages", key = "#messageDto.directChannelId")
    public DirectMessageDto createMessage(DirectMessageDto messageDto){

        log.info("Testing incoming data object: {}", messageDto);

        DirectMessage message = directMessageMapper.dtoToDirectMessage(messageDto);


        UserModel existingUser =
                userRepository.findByUserId(messageDto.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User Not Found!"));


        message.setUser(existingUser);

        if (messageDto.getIsReply()){
            DirectMessage parentMessage =
                    directMessagesRepository.findByMessageId(messageDto.getRepliedTo().getMessageId())
                            .orElseThrow(() -> new EntityNotFoundException("Reply Message Not found"));

            message.setRepliedTo(parentMessage);
        }

        if (messageDto.getIsServerInvite()){

            String serverCode = getSubString(message.getMessage());

            if (serverCode == null || serverCode.isEmpty()){
                message.setIsServerInvite(false);
            }

            Optional<UUID> serverId =
                    serverModelRepository.findServerByServerCode(serverCode, LocalDateTime.now());

            serverId.ifPresentOrElse(message::setServerId, ()-> message.setIsServerInvite(false));
        }

//        int counter = message.getUnreadMessages() + 1;
//        message.setUnreadMessages(counter);
        message.setIsRead(false);

        log.info("testing mapped object: {}", message);
        directMessagesRepository.save(message);

        return directMessageMapper.directMessageToDto(message);
    }

    @Override
    @Cacheable(value = "directChannelMessages", key = "#directChannelId")
    public List<DirectMessageDto> getDirectChannelMessages(UUID directChannelId){

        if (directChannelId == null){
            throw new EntityNotFoundException("Cannot Proceed with empty ID");
        }

        return directMessagesRepository.findMessagesByChannelId(
                directChannelId
            ).stream().map(directMessageMapper::directMessageToDto).toList();
    }

    @Transactional
    @CachePut(value = "directMessageCache", key = "#messageId")
    public DirectMessageDto updateDirectMessage(UUID messageId, String message){

        if (messageId == null){
            throw new EntityNotFoundException("Cannot update message with empty ID!");
        }

        log.info("message from frontend: {}", message);

        int updatedValue = directMessagesRepository
                .updateDirectMessage(messageId, message);

        if (updatedValue == 0){
            throw new EntityNotFoundException("Cannot update message entity");
        }

        return directMessagesRepository.findByMessageId(messageId)
                .map(directMessageMapper::directMessageToDto)
                .orElseThrow(() -> new EntityNotFoundException("Message Not Found!"));
    }

    @Override
    @Transactional
    @CacheEvict(value = "messageCache", key = "#messageId")
    public UUID deleteDirectMessage(UUID messageId) {

        detachParentMessageFromReplies(messageId);

        UUID directChannelID = directMessagesRepository.findByMessageId(messageId)
                .map(message -> message.getDirectChannelModel().getDirectChannelId())
                .orElseThrow(() -> new EntityNotFoundException("Channel ID not found!"));

        directMessagesRepository.deleteDirectMessage(messageId);

        return directChannelID;
    }

    public NotificationPayloadDto getNotificationDto(DirectMessageDto dto){

        Integer totalCount = directMessagesRepository
                .getTotalCountOfUnreadMessages(dto.getDirectChannelId());

        if (totalCount == null){
            throw new EntityNotFoundException("No Count");
        }

        NotificationPayloadDto notifDto = directMessageMapper.directMessageDtoToNotification(dto);
        notifDto.setUnreadMessages(totalCount);

        return notifDto;
    }

    @Transactional
    public void detachParentMessageFromReplies(UUID messageId){

        List<DirectMessage> replies = directMessagesRepository
                .findRepliedToMessagesByMessageId(messageId)
                .orElse(Collections.emptyList());

        replies.forEach(reply -> reply.setRepliedTo(null));

        directMessagesRepository.saveAllAndFlush(replies);
    }

    // Discordia link invite to extract code coming from url in FE
    public String getSubString(String urlLink){

        if (urlLink == null || urlLink.isEmpty()){
            return urlLink;
        }

        // Remove trailing slash to avoid empty results
        String trimmedInput = urlLink.replaceAll("/+$", "");

        int lastSlashIndex = trimmedInput.lastIndexOf('/');

        if (lastSlashIndex == -1){
            return trimmedInput;
        }

        return trimmedInput.substring(lastSlashIndex + 1);
    }
}
