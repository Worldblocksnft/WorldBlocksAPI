package net.worldblocks.libs.worldblocksapi.server;

import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.Socket;

public enum Server {

    EXAMPLE_SERVER_1(25566),
    EXAMPLE_SERVER_2(25567);

    private static int TASK;

    private final int port;

    @Getter
    private boolean online;

    Server(int port) {
        this.port = port;
    }


    public static void startTask() {
        TASK = Bukkit.getScheduler().scheduleSyncRepeatingTask(WorldBlocksAPI.getAPI(), Server::update, 100, 100);
    }

    public static void killTask() {
        Bukkit.getScheduler().cancelTask(TASK);
    }

    private static void update() {
        Bukkit.getScheduler().runTaskAsynchronously(WorldBlocksAPI.getAPI(), () -> {
            for (Server server : Server.values()) {
                try {
                    Socket socket = new Socket("localhost", server.port);
                    socket.close();
                    server.online = true;
                } catch (IOException e) {
                    server.online = false;
                }
            }
        });
    }


}
