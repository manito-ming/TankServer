package org.dao;

import redis.clients.jedis.Jedis;

public class RedisUser {
    public Jedis redis;

    {
        redis = new Jedis("127.0.0.1", 6379);

    }
    public String get(String key) {

        return redis.get("name");

    }
    public String set(String key, String value) {

        return redis.set(key, value);

    }
    public Long del(String... keys) {

        return redis.del(keys);

    }
    public Boolean exists(String key) {

        return redis.exists(key);

    }
    public String hget(final String key, final String field) {

        return redis.hget(key, field);

    }
    public Long hset(final String key, final String field, final String value) {

        return redis.hset(key, field, value);

    }

    public Long expire(final String key, final int seconds) {

        return redis.expire(key, seconds);

    }

    public Boolean hexists(final String key, final String field) {

        return redis.hexists(key, field);

    }

}
