package com.example.AddressBookApp.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    public void saveToCache(String key, Object value) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String jsonValue = om.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, jsonValue, 10, TimeUnit.MINUTES);
    }

    public Object getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void removeFromCache(String key) {
        redisTemplate.delete(key);
    }
}