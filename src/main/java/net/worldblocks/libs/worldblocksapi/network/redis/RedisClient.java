package net.worldblocks.libs.worldblocksapi.network.redis;

import lombok.Getter;
import lombok.Setter;
import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisMessageReceivedEvent;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisMessageSentEvent;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisPlayerMessageReceiveEvent;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisPlayerMessageSendEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisClient {
    @Getter final private Jedis jedis;
    @Getter @Setter private int ttl = 120;
    @Getter @Setter private int playerMessageTtl = 10;
    private final String namespace = "worldblocksmessaging";

    public RedisClient(FileConfiguration config) {
        String ip = config.getString("redis-ip");
        int port = config.getInt("redis-port");
        int db = config.getInt("redis-db");
        String user = config.getString("redis-user");
        String pass =config.getString("redis-pass");

        this.jedis = new Jedis("redis://" + ip + ":" + port + "/" + db);

        // Authentication & Login
        if (!user.equalsIgnoreCase("") && !pass.equalsIgnoreCase(""))
            this.jedis.auth(user, pass);
        if (!pass.equalsIgnoreCase("") && user.equalsIgnoreCase(""))
            this.jedis.auth(pass);
    }

    public String get(String key) {
        return this.jedis.get(key);
    }

    public void addToCache(String key, String value, int expire) {
        this.jedis.set(key, value);
        this.jedis.expire(key, expire);
    }

    public void addToCache(String namespace, String key, String value, int expire) {
        String fullKey = namespace + ":" + key;
        this.jedis.set(fullKey, value);
        this.jedis.expire(fullKey, expire);
    }

    public void sendPlayerMessage(UUID uuid, String namespace, String message) {
        String json = jedis.get(this.namespace);
        JSONObject jsonObject = parseJsonSafe(json);

        if (jsonObject.containsKey(namespace)) {
            JSONObject nsObj = (JSONObject) jsonObject.get(namespace);
            List<String> received = new ArrayList<>();
            JSONObject entry = new JSONObject();
            entry.put("message", message);
            entry.put("received", received);
            entry.put("origin-server", WorldBlocksAPI.getAPI().getServerName());
            entry.put("uuid", uuid.toString());
            nsObj.put(UUID.randomUUID(), entry);
            jsonObject.put(namespace, nsObj);
            jedis.set(this.namespace, jsonObject.toJSONString());
            if (ttl != 0)
                jedis.expire(this.namespace, playerMessageTtl);
            System.out.println("Sent player message");
        } else {
            JSONObject nsObj = new JSONObject();
            List<String> received = new ArrayList<>();
            JSONObject entry = new JSONObject();
            entry.put("message", message);
            entry.put("received", received);
            entry.put("origin-server", WorldBlocksAPI.getAPI().getServerName());
            entry.put("uuid", uuid.toString());
            nsObj.put(UUID.randomUUID(), entry);
            jsonObject.put(namespace, nsObj);
            jedis.set(this.namespace, jsonObject.toJSONString());
            if (ttl != 0)
                jedis.expire(this.namespace, playerMessageTtl);

            Player player = Bukkit.getPlayer(uuid);
            Bukkit.getPluginManager().callEvent(new RedisPlayerMessageSendEvent(uuid, namespace, message, player));
            System.out.println("Sent player message");
        }
    }

    private JSONObject parseJsonSafe(String json) {
        if (json != null) {
            try {
                return (JSONObject) new JSONParser().parse(json);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

}
