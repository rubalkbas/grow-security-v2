package com.ittiva.generic.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 *  @author ITTIVA
 */
@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void storeToken(String userId, String token, long ttl) {
        redisTemplate.opsForValue().set(userId, token, ttl, TimeUnit.SECONDS);
        log.debug("Token almacenado en Redis para el usuario: {}", userId);
        log.debug("Token ttl: {}", ttl);
    }

    public String getToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
        log.info("Token eliminado para el usuario: {}", token);
    }

    public boolean isTokenValid(String token) {
        return redisTemplate.hasKey(token);
    }
}