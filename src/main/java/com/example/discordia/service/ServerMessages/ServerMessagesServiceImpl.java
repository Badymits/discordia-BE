package com.example.discordia.service.ServerMessages;

import com.example.discordia.dto.ReplyMessageDto;
import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.model.ServerMessage;
import com.example.discordia.model.UserModel;
import com.example.discordia.repository.ServerChannelRepository;
import com.example.discordia.repository.ServerMessagesRepository;
import com.example.discordia.repository.UserRepository;
import com.example.discordia.service.Cloudinary.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServerMessagesServiceImpl implements ServerMessagesService {

    private final ServerMessagesRepository messagesRepository;
    private final ServerChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    @Async
    public ServerMessageDto createMessage(ServerMessageDto dto){

        ServerMessage message = toEntity(dto);
        if (message == null){
            throw new Error("Error Creating message");
        }
        messagesRepository.save(message);

        return toDto(message);
    }

    public UploadImageDto uploadMessageImage(UUID messageId, UUID serverId, UUID channelId,
            MultipartFile image) {

        String messageImgUrl;
        String messageFolderName = serverId + "_" + channelId + "_" + "Media_Img_Folder";

        try {
            messageImgUrl = cloudinaryService.uploadFile(
                    image,
                    messageFolderName
            );

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
            throw new Error("Cannot create cloudinary URL");
        }

        if (!messageImgUrl.trim().isEmpty()){
            messagesRepository.updateImgUrl(messageId, messageImgUrl);
        }

        return toUploadImageDto(
                messageId,
                messageImgUrl
        );
    }

    public List<ServerMessageDto> getMessagesByChannelId(UUID channelId){
        List<ServerMessage> list =
                messagesRepository.findMessagesByChannelId(channelId);

        return list.stream().map(this::toDto).toList();
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

    public ReplyMessageDto toReplyMessageDto(ServerMessage message){
        ReplyMessageDto dto = new ReplyMessageDto();


        dto.setMessageId(message.getRepliedTo().getMessageId());
        dto.setMessage(message.getRepliedTo().getMessage());

        dto.setUserId(message.getRepliedTo().getUser().getUserId());
        dto.setDisplayName(message.getRepliedTo().getUser().getDisplayName());
        dto.setImgUrl(message.getRepliedTo().getUser().getImgUrl());

        return dto;
    }

    public UploadImageDto toUploadImageDto(UUID messageId, String url){

        UploadImageDto dto = new UploadImageDto();

        dto.setMessageId(messageId);
        dto.setMessageImgUrl(url);

        return dto;
    }
}
