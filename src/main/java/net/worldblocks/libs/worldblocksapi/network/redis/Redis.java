package net.worldblocks.libs.worldblocksapi.network.redis;

import net.worldblocks.libs.worldblocksapi.network.NetworkPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Optional;
import java.util.UUID;

public interface Redis {
    Jedis getJedis();

    String get(String key);

    JSONObject getJsonObject(String key);

    Optional<NetworkPlayer> getNetworkPlayer(UUID uuid);

    void addToCache(String key, JSONObject jsonObject);

    void addToCache(String key, String value);

    void addToCache(Player player);

    void removeFromCache(Player player);

    void removeFromCache(String key);
}
