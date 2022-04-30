package net.worldblocks.libs.worldblocksapi;

import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.command.AbstractCommand;
import net.worldblocks.libs.worldblocksapi.configuration.Serialization;
import net.worldblocks.libs.worldblocksapi.databases.*;
import net.worldblocks.libs.worldblocksapi.item.Item;
import net.worldblocks.libs.worldblocksapi.redis.RedisModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class WorldBlocksAPI extends JavaPlugin {

    private Map<Modules, Module> modules = new HashMap<>();
    @Getter
    private String serverName;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Serialization.register(Item.class);
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

    public void registerCommands(AbstractCommand... abstractCommands) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            for (AbstractCommand abstractCommand : abstractCommands) {
                commandMap.register(abstractCommand.getLabel(), abstractCommand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
