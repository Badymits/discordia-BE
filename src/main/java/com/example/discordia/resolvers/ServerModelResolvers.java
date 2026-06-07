package com.example.discordia.resolvers;


import com.example.discordia.model.ServerModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServerModelResolvers {

    @PersistenceContext
    private final EntityManager entityManager;

    @ObjectFactory
    public ServerModel resolve(UUID serverId, @TargetType Class<ServerModel> targetType){
        return serverId != null ? entityManager.getReference(targetType, serverId) : null;
    }
}
