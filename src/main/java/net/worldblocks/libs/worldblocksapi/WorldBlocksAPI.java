package net.worldblocks.libs.worldblocksapi;

import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.bungee.BungeeHandler;
import net.worldblocks.libs.worldblocksapi.command.AbstractCommand;
import net.worldblocks.libs.worldblocksapi.configuration.Serialization;
import net.worldblocks.libs.worldblocksapi.databases.*;
import net.worldblocks.libs.worldblocksapi.item.Item;
import net.worldblocks.libs.worldblocksapi.network.redis.Redis;
import net.worldblocks.libs.worldblocksapi.network.redis.RedisImpl;
import net.worldblocks.libs.worldblocksapi.redis.RedisModule;
import net.worldblocks.libs.worldblocksapi.server.Server;
import net.worldblocks.libs.worldblocksapi.utilities.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class WorldBlocksAPI extends JavaPlugin {

    private Map<Modules, Module> modules = new HashMap<>();
    @Getter
    private String serverName;

    @Getter
    private BungeeHandler bungeeHandler;

    @Getter
    private Redis redis;

    private static WorldBlocksAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Serialization.register(Item.class);
        Server.startTask();
        this.redis = new RedisImpl(getConfig());
        this.serverName = getConfig().getString("server-name");
        this.bungeeHandler = new BungeeHandler();
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

        getDatabaseModule();

        // Database
        String ip = "localhost";
        String dbName = "worldblocks";
        String user = "postgres";
        String pass = "MUixjs.cHqa7feaZsNsFEkm6L";
        int port = 5432;

        SQLModule.instantiateClient(ip, port, dbName, user, pass);
        SQLModule.getClient().createTable("worldblocks_players", new PostgresColumn("uuid", PostgresType.UUID), new PostgresColumn("data", PostgresType.JSON));
    }

    @Override
    public void onDisable() {
        Server.killTask();
    }

    public Module getModuleGeneric(Modules id) {
        return modules.get(id);
    }

    public void instantiateModule(Modules id, Module module) {
        module.instantiate(this);
        modules.put(id, module);
    }

    public void instantiateDefaultModules() {
        instantiateModule(Modules.DATABASES, new SQLModule());
        instantiateModule(Modules.REDIS, new RedisModule());
    }

    public static WorldBlocksAPI getAPI() {
        return instance;
    }

    public SQLModule getDatabaseModule() {
        return (SQLModule) modules.get(Modules.DATABASES);
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
