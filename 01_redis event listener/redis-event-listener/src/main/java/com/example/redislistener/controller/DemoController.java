package com.example.redislistener.controller;

import com.example.redislistener.service.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {
    
    private final CacheService cacheService;
    
    public DemoController(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    
    @PostMapping("/cache")
    public ResponseEntity<String> cacheData(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam long expirySeconds) {
        
        cacheService.setWithExpiry(key, value, expirySeconds);
        return ResponseEntity.ok("Data cached with key: " + key);
    }
    
    @GetMapping("/cache/{key}")
    public ResponseEntity<String> getCachedData(@PathVariable String key) {
        String value = cacheService.get(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/cache/{key}")
    public ResponseEntity<String> deleteCachedData(@PathVariable String key) {
        boolean deleted = cacheService.delete(key);
        if (deleted) {
            return ResponseEntity.ok("Key deleted: " + key);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}