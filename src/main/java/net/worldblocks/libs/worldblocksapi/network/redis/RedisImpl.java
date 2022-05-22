package net.worldblocks.libs.worldblocksapi.network.redis;

import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;

public class RedisImpl implements Redis {
    final private Jedis jedis;
    public RedisImpl(FileConfiguration config) {
        String ip = config.getString("redis-ip");
        int port = config.getInt("redis-port");
        int db = config.getInt("redis-db");
        String user = config.getString("redis-user");
        String pass = config.getString("redis-pass");

        this.jedis = new Jedis("redis://" + ip + ":" + port + "/" + db);

        if (user != null && !user.isEmpty()) {
            jedis.auth(user, pass);
        } else {
            jedis.auth(pass);
        }
    }

    @Override
    public Jedis getJedis() {
        return jedis;
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public void addToCache(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public void removeFromCache(String key) {
        jedis.del(key);
    }
}
