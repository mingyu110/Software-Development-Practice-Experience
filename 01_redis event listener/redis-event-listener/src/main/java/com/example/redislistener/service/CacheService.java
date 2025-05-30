package com.example.redislistener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CacheService {
    
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private final StringRedisTemplate redisTemplate;
    
    public CacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 设置带过期时间的缓存
     * 
     * @param key 键名
     * @param value 值
     * @param expiryTimeInSeconds 过期时间(秒)
     */
    public void setWithExpiry(String key, String value, long expiryTimeInSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expiryTimeInSeconds));
        logger.info("Set key: {} with expiry: {} seconds", key, expiryTimeInSeconds);
    }
    
    /**
     * 删除缓存
     * 
     * @param key 键名
     * @return 是否成功删除
     */
    public boolean delete(String key) {
        boolean result = Boolean.TRUE.equals(redisTemplate.delete(key));
        if (result) {
            logger.info("Deleted key: {}", key);
        } else {
            logger.warn("Failed to delete key: {}", key);
        }
        return result;
    }
    
    /**
     * 获取缓存
     * 
     * @param key 键名
     * @return 缓存值
     */
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            logger.debug("Retrieved value for key: {}", key);
        } else {
            logger.debug("No value found for key: {}", key);
        }
        return value;
    }
} 