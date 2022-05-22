package net.worldblocks.libs.worldblocksapi.network;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Network {
    public static NetworkPlayer getNetworkPlayer(UUID uuid) {
        return new NetworkPlayerImpl(uuid);
    }

    public static void cachePlayerAsNetworkJsonObject(Player player) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("uuid", player.getUniqueId().toString());
    }
}
