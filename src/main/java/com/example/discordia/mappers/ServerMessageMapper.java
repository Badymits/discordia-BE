package com.example.discordia.mappers;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ServerMessageMapper {


}
