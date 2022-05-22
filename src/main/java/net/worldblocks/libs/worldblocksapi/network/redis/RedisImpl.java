package net.worldblocks.libs.worldblocksapi.network.redis;

import redis.clients.jedis.Jedis;

public class RedisImpl implements Redis {
    @Override
    public Jedis getJedis() {
        return null;
    }

    @Override
    public void addToCache(String key, String value) {

    }

    @Override
    public void removeFromCache() {

    }
}
