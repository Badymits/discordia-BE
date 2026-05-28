package com.example.discordia.mappers;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.resolvers.ServerChannelResolvers;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring", uses={ServerChannelResolvers.class})
public interface ServerChannelMapper {


    ServerChannel dtoToServerChannel(ServerChannelDto dto);

    ServerChannel map(UUID channelId);
    UUID map(ServerChannel channel);
}
