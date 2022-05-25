package world.worldblocks.redis;

import world.worldblocks.Service;
import world.worldblocks.cache.RedisClient;
import world.worldblocks.configuration.WBConfig;

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
