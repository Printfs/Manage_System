package com.hang.manage.system.redis;

import java.util.List;

public interface RedisService {

    /**
     * @param
     * @param  seconds   超时时间
     */

    public void put(String key, Object value, long seconds);


    //取值
    public Object get(String key);

    //删
    public void delete(String key);


    public void lpush(String key,String value);


    public List rpop(String key, Long length);


    public Long listLength(String key);

    //list元素删除
    public void listDelete(String key,String value);

}
