package com.example.redislistener.listener;

import com.example.redislistener.service.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener implements MessageListener {
    
    private static final Logger logger = LoggerFactory.getLogger(RedisKeyExpirationListener.class);
    private final BusinessService businessService;
    
    public RedisKeyExpirationListener(BusinessService businessService) {
        this.businessService = businessService;
    }
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        logger.info("Key expired: {}", expiredKey);
        
        // 根据过期的key调用业务逻辑
        businessService.handleExpiredKey(expiredKey);
    }
} 