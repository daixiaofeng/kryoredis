package com.feng.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author daixiaofeng
 * @Description
 **/
@Component
public class RedisUtils {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;

    /**
     * 获取缓存的key
     *
     * @param id
     * @return
     */
    private <T> byte[] getKey(T id) {
        RedisSerializer serializer = redisTemplate.getKeySerializer();
        return serializer.serialize(id);
    }

    /** ----------------------------↓↓ object 相关操作 ↓↓------------------------- **/

    public <T> void setObject(String key,T value){
        setObject(key,value,NOT_EXPIRE);
    }

    public <T> void setObject(String key,T value,Long expire){
        if (expire.equals(NOT_EXPIRE)) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    public  void set(String key, String value) {
        set(key, value, DEFAULT_EXPIRE);
    }
    /**
     * 设置缓存
     *
     * @param key    键
     * @param value  值
     * @param expire 超时时间，NOT_EXPIRE为不超时
     */
    public  void set(String key, String value, long expire) {
        if (expire != NOT_EXPIRE) {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public <T> T getObject(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /** ----------------------------↑↑ object 相关操作 ↑↑------------------------- **/




    /** ----------------------------↓↓ hash 相关 ↓↓------------------------------**/

    public void hPut(String key, String field, Object value) {
        hPut( key,  field,  value, DEFAULT_EXPIRE);
    }

    public void hPut(String key, String field, Object value,long expire) {
        redisTemplate.opsForHash().put(key, field, value);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置Map缓存
     *
     * @param key   键
     * @param value 值
     */
    public void hPutAll(String key, Map<String, Object> value) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
        redisTemplate.opsForHash().putAll(key,value);
    }


    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }
    /** ----------------------------↑↑ hash 相关操作 ↑↑------------------------- **/

    /** ----------------------------↓↓ list 相关操作 ↓↓------------------------- **/
    /**
     * 存储在list头部
     *
     * @param key
     * @param value
     * @return
     */
    public <T> Long lLeftPush(String key, T value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public <T> Long lLeftPushAll(String key, T... value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public <T> Long lLeftPushAll(String key, Collection<T> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public <T> Long lRightPush(String key, T value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public <T> Long lRightPushAll(String key, T... value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public <T> Long lRightPushAll(String key, Collection<T> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    public <T> T lLeftPop(String key){
        return (T) redisTemplate.opsForList().leftPop(key);
    }
    public <T> T lRightPop(String key){
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    public <T> List<T> lRange(String key){
        return (List<T>) redisTemplate.opsForList().range(key,0,-1);
    }
    /** ----------------------------↑↑ list 相关操作 ↑↑------------------------- **/

    /** ----------------------------↓↓ Key 相关操作 ↓↓------------------------- **/

    /**
     * 删除key
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key
     * @return
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /** ----------------------------↑↑ Key 相关操作 ↑↑------------------------- **/


}
