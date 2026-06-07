package com.example.discordia.service.ServerMessages;

import com.example.discordia.dto.ReplyMessageDto;
import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.mappers.ServerMessageMapper;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.model.ServerMessage;
import com.example.discordia.model.UserModel;
import com.example.discordia.jparepository.JpaServerChannelRepository;
import com.example.discordia.jparepository.JpaServerMessagesRepository;
import com.example.discordia.jparepository.JpaUserRepository;
import com.example.discordia.service.Cloudinary.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ServerMessagesServiceImpl implements ServerMessagesService {

    private final JpaServerMessagesRepository messagesRepository;
    private final JpaServerChannelRepository channelRepository;
    private final JpaUserRepository userRepository;

    private final CloudinaryService cloudinaryService;

    private final ServerMessageMapper serverMessageMapper;

    @Transactional
    @Async
    @CacheEvict(
            value = "channelMessagesCache",
            key = "#dto.channelId",
            condition = "#dto.channelId != null"
    )
    public ServerMessageDto createMessage(ServerMessageDto dto){

        ServerMessage message = toEntity(dto);

        if (message == null){
            throw new Error("Error Creating message");
        }
        messagesRepository.save(message);

        return toDto(message);
    }

    @Transactional
    public UploadImageDto uploadMessageImage(
            UUID messageId,
            UUID serverId,
            UUID channelId,
            MultipartFile image
    ) {

        String messageImgUrl;
        String messageFolderName = serverId + "_" + channelId + "_" + "Media_Img_Folder";

        try {
            messageImgUrl = cloudinaryService.uploadFile(
                    image,
                    messageFolderName
            );

        } catch (Exception e){
            System.out.println(e);
            throw new Error("Cannot create cloudinary URL");
        }

        if (!messageImgUrl.trim().isEmpty()){
            messagesRepository.updateImgUrl(messageId, messageImgUrl);

            ServerMessage updatedMessage =
                    messagesRepository.findByMessageId(messageId)
                            .orElseThrow(() -> new EntityNotFoundException("Message Not Found!"));

            return toImageDto(updatedMessage);
        }

        return null;
    }

    @Cacheable(
            value = "channelMessagesCache",
            key = "#channelId",
            condition = "#channelId != null"
    )
    public ArrayList<ServerMessageDto> getMessagesByChannelId(UUID channelId){

        if (channelId == null){
            throw new EntityNotFoundException("Cannot retrieve messages with empty channelId");
        }

//        return messagesRepository.findMessagesByChannelId(channelId)
//                .stream()
//                .map(serverMessageMapper::serverMessageToDto)
//                .toList();

        return messagesRepository.findMessagesByChannelId(channelId)
                .stream()
                .map(serverMessageMapper::serverMessageToDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    @CacheEvict(
            value = "channelMessagesCache",
            key = "#channelId",

            // finds every cached page associated with this channel ID and blow them all up.
            // tho it's an aggressive approach this just ensures consistency but edits are relatively rare
            allEntries = true
    )
    public ServerMessageDto updateServerMessage(UUID messageId, UUID channelId, String message){

        if (messageId == null){
            throw new EntityNotFoundException("Empty message ID Cannot be updated!");
        }

        int updatedValue = messagesRepository.updateServerMessage(messageId, message);

        if (updatedValue == 0){
            throw new EntityNotFoundException("Error in updating message. Please try again later");
        }

        return messagesRepository.findByMessageId(messageId)
                .map(serverMessageMapper::serverMessageToDto)
                .orElseThrow(() -> new EntityNotFoundException("Message with ID not found!"));
    }

    @Transactional
    @CacheEvict(
            value = "channelMessagesCache",
            key = "#channelId",
            condition = "#channelId != null"
    )
    public void deleteServerMessage(UUID messageId, UUID channelId){
        detachParentMessageFromReplies(messageId);
        messagesRepository.deleteServerMessage(messageId);

    }

    @Transactional
    public void detachParentMessageFromReplies(UUID messageId){

        List<ServerMessage> replies = messagesRepository
                .findRepliedToMessagesByMessageId(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Reply Object not found"));

        replies.forEach(reply -> reply.setRepliedTo(null));

        messagesRepository.saveAllAndFlush(replies);
    }


    public ServerMessage toEntity(ServerMessageDto dto){

        ServerMessage message = new ServerMessage();

        ServerChannel channel = channelRepository
                .findByChannelId(dto.getChannelId())
                .orElseThrow(() -> new EntityNotFoundException("Channel Not Found"));

        log.info("Found channel: ${}", channel.toString());

        if (dto.getIsReply()){
            ServerMessage existing = messagesRepository.
                    findByMessageId(dto.getRepliedTo().getMessageId())
                    .orElseThrow(() -> new EntityNotFoundException("Message Not Found"));

            message.setRepliedTo(existing);
        }

        UserModel user = userRepository
                .findByUserId(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String url = dto.getMessageImgUrl();

        // Set it to true ahead of checking if the actual image exists.
        // This is so the frontend knows that an image is waiting to be rendered
        message.setIsContentWithImg(dto.getIsContentWithImg());


        if (url != null && !url.trim().isEmpty()){
            log.info("Image detected {}", url);
            message.setMessageImgUrl(url);
        }

        message.setServerChannel(channel);
        message.setMessage(dto.getMessage());
        message.setUser(user);
        message.setDateTimestamp(
                LocalDateTime.now()
        );
        message.setIsReply(dto.getIsReply());


        return message;

    }

    public ServerMessageDto toDto(ServerMessage message){
        ServerMessageDto dto = new ServerMessageDto();

        dto.setMessageId(message.getMessageId());
        dto.setMessage(message.getMessage());
        dto.setMessageId(message.getMessageId());

        if (message.getUser() != null){
            dto.setDisplayName(message.getUser().getDisplayName());
            dto.setUserId(message.getUser().getUserId());

            String avatar = message.getUser().getImgUrl();
            dto.setUserAvatar((avatar != null) ? avatar : "");
        }

        if (message.getServerChannel().getChannelId() != null){
            dto.setChannelId(message.getServerChannel().getChannelId());
        }

        if (message.getIsContentWithImg()){
            dto.setIsContentWithImg(true);
            dto.setMessageImgUrl(message.getMessageImgUrl());
        } else {
            dto.setIsContentWithImg(false);
        }

        // Its recursive pero hopefully....idk
        if (message.getIsReply()){
            dto.setRepliedTo(toReplyMessageDto(message));
        }

        dto.setIsReply(message.getIsReply());

        dto.setDateTimestamp(message.getDateTimestamp());

        log.info("testing if image has URL: {}", dto.getMessageImgUrl());

        return dto;
    }

    private UploadImageDto toImageDto(ServerMessage message){

        UploadImageDto imageDto = new UploadImageDto();

        imageDto.setMessageId(message.getMessageId());
        imageDto.setMessageImgUrl(message.getMessageImgUrl());
        imageDto.setDisplayName(message.getUser().getDisplayName());
        imageDto.setUserAvatar(message.getUser().getImgUrl());

        imageDto.setMessage(message.getMessage());
        imageDto.setDateTimestamp(message.getDateTimestamp());

        imageDto.setIsContentWithImg(message.getIsContentWithImg());
        imageDto.setIsReply(message.getIsReply());
        imageDto.setIsEdited(message.getIsEdited());

        imageDto.setRepliedTo(toReplyMessageDto(message.getRepliedTo()));

        return imageDto;
    }

    public ReplyMessageDto toReplyMessageDto(ServerMessage message){
        ReplyMessageDto dto = new ReplyMessageDto();


        dto.setMessageId(message.getRepliedTo().getMessageId());
        dto.setMessage(message.getRepliedTo().getMessage());

        dto.setUserId(message.getRepliedTo().getUser().getUserId());
        dto.setDisplayName(message.getRepliedTo().getUser().getDisplayName());
        dto.setImgUrl(message.getRepliedTo().getUser().getImgUrl());

        if (message.getRepliedTo().getMessageImgUrl() != null){
            dto.setIsContentWithImg(
                    message.getRepliedTo().getIsContentWithImg()
            );
        }


        return dto;
    }

}
