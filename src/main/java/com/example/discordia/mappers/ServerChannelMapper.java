package com.example.discordia.mappers;


import com.example.discordia.dto.ServerChannelDto;
import com.example.discordia.model.ServerChannel;
import com.example.discordia.resolvers.ServerChannelResolvers;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        uses={
                ServerChannelResolvers.class
        }
)
public interface ServerChannelMapper {


    ServerChannel dtoToServerChannel(ServerChannelDto dto);

    @Named("uuidToChannelModel")
    ServerChannel map(UUID channelId);
    UUID map(ServerChannel channel);
}
