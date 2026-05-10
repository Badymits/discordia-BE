package com.example.discordia.mappers;


import com.example.discordia.dto.DirectChannelDto;
import com.example.discordia.model.DirectChannel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UserMapper.class)
public interface DirectChannelMapper {

    DirectChannelMapper directChannelMapper = Mappers.getMapper(DirectChannelMapper.class);


    DirectChannelDto directChannelModelToDto(DirectChannel channel);
    DirectChannel dtoToDirectChannelModel(DirectChannelDto dto);
}
