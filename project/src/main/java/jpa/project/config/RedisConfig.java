package jpa.project.config;

import jpa.project.cache.CacheKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableCaching
@Configuration
public class RedisConfig {
    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.host}")
    private String host;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null value 캐시안함
                .entryTtl(Duration.ofSeconds(CacheKey.DEFAULT_EXPIRE_SEC)) // 캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())); // redis 캐시 데이터 저장방식을 StringSeriallizer로 지정

// 캐시키별 default 유효시간 설정
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(CacheKey.Member, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.USER_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.BOARD, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.BOARD_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.POST, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.POST_EXPIRE_SEC)));
        cacheConfigurations.put(CacheKey.POSTS, RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(CacheKey.POST_EXPIRE_SEC)));

        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory).cacheDefaults(configuration)
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }
}