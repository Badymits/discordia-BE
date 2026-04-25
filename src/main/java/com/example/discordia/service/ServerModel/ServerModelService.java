package com.example.discordia.service.ServerModel;


import com.example.discordia.dto.ServerModelDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ServerModelService {

    ServerModelDto findByServerID(UUID id);

    ServerModelDto createServer(
            ServerModelDto createServerRequest
    );

    List<ServerModelDto> getServersByUserId(UUID userId, String username);

}
