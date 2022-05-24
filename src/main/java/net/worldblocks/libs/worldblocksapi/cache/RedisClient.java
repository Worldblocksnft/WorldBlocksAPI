package net.worldblocks.libs.worldblocksapi.cache;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.configuration.WBConfig;
import redis.clients.jedis.Jedis;

public class RedisClient implements CacheClient {

    @Getter
    protected Jedis jedis;
    protected Gson gson;

    public RedisClient(WBConfig wbConfig) {
        String ip = wbConfig.getRedisIp();
        int port = wbConfig.getRedisPort();
        int db = wbConfig.getRedisDb();
        String user = wbConfig.getRedisUser();
        String pass = wbConfig.getRedisPass();

        this.jedis = new Jedis("redis://" + ip + ":" + port + "/" + db);

        if (!Strings.isNullOrEmpty(user)) {
            jedis.auth(user, pass);
        }

        this.gson = new Gson();
    }

    @Override
    public void connect() {
        jedis.connect();
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public void putObject(String key, Object object) {
        String bytes = gson.toJson(object);
        jedis.set(key, bytes);
    }

    @Override
    public String getStr(String key) {
        return jedis.get(key);
    }

    @Override
    public void putStr(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public void rm(String key) {
        jedis.del(key);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
