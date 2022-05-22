package net.worldblocks.libs.worldblocksapi.network;

import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;

import java.util.UUID;

public class Network {
    public static NetworkPlayer getNetworkPlayer(UUID uuid) {
        return WorldBlocksAPI.getAPI().getRedis().getNetWorkPlayer(uuid);
    }
}
