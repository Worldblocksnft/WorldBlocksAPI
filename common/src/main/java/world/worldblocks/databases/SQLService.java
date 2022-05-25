package world.worldblocks.databases;

import world.worldblocks.Service;
import world.worldblocks.configuration.WBConfig;

public class SQLService implements Service {

    private static SQLClient client;

    @Override
    public void init(WBConfig config) {}

    @Override
    public boolean isInitialized() {
        return client != null;
    }

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

    @Override
    public void close() {
    }
}
