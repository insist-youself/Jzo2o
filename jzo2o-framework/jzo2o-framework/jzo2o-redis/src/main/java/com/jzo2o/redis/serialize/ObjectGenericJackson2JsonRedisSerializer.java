package com.jzo2o.redis.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Objects;

public class ObjectGenericJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {
    public static final ObjectGenericJackson2JsonRedisSerializer DEFAULT_INSTANCE = new ObjectGenericJackson2JsonRedisSerializer(new ObjectMapper());

    public ObjectGenericJackson2JsonRedisSerializer(ObjectMapper mapper){
        super(mapper);
    }
    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (Objects.nonNull(source)) {
            if (source instanceof String || source instanceof Character) {
                return source.toString().getBytes();
            }
        }
        return super.serialize(source);
    }
    @Override
    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {
        return super.deserialize(source, type);
    }

}
