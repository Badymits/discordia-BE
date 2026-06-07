package com.example.discordia.mappers;

import com.example.discordia.dto.ServerCategoryDto;
import com.example.discordia.model.ServerCategory;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        uses = {
            ServerChannelMapper.class
        }
    )
public interface ServerCategoryMapper {

    ServerCategoryDto categoryModelToDto(UUID categoryId);
    ServerCategory dtoToCategory(ServerCategoryDto dto);

    @Named("uuidToCategoryModel")
    ServerCategory map(UUID categoryId);
    UUID map(ServerCategory category);

}
