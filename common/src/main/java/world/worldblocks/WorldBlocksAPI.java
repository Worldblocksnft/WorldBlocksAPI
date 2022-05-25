package world.worldblocks;

import com.google.common.base.Strings;
import lombok.Getter;
import world.worldblocks.bungee.BungeeHandler;
import world.worldblocks.command.AbstractCommand;
import world.worldblocks.configuration.WBConfig;
import world.worldblocks.databases.SQLService;
import world.worldblocks.redis.RedisService;
import world.worldblocks.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class WorldBlocksAPI extends JavaPlugin {

    @Getter
    private static WorldBlocksAPI instance;
    private Map<IService, Service> services = new HashMap<>();
    @Getter
    public WBConfig wbConfig;
    @Getter
    private BungeeHandler bungeeHandler;

    @Override
    public void onEnable() {
        instance = this;

        // Configuration
        saveDefaultConfig();
        this.wbConfig = new WBConfig(instance, this.getConfig());

        if (Strings.isNullOrEmpty(wbConfig.getServerName()) || wbConfig.getServerName().equalsIgnoreCase("unnamed")) {
            Bukkit.getLogger().severe("[WorldBlocksAPI] You must insert a name for your server to use in redis.");
            Bukkit.shutdown();
            return;
        }

        initDefaultServices();

//        this.bungeeHandler = new BungeeHandler();

//        // Database
//        String ip = "localhost";
//        String dbName = "worldblocks";
//        String user = "postgres";
//        String pass = "MUixjs.cHqa7feaZsNsFEkm6L";
//        int port = 5432;

//        SQLModule.instantiateClient(ip, port, dbName, user, pass);
//        SQLModule.getClient().createTable("worldblocks_players", new PostgresColumn("uuid", PostgresType.UUID), new PostgresColumn("data", PostgresType.JSON));

        Server.startTask();
    }

    @Override
    public void onDisable() {

        // Close service connections
        this.services.values().forEach(service -> {
            try {
                service.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Server.killTask();

    }

    public Service getModule(IService id) {
        return services.get(id);
    }

    public void initModule(IService id, Service service) {
        service.init(this.wbConfig);
        services.put(id, service);
    }

    public void initDefaultServices() {
        initModule(IService.DATABASES, new SQLService());
        initModule(IService.REDIS, new RedisService());
    }

    public SQLService getDatabaseModule() {
        return (SQLService) services.get(IService.DATABASES);
    }

    public RedisService getRedisModule() {
        return (RedisService) services.get(IService.REDIS);
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
