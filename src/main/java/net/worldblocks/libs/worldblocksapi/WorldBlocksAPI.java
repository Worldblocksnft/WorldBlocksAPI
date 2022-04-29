package net.worldblocks.libs.worldblocksapi;

import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.databases.*;
import net.worldblocks.libs.worldblocksapi.redis.RedisModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class WorldBlocksAPI extends JavaPlugin {

    private Map<Modules, Module> modules = new HashMap<>();
    @Getter private String serverName;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        serverName = getConfig().getString("server-name");
        if (serverName == null || serverName.equalsIgnoreCase("unnamed")) {
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe("[WorldblocksAPI] You must insert a name for your server to use in redis.");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe(" ");
            Bukkit.getLogger().severe(" ");
            Bukkit.shutdown();
            return;
        }

        instantiateDefaultModules();
        getRedisModule().instantiate(this);

    }

    public Module getModuleGeneric(Modules id) {
        return modules.get(id);
    }

    public void instantiateModule(Modules id, Module module) {
        module.instantiate(this);
        modules.put(id, module);
    }

    public void instantiateDefaultModules() {
        instantiateModule(Modules.DATABASES, new DatabaseModule());
        instantiateModule(Modules.REDIS, new RedisModule());
    }

    public static WorldBlocksAPI getAPI() {
        return (WorldBlocksAPI) Bukkit.getServer().getPluginManager().getPlugin("WorldblocksAPI");
    }

    public DatabaseModule getDatabaseModule() {
        return (DatabaseModule) modules.get(Modules.DATABASES);
    }

    public RedisModule getRedisModule() {
        return (RedisModule) modules.get(Modules.REDIS);
    }

}
