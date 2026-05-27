package com.example.discordia.resolvers;


import com.example.discordia.model.DirectChannel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DirectChannelResolvers {

    @PersistenceContext
    private final EntityManager entityManager;

    @ObjectFactory
    public DirectChannel resolve(UUID directChannelId, @TargetType Class<DirectChannel> type){
        return directChannelId != null ? entityManager.getReference(type, directChannelId) : null;
    }
}
