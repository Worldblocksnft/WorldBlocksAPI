package world.worldblocks.network;

import world.worldblocks.WorldBlocksAPI;

import java.util.UUID;

public class NetworkPlayerImpl implements NetworkPlayer {
    private final String name;
    private final UUID uuid;

    public NetworkPlayerImpl(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getServerName() {
        return WorldBlocksAPI.getInstance().getWbConfig().getServerName();
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendTitle(String header, String footer) {

    }

    @Override
    public void sendActionBar(String message) {

    }

    @Override
    public void sendToServer(String server) {

    }
}
