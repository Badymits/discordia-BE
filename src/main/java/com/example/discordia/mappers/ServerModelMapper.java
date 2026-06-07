package com.example.discordia.mappers;


import com.example.discordia.dto.ServerModelDto;
import com.example.discordia.model.ServerModel;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE, // Forces clean copies
        uses = {
            ServerChannelMapper.class,
            ServerCategoryMapper.class,
            UserMapper.class
        }
)
public interface ServerModelMapper {

    ServerModelDto modelServerToDto(ServerModel model);

    @Mapping(source = "userId", target = "serverOwner", qualifiedByName = "uuidToUserModel")
    ServerModel dtoToServerModel(ServerModelDto dto);

    @Named("uuidToServerModel")
    ServerModel map(UUID serverID);
    UUID map(ServerModel model);
}
