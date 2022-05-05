package net.worldblocks.libs.worldblocksapi.databases;

import net.worldblocks.libs.worldblocksapi.Module;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLModule implements Module {

    private static SQLClient client;

    @Override
    public void instantiate(JavaPlugin plugin) {}

    public static void instantiateClient(String host, int port, String database, String username, String password) {
        client = new SQLClient(host, port, database, username, password);
    }

    public static SQLClient getClient(String host, int port, String database, String username, String password) {
        if (client == null) {
            client = new SQLClient(host, port, database, username, password);
        }

        return client;
    }

    public static SQLClient getClient() {
        if (client == null) {
            throw new IllegalStateException("SQLClient has not been instantiated yet!");
        }

        return client;
    }

}
