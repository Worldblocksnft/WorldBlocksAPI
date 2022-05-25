package world.worldblocks.network;

import java.util.UUID;

public interface NetworkPlayer {
    String getName();

    UUID getUuid();

    String getServerName();

    void sendMessage(String message);

    void sendTitle(String header, String footer);

    void sendActionBar(String message);

    void sendToServer(String server);
}
