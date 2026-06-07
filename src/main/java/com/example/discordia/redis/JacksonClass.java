package com.example.discordia.redis;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class JacksonClass {

    @Bean(name = "redisJsonMapper")
    public RedisSerializer<Object> redisPayloadSerializer(){
        JsonMapper mapper = JsonMapper.builder()
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator
                                .builder()
                                .allowIfSubType("com.example.discordia.dto")
                                // this is to allow data structures to be serialized in redis
                                .allowIfSubType("java.util")
                                .build(),
                        DefaultTyping.NON_FINAL
                )
                .build();
        return new GenericJacksonJsonRedisSerializer(mapper);
    }
}
