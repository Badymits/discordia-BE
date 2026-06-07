package com.example.discordia.redis;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class CustomGenericSerializer<T> implements RedisSerializer<T> {

    private final JsonMapper jsonMapper;

    public CustomGenericSerializer(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }


    @Override
    public byte[] serialize(@Nullable T value) throws SerializationException {

        if (value == null){
            return new byte[0];
        }

        return this.jsonMapper.writeValueAsBytes(value);
    }

    @Override
    public @Nullable T deserialize(byte @Nullable [] bytes) throws SerializationException {

        if (bytes == null || bytes.length == 0){
            return null;
        }

        String json = new String(bytes);

        //jsonObjectToClass(json, Class);
        return null;
    }

    public T jsonObjectToClass(String json, Class<T> entityClass) {

        log.info("Data: {}", json);
        log.info("Class: {}", entityClass);

        return this.jsonMapper.readValue(json, entityClass);
    }


}
