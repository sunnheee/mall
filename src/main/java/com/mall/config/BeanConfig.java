package com.mall.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BeanConfig {

    //配置自定义的bean

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        //设置连接工厂
        redisTemplate.setConnectionFactory(factory);

        //创建序列化对象
        StringRedisSerializer srs = new StringRedisSerializer();
        //设置key的序列化方式
        redisTemplate.setKeySerializer(srs);

        //创建jackson序列化方式
        Jackson2JsonRedisSerializer json = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        json.setObjectMapper(om);

        //设置值的序列化方式
        redisTemplate.setValueSerializer(json);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
