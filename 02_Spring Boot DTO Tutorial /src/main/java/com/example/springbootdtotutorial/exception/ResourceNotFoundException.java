package com.example.springbootdtotutorial.exception;

/**
 * 资源未找到异常
 * 当请求的资源不存在时抛出此异常
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 