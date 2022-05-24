package net.worldblocks.libs.worldblocksapi.configuration;

import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class WBConfig {

    private final int configVersion;
    private final String serverName;
    private final String redisIp;
    private final int redisPort;
    private final int redisDb;
    private final String redisUser;
    private final String redisPass;
//    private final String databaseType;
//    private final String databaseMySQLAddress;
//    private final int databaseMySQLPort;
//    private final String databaseMySQLDBName;
//    private final String databaseMySQLUsername;
//    private final String databaseMySQLPassword;
//    private final String databaseMySQLPrefix;
//    private final boolean databaseMySQLSSL;
//    private final boolean databaseMySQLPublicKeyRetrieval;
//    private final long databaseMySQLWaitTimeout;
//    private final long databaseMySQLMaxLifetime;

    public WBConfig(WorldBlocksAPI plugin, FileConfiguration config) {
        configVersion = config.getInt("config-version");
        serverName = config.getString("server-name");

        redisIp = config.getString("redis.ip");
        redisPort = config.getInt("redis.port");
        redisDb = config.getInt("redis.db");
        redisUser = config.getString("redis.user");
        redisPass = config.getString("redis.pass");

//        databaseType = config.getString("database.type");
//        databaseMySQLAddress = config.getString("database.address");
//        databaseMySQLPort = config.getInt("database.port");
//        databaseMySQLDBName = config.getString("database.db-name");
//        databaseMySQLUsername = config.getString("database.user-name");
//        databaseMySQLPassword = config.getString("database.password");
//        databaseMySQLPrefix = config.getString("database.prefix");
//        databaseMySQLSSL = config.getBoolean("database.useSSL");
//        databaseMySQLPublicKeyRetrieval = config.getBoolean("database.allowPublicKeyRetrieval");
//        databaseMySQLWaitTimeout = config.getLong("database.waitTimeout");
//        databaseMySQLMaxLifetime = config.getLong("database.maxLifetime");
    }
}
