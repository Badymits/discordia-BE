package com.example.discordia.mappers;

import com.example.discordia.dto.DirectMessageDto;
import com.example.discordia.dto.NotificationPayloadDto;
import com.example.discordia.model.DirectMessage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {UserMapper.class, DirectChannelMapper.class})
public interface DirectMessageMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.displayName", target = "displayName")
    @Mapping(source = "user.imgUrl", target = "userAvatar")
    @Mapping(source = "serverId", target = "serverId")
    @Mapping(source = "directChannelModel.directChannelId", target = "directChannelId")
    DirectMessageDto directMessageToDto(DirectMessage message);

    @Mapping(source = "userId", target = "user", qualifiedByName = "uuidToUserModel")
    @Mapping(source = "directChannelId", target= "directChannelModel")
    DirectMessage dtoToDirectMessage(DirectMessageDto dto);


    NotificationPayloadDto directMessageDtoToNotification(DirectMessageDto dto);
}
