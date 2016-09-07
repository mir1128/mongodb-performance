package com.loobo;

import com.loobo.model.Book;
import com.loobo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@EnableCaching
@SpringBootApplication
public class SpringBootMongodbApplication {

    @Autowired
    private BookRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMongodbApplication.class, args);
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setExposeConnection(true);
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        redisCacheManager.setTransactionAware(true);
        redisCacheManager.setLoadRemoteCachesOnStartup(true);
        redisCacheManager.setUsePrefix(true);
        return redisCacheManager;
    }


}
