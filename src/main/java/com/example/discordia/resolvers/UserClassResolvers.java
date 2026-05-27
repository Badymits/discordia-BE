package com.example.discordia.resolvers;


import com.example.discordia.model.UserModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserClassResolvers {

    @PersistenceContext
    private final EntityManager entityManager;

    @ObjectFactory
    public UserModel resolve(UUID userId, @TargetType Class<UserModel> type){
        return userId != null ? entityManager.getReference(type, userId) : null;

    }
}
