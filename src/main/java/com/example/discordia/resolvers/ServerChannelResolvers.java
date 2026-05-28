package com.example.discordia.resolvers;

import com.example.discordia.model.ServerChannel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServerChannelResolvers {

    @PersistenceContext
    private final EntityManager entityManager;

    public ServerChannel resolve(UUID channelId, @TargetType Class<ServerChannel> type){
        return channelId != null ? entityManager.getReference(type, channelId) : null;
    }
}
