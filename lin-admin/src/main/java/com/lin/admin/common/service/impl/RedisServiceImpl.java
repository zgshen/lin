package com.lin.admin.common.service.impl;

import com.lin.admin.common.service.IRedisService;
import com.lin.admin.common.utils.JacksonUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Override
    public boolean set(final String key, final String value) throws Exception {
        Assert.hasText(key, "Key is not empty.");
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    @Override
    public byte[] get(final String key) {
        Assert.hasText(key, "Key is not empty.");
        byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value = connection.get(serializer.serialize(key));
            return value;
        });

        return result;
    }

    @Override
    public void del(final String key) throws Exception {
        Assert.hasText(key, "Key is not empty.");

        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection conn) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                return conn.del(serializer.serialize(key));
            }
        });
    }

    @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public <T> boolean setList(String key, List<T> list) throws Exception {
        Assert.hasText(key, "Key is not empty.");
        String value = JacksonUtil.toJson(list);
        return set(key, value);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clz) throws Exception {

        Assert.hasText(key, "Key is not empty.");

        byte[] json = get(key);
        if (json != null) {
            List<T> list = JacksonUtil.readValue(new String(json), JacksonUtil.getCollectionType(ArrayList.class, clz));
            return list;
        }
        return null;
    }

    @Override
    public long lpush(final String key, Object obj) {
        Assert.hasText(key, "Key is not empty.");

        final String value;
        try {
            value = JacksonUtil.toJson(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
            return count;
        });
        return result;
    }

    @Override
    public long rpush(final String key, Object obj) throws Exception {
        Assert.hasText(key, "Key is not empty.");

        final String value = JacksonUtil.toJson(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    @Override
    public void hmset(String key, Object obj) throws Exception {
        Assert.hasText(key, "Key is not empty.");

        Map<byte[], byte[]> data = JacksonUtil.readValue(JacksonUtil.toJson(obj), Map.class);
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.hMSet(serializer.serialize(key), data);
                return "";
            }
        });
    }

    @Override
    public <T> T hget(String key, Class<T> clz) throws Exception {
        Assert.hasText(key, "Key is not empty.");

        return redisTemplate.execute(new RedisCallback<T>() {
            @SneakyThrows
            @Override
            public T doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                Map<String, Object> result;
                Map<byte[], byte[]> data = connection.hGetAll(serializer.serialize(key));
                result = new HashMap<>(16);
                for (Map.Entry<byte[], byte[]> entry : data.entrySet()) {
                    result.put(serializer.deserialize(entry.getKey()), serializer.deserialize(entry.getValue()));
                }
                return JacksonUtil.readValue(JacksonUtil.toJson(result), clz);
            }
        });
    }

    @Override
    public <T> List<T> hmGetAll(String key, Class<T> clz) throws Exception {
        Assert.hasText(key, "Key is not empty.");

        List<Map<String, Object>> dataList = new ArrayList<>();
        return redisTemplate.execute(new RedisCallback<List<T>>() {
            @SneakyThrows
            @Override
            public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                Set<String> keysSet = redisTemplate.keys(key);
                Map<byte[], byte[]> data;
                Map<String, Object> result;
                for (String newKey : keysSet) {
                    data = connection.hGetAll(serializer.serialize(newKey));
                    result = new HashMap<>();
                    for (Map.Entry<byte[], byte[]> entry : data.entrySet()) {
                        result.put(serializer.deserialize(entry.getKey()), serializer.deserialize(entry.getValue()));
                    }
                    dataList.add(result);
                }
                return JacksonUtil.readValue(JacksonUtil.toJson(dataList),
                        JacksonUtil.getCollectionType(ArrayList.class, clz));
            }
        });
    }

    @Override
    public String lpop(final String key) throws Exception {
        Assert.hasText(key, "Key is not empty.");

        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res = connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }

    @Override
    public boolean hasKey(String key) throws Exception {
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T getEntity(String key, Class<T> clz) throws Exception {
        byte[] str = get(key);
        if (str == null) {
            return null;
        }
        return JacksonUtil.readValue(new String(str), clz);
    }

    @Override
    public void flushDB() {
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    @Override
    public Long dbSize() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    @Override
    public Set<String> keys(String keys) {
        return redisTemplate.keys(keys);
    }

}
