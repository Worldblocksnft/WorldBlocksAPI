package net.worldblocks.libs.worldblocksapi.network;

import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import org.json.simple.JSONObject;

import java.util.UUID;

public class NetworkPlayerImpl implements NetworkPlayer {
    private final String name;
    private final UUID uuid;
    public NetworkPlayerImpl(UUID uuid) {
        JSONObject jsonObject = WorldBlocksAPI.getAPI().getRedis().getJsonObject(uuid.toString());
        this.name = jsonObject.get("name").toString();
        this.uuid = UUID.fromString(jsonObject.get("uuid").toString());
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
        return WorldBlocksAPI.getAPI().getServerName();
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
