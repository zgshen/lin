package com.lin.admin.common.service;

import java.util.List;
import java.util.Set;

public interface IRedisService {
    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     * @return
     * @throws Exception
     */
    boolean set(String key, String value) throws Exception;

    /**
     * @param key
     * @return
     * @throws Exception
     */
    byte[] get(String key);

    boolean expire(String key, long expire) throws Exception;

    <T> boolean setList(String key, List<T> list) throws Exception;

    <T> List<T> getList(String key, Class<T> clz) throws Exception;

    long lpush(String key, Object obj);

    long rpush(String key, Object obj) throws Exception;

    void hmset(String key, Object obj) throws Exception;

    <T> T hget(String key, Class<T> clz) throws Exception;


    void del(String key) throws Exception;

    <T> List<T> hmGetAll(String key, Class<T> clz) throws Exception;

    String lpop(String key) throws Exception;

    /**
     * 是否存在
     *
     * @param key
     * @return
     * @throws Exception
     */
    boolean hasKey(String key) throws Exception;

    /**
     * 通过key获取java对象
     *
     * @param key 键
     * @param clz return
     * @throws Exception
     */
    <T> T getEntity(String key, Class<T> clz) throws Exception;

    /**
     * 清空DB
     */
    void flushDB();

    /**
     * 获取DB大小
     *
     * @return
     */
    Long dbSize();

    /**
     * 匹配keys
     *
     * @param keys
     * @return
     */
    Set<String> keys(String keys);

}
