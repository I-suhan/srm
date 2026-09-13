package com.ne.srm.core.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.ne.srm.core.properties.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {


    @Bean
    public RedisTemplate<String,Object> redisTemplate(
            RedisConnectionFactory factory,
            ObjectMapper objectMapper,
            RedisProperties properties){


        RedisTemplate<String,Object> template = new RedisTemplate<>();


        template.setConnectionFactory(factory);


        /*
         * 使用 Spring Boot 注入的 ObjectMapper 副本，
         * 保留 JavaTimeModule 等全局 Jackson 配置。
         */
        ObjectMapper mapper = objectMapper.copy();

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        BasicPolymorphicTypeValidator.Builder builder = BasicPolymorphicTypeValidator.builder();
        // Redis JSON 反序列化白名单
        properties.getTrustedPackages().forEach(builder::allowIfBaseType);

        builder.allowIfSubType("com.ne.srm");
        builder.allowIfSubType("java.util");
        builder.allowIfSubType("org.springframework.security");


        PolymorphicTypeValidator validator = builder.build();

        mapper.activateDefaultTyping(
                validator,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );


        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(mapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();

        return template;
    }
}