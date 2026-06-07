package com.example.discordia.mappers;


import com.example.discordia.dto.ServerMessageDto;
import com.example.discordia.model.ServerMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
            UserMapper.class,
            ServerChannelMapper.class
        }
    )
public interface ServerMessageMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.displayName", target = "displayName")
    @Mapping(source = "user.imgUrl", target = "userAvatar")
    @Mapping(source = "serverChannel.channelId", target = "channelId")
    ServerMessageDto serverMessageToDto(ServerMessage message);

    @Mapping(source = "userId", target = "user", qualifiedByName = "uuidToUserModel")
    @Mapping(source = "channelId", target = "serverChannel", qualifiedByName = "uuidToChannelModel")
    ServerMessage dtoToServerMessage(ServerMessageDto messageDto);
}
