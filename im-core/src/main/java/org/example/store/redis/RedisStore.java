package org.example.store.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Im;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
public class RedisStore {

    /**
     * Jedis连接池
     */
    private static JedisPool pool = null;

    public static Jedis getJedis() {
        //断言 ，当前锁是否已经锁住，如果锁住了，就啥也不干，没锁的话就执行下面步骤
        if (pool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (pool != null) {
                jedis = pool.getResource();
            }
        } catch (Exception e) {
            log.error("获取资源一场", e);
        } finally {
            returnResource(jedis);
        }
        return jedis;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    private static synchronized void poolInit() {
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        pool = new JedisPool(config, Im.redisHost, Im.redisPort, Im.redisTimeOut, StrUtil.blankToDefault(Im.redisAuth, null));
    }

    public static List<String> list(String key) {
        List<String> list = getJedis().lrange(key, 0, -1);
        return CollUtil.reverse(list);
    }

    public static void hSet(String key, String field, String value) {
        getJedis().hset(key, field, value);
    }

    public static String hGet(String key, String field) {
        return getJedis().hget(key, field);
    }

    public static Map<String, String> hGet(String key) {
        return getJedis().hgetAll(key);
    }

    /**
     * 给某个Id下添加内容
     *
     * @param key   键
     * @param value 值
     */
    public static void push(String key, String value) {
        getJedis().lpush(key, value);
    }


    public static void set(String key, Serializable value) {
        String str = value instanceof String ? ((String) value) : JSON.toJSONString(value);
        getJedis().set(key, str);
    }

    public static String get(String key) {
        return getJedis().get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return JSONObject.parseObject(get(key), clazz);
    }

    public static void remove(String key) {
        getJedis().del(key);
    }
}
