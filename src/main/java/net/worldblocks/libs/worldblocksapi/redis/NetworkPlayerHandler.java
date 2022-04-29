package net.worldblocks.libs.worldblocksapi.redis;

import lombok.Setter;
import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class NetworkPlayerHandler implements Listener {

    private final RedisClient redisClient;
    private final String namespace = "networkplayer";
    @Setter private int timeout;
    private HashMap<UUID, NetworkPlayer> playerMap = new HashMap<>();
    private HashSet<NetworkPlayer> playerSet = new HashSet<>();

    public NetworkPlayerHandler(RedisClient redisClient) {
        this.redisClient = redisClient;
        this.timeout = 86400;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerConnectEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        long currentUnix = System.currentTimeMillis();
        String currentServer = WorldBlocksAPI.getAPI().getServerName();
        NetworkPlayer networkPlayer = new NetworkPlayer(uuid, player.getName(), currentServer, currentUnix);

        this.redisClient.addToCache(namespace, uuid.toString(), networkPlayer.toJson(), this.timeout);
    }

    /*
    Methods for getting the current cached iteration of each set.
    Preferably, always use the fresh set methods to ensure up-to-date values.
     */
    public HashSet<NetworkPlayer> getCachedSet() {
        return playerSet;
    }

    public HashMap<UUID, NetworkPlayer> getCachedMap() {
        return playerMap;
    }

    /*
    Returns a freshly obtained instance of the current player hashset and overrides the cached set.
     */
    public HashSet<NetworkPlayer> getPlayerSet() {
        playerSet.clear();
        for (String s : redisClient.getJedis().keys("*")) {
            if (s.startsWith(namespace + ":")) {
                try {
                    UUID uuid = UUID.fromString(s.split((namespace + ":"))[1]);
                    if (uuid != null) {
                        String json = redisClient.getJedis().get(namespace + ":" + uuid.toString());
                        if (json != null) {
                            NetworkPlayer networkPlayer = new NetworkPlayer(json);
                            playerSet.add(networkPlayer);
                        }
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return playerSet;
    }

    /*
    Returns a freshly obtained instance of the current player hashmap and overrides the cached map.
     */
    public HashMap<UUID, NetworkPlayer> getPlayerMap() {
        playerMap.clear();

        for (NetworkPlayer networkPlayer : getPlayerSet()) {
            playerMap.put(networkPlayer.getUuid(), networkPlayer);
        }

        return playerMap;
    }

}
