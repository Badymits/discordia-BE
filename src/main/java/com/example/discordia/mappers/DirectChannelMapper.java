package com.example.discordia.mappers;


import com.example.discordia.dto.DirectChannelDto;
import com.example.discordia.model.DirectChannel;
import com.example.discordia.resolvers.DirectChannelResolvers;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class,
                DirectChannelResolvers.class
        }
)
public interface DirectChannelMapper {

    DirectChannelDto directChannelModelToDto(DirectChannel channel);
    DirectChannel dtoToDirectChannelModel(DirectChannelDto dto);

    DirectChannel map(UUID directChannelId);
    UUID map(DirectChannel channel);

}
