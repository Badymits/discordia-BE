package com.example.discordia.service.ServerModel;


import com.example.discordia.dto.ServerMetaDataDto;
import com.example.discordia.dto.ServerModelDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.UUID;

@Service
public interface ServerModelService {

    ServerModelDto findByServerID(UUID id);

    ServerModelDto createServer(ServerModelDto createServerRequest, MultipartFile image);
    String updateServer(UUID serverId, ServerModelDto serverDto, MultipartFile image);
    ArrayList<ServerModelDto> getServersByUserId(UUID userId, String username);

    String getServerCode(UUID serverId);
    ServerMetaDataDto getServerMetaData(UUID serverId);


}
