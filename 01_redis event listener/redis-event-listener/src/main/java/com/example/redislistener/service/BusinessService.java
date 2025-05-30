package com.example.redislistener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    
    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    
    /**
     * 处理过期的key的业务逻辑
     * @param key 过期的键名
     */
    public void handleExpiredKey(String key) {
        // 实现您的业务逻辑
        logger.info("Business logic triggered for expired key: {}", key);
        
        // 例如：可以在这里触发邮件通知、数据同步或其他业务流程
        if (key.startsWith("user:")) {
            // 处理用户相关的过期事件
            String userId = key.substring(5);
            logger.info("User data expired for userId: {}", userId);
        } else if (key.startsWith("order:")) {
            // 处理订单相关的过期事件
            String orderId = key.substring(6);
            logger.info("Order data expired for orderId: {}", orderId);
        }
    }
} 