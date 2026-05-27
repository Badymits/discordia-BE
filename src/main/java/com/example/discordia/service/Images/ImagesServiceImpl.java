package com.example.discordia.service.Images;


import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.mappers.UploadImageMessageMapper;
import com.example.discordia.model.DirectMessage;
import com.example.discordia.model.ServerMessage;
import com.example.discordia.repository.DirectMessagesRepository;
import com.example.discordia.repository.ServerMessagesRepository;
import com.example.discordia.service.Cloudinary.CloudinaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagesServiceImpl implements ImagesService {

    private final ServerMessagesRepository serverMessagesRepository;
    private final DirectMessagesRepository directMessagesRepository;
    private final UploadImageMessageMapper imageMessageMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional
    @Override
    public UploadImageDto uploadMessageImage(
            UUID messageId,
            UUID messageTypeId, // either direct channel ID or server channel id
            String messageType,
            MultipartFile image
    ) {

        String messageImgUrl;
        String messageFolderName;

        if (Objects.equals(messageType, "server")){

            messageFolderName = messageTypeId + "_" + "ServerImage";
            try{
                messageImgUrl = cloudinaryService.uploadFile(
                        image,
                        messageFolderName
                );
            } catch (Exception e) {
                //e.printStackTrace();
                log.error("e: ", e);
                throw new RuntimeException(e);
            }

            if (!messageImgUrl.trim().isEmpty()){
                serverMessagesRepository.updateImgUrl(messageId, messageImgUrl);

                ServerMessage serverMessage =
                        serverMessagesRepository.findByMessageId(messageId)
                                .orElseThrow(() -> new EntityNotFoundException("Message does not exist"));

                //return toImageDto(existingMessage, messageType);
                return imageMessageMapper.serverMessageToUploadImageDto(serverMessage);
            }

        } else if (Objects.equals(messageType, "direct")){

            messageFolderName = messageTypeId + "_" + "DirectChannelImage";

            try{
                messageImgUrl = cloudinaryService.uploadFile(
                        image,
                        messageFolderName
                );
            } catch (Exception e) {
                log.error("e: ", e);
                throw new RuntimeException(e);
            }

            if (!messageImgUrl.trim().isEmpty()){
                directMessagesRepository.updateImgUrl(messageId, messageImgUrl);

                DirectMessage directMessage =
                        directMessagesRepository.findByMessageId(messageId)
                                .orElseThrow(() -> new EntityNotFoundException("Message Not Found"));

                return imageMessageMapper.directMessageToUploadImageDto(directMessage);
            }

        } else {
            throw new RuntimeException("Type does not exist. Please try again later");
        }

        return null;
    }

//    private UploadImageDto toImageDto(Object message, String messageType){
//
//        UploadImageDto imageDto = new UploadImageDto();
//
//        if (Objects.equals(messageType, "server")){
//
//        }
//
//        imageDto.setMessageId(message.getMessageId());
//        imageDto.setMessageImgUrl(message.getMessageImgUrl());
//        imageDto.setDisplayName(message.getUser().getDisplayName());
//        imageDto.setUserAvatar(message.getUser().getImgUrl());
//
//        imageDto.setMessage(message.getMessage());
//        imageDto.setDateTimestamp(message.getDateTimestamp());
//
//        imageDto.setIsContentWithImg(message.getIsContentWithImg());
//        imageDto.setIsReply(message.getIsReply());
//        imageDto.setIsEdited(message.getIsEdited());
//
//        if (message.getIsReply()){
//            imageDto.setRepliedTo(toReplyMessageDto(message.getRepliedTo()));
//        }
//
//        return imageDto;
//    }
}
