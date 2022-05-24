package net.worldblocks.libs.worldblocksapi.redis;

import net.worldblocks.libs.worldblocksapi.Service;
import net.worldblocks.libs.worldblocksapi.cache.RedisClient;
import net.worldblocks.libs.worldblocksapi.configuration.WBConfig;

public class RedisService implements Service {
    private WBConfig wbConfig;
    private RedisClient redisClient;

    @Override
    public void init(WBConfig wbConfig) {
        this.wbConfig = wbConfig;
        this.redisClient = getClient();
    }

    @Override
    public boolean isInitialized() {
        return redisClient != null;
    }

    public RedisClient getClient() {
        if (redisClient == null) {
            redisClient = new RedisClient(wbConfig);
        }

        return redisClient;
    }

    @Override
    public void close() {
        redisClient.close();
    }
}
