package net.worldblocks.libs.worldblocksapi.network.redis;

import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;

public interface Redis {
    Jedis getJedis();

    String get(String key);

    JSONObject getJsonObject(String key);

    void addToCache(String key, String value);

    void removeFromCache(String key);
}
