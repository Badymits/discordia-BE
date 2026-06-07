package com.example.discordia.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;


import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisCacheConfig {

    private final ObjectMapper objectMapper;


    @Bean
    public LettuceConnectionFactory redisConnection(){
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(RedisSerializer<Object> redisPayloadSerializer){

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer((new StringRedisSerializer()))
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(
                                        redisPayloadSerializer
                                )
                )
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory, // Best practice: inject this too
            RedisCacheConfiguration cacheConfiguration
    ){
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .transactionAware()
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            LettuceConnectionFactory factory,
            RedisSerializer<Object> redisPayloadSerializer
    ) throws IOException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());


        template.setValueSerializer(redisPayloadSerializer);
        template.setHashValueSerializer(redisPayloadSerializer);

        template.afterPropertiesSet();

        return template;
    }

    public static <T> List<T> jsonArrayToList(String json, Class<T> entityClass) throws IOException {
        ObjectMapper classObjectMapper = new ObjectMapper();

        CollectionType listType =
                classObjectMapper.getTypeFactory().constructCollectionType(ArrayList.class, entityClass);

        return classObjectMapper.readValue(json, listType);
    }



}
