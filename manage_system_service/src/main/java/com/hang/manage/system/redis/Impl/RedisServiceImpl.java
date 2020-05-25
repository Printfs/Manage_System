package com.hang.manage.system.redis.Impl;

import com.hang.manage.system.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void put(String key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    //list放数据
    @Override
    public void lpush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    //list拿数据
    @Override
    public List rpop(String key, Long length) {
        List list = redisTemplate.opsForList().range(key, 0, length);
        return list;
    }

    //长度
    @Override
    public Long listLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    //list元素删除
    public void listDelete(String key, String value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }


}
