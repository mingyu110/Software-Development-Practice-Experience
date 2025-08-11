package com.example.qrcodegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QRCodeGeneratorApplication {

    public static void main(String[] args) {
        // Enable virtual threads
        System.setProperty("spring.threads.virtual.enabled", "true");
        SpringApplication.run(QRCodeGeneratorApplication.class, args);
    }
}
