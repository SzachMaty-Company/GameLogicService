package com.szachmaty.gamelogicservice.infrastructure.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Bean
    public JedisConnectionFactory connectionFactory() {
        var config = new RedisStandaloneConfiguration();
//        config.setUsername("admin");
//        config.setPassword("qwerty");
        config.setHostName("localhost");
        config.setPort(6379);
        return new JedisConnectionFactory(config);
    }
    @Bean
    public RedisTemplate<String, String> template() {
        var template = new RedisTemplate<String, String>();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

}
