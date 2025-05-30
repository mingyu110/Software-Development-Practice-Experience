package com.example.springbootdtotutorial.exception;

/**
 * 重复资源异常
 * 当创建或更新资源时发生重复冲突时抛出此异常
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}