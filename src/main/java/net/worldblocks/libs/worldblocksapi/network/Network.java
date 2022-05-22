package net.worldblocks.libs.worldblocksapi.network;

import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import net.worldblocks.libs.worldblocksapi.network.redis.Redis;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Network {
    public static NetworkPlayer getNetworkPlayer(UUID uuid) {
        Redis redis = WorldBlocksAPI.getAPI().getRedis();

        JSONObject jsonObject = redis.getJsonObject(uuid.toString());
        String name = jsonObject.get("name").toString();

        return new NetworkPlayerImpl(name, uuid);
    }

    public static void cachePlayerAsNetworkJsonObject(Player player) {
        Map<String, String> jsonMap = new HashMap<>();
        String uuid = player.getUniqueId().toString();

        jsonMap.put("uuid", uuid);
        jsonMap.put("name", player.getName());

        WorldBlocksAPI.getAPI().getRedis().addToCache(uuid, new JSONObject(jsonMap).toJSONString());
    }
}
