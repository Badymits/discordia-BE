package com.example.discordia.mappers;


import com.example.discordia.dto.UploadImageDto;
import com.example.discordia.model.DirectMessage;
import com.example.discordia.model.ServerMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UploadImageMessageMapper {


    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.displayName", target = "displayName")
    @Mapping(source = "user.imgUrl", target = "userAvatar")
    UploadImageDto directMessageToUploadImageDto(DirectMessage directMessage);


    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.displayName", target = "displayName")
    @Mapping(source = "user.imgUrl", target = "userAvatar")
    UploadImageDto serverMessageToUploadImageDto(ServerMessage serverMessage);
}
