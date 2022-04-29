package net.worldblocks.libs.worldblocksapi.redis;

import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.Module;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RedisModule implements Module {

    @Getter private RedisClient redisClient = null;
    private RedisEventListener redisEventListener;
    @Getter private Plugin plugin;

    @Override
    public void instantiate(JavaPlugin plugin) {
        this.plugin = plugin;
        this.redisEventListener = new RedisEventListener(this);
        Bukkit.getPluginManager().registerEvents(redisEventListener, plugin);
        instantiateRedisClient(plugin.getConfig().getBoolean("redis-use-internal-messaging"));
    }

    public boolean hasRedisClient() {
        return redisClient != null;
    }

    public void instantiateRedisClient(boolean useInternalMessaging) {
        if (!useInternalMessaging) {
            this.redisClient = new RedisClient(plugin);
        } else {
            this.redisClient = new RedisClient(plugin, true);
        }
    }

    public RedisClient getRedisClient() {
        if (redisClient == null) {
            this.redisClient = new RedisClient(plugin);
        }

        return this.redisClient;
    }

}
