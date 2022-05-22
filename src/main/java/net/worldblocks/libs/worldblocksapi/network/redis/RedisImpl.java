package net.worldblocks.libs.worldblocksapi.network.redis;

import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import net.worldblocks.libs.worldblocksapi.network.NetworkPlayer;
import net.worldblocks.libs.worldblocksapi.network.NetworkPlayerImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RedisImpl implements Redis {
    private final Jedis jedis;
    public RedisImpl(FileConfiguration config) {
        String ip = config.getString("redis-ip");
        int port = config.getInt("redis-port");
        int db = config.getInt("redis-db");
        String user = config.getString("redis-user");
        String pass = config.getString("redis-pass");

        this.jedis = new Jedis("redis://" + ip + ":" + port + "/" + db);

        if (user != null && !user.isEmpty()) {
            jedis.auth(user, pass);
        } else {
            jedis.auth(pass);
        }
    }

    @Override
    public Jedis getJedis() {
        return jedis;
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public JSONObject getJsonObject(String key) {
        if (key != null) {
            try {
                String value = get(key);
                return (JSONObject) new JSONParser().parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    @Override
    public Optional<NetworkPlayer> getNetworkPlayer(UUID uuid) {
        String key = uuid.toString();

        if (jedis.exists(key)) {
            JSONObject jsonObject = getJsonObject(key);
            String name = jsonObject.get("name").toString();
            return Optional.of(new NetworkPlayerImpl(name, uuid));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addToCache(String key, JSONObject jsonObject) {
        jedis.set(key, jsonObject.toJSONString());
    }

    @Override
    public void addToCache(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public void addToCache(Player player) {
        Map<String, String> jsonMap = new HashMap<>();
        String uuid = player.getUniqueId().toString();

        jsonMap.put("uuid", uuid);
        jsonMap.put("name", player.getName());

        addToCache(uuid, new JSONObject(jsonMap));
    }

    @Override
    public void removeFromCache(Player player) {
        removeFromCache(player.getUniqueId().toString());
    }

    @Override
    public void removeFromCache(String key) {
        jedis.del(key);
    }
}
