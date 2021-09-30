package org.example.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.List;

public class RedisStore {

    /**
     * Jedis连接池
     */
    private static final JedisPool pool;

    static {
        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);

        pool = new JedisPool(config, "106.12.118.54", 6379, 2000, "mima");
//        pool = new JedisPool(config, "127.0.0.1", 6379, 2000);
    }

    public static List<String> list(String key) {
        return pool.getResource().lrange(key, 0, -1);
    }


    public static void push(String key, String id) {
        pool.getResource().lpush(key, id);
    }

    public static void set(String key, Serializable value) {
        String str = value instanceof String ? ((String) value) : JSON.toJSONString(value);
        pool.getResource().set(key, str);
    }

    public static String get(String key) {
        return pool.getResource().get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return JSONObject.parseObject(get(key), clazz);
    }
}
