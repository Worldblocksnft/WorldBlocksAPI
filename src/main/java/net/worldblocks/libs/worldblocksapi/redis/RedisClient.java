package net.worldblocks.libs.worldblocksapi.redis;

import lombok.Getter;
import lombok.Setter;
import net.worldblocks.libs.worldblocksapi.WorldBlocksAPI;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisMessageReceivedEvent;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisMessageSentEvent;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisPlayerMessageReceiveEvent;
import net.worldblocks.libs.worldblocksapi.redis.events.RedisPlayerMessageSendEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RedisClient {

    @Getter private Jedis jedis;
    private UUID serverUUID;
    private Plugin plugin;
    @Getter private NetworkPlayerHandler networkPlayerHandler;
    @Getter @Setter private int ttl = 120;
    @Getter @Setter private int playerMessageTtl = 10;
    private final String namespace = "worldblocksmessaging";
    private boolean isEnabled = false;
    private final String ip;
    private final int port;
    private final int db;
    private final String user;
    private final String pass;
    private boolean useBukkitEvents;

    public RedisClient(Plugin plugin) {
        this.plugin = plugin;
        this.useBukkitEvents = false;

        //Load details from "plugins/WorldBlocksAPI/config.yml"
        this.ip = plugin.getConfig().getString("redis-ip");
        this.port = plugin.getConfig().getInt("redis-port");
        this.db = plugin.getConfig().getInt("redis-db");
        this.user = plugin.getConfig().getString("redis-user");
        this.pass = plugin.getConfig().getString("redis-pass");

        //Redis Internal URI builder.
        this.jedis = new Jedis("redis://" + this.ip + ":" + this.port + "/" + this.db);

        //Authentication & Login
        if (!user.equalsIgnoreCase("") && !pass.equalsIgnoreCase(""))
            this.jedis.auth(user, pass);
        if (!pass.equalsIgnoreCase("") && user.equalsIgnoreCase(""))
            this.jedis.auth(pass);

        if (plugin.getConfig().contains("server-id")) {
            this.serverUUID = UUID.fromString(plugin.getConfig().getString("server-id"));
        } else {
            this.serverUUID = UUID.randomUUID();
            plugin.getConfig().set("server-id", serverUUID.toString());
            try {
                plugin.getConfig().save(new File(plugin.getDataFolder().toString(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.networkPlayerHandler = new NetworkPlayerHandler(this);
        Bukkit.getPluginManager().registerEvents(this.networkPlayerHandler, this.plugin);
    }

    public RedisClient(Plugin plugin, boolean useBukkitEvents) {
        this.plugin = plugin;

        //Load details from "plugins/WorldBlocksAPI/config.yml"
        this.ip = plugin.getConfig().getString("redis-ip");
        this.port = plugin.getConfig().getInt("redis-port");
        this.db = plugin.getConfig().getInt("redis-db");
        this.user = plugin.getConfig().getString("redis-user");
        this.pass = plugin.getConfig().getString("redis-pass");

        //Redis Internal URI builder.
        this.jedis = new Jedis("redis://" + this.ip + ":" + this.port + "/" + this.db);

        //Authentication & Login
        if (!user.equalsIgnoreCase("") && !pass.equalsIgnoreCase(""))
            this.jedis.auth(user, pass);
        if (!pass.equalsIgnoreCase("") && user.equalsIgnoreCase(""))
            this.jedis.auth(pass);

        if (plugin.getConfig().contains("server-id")) {
            this.serverUUID = UUID.fromString(plugin.getConfig().getString("server-id"));
        } else {
            this.serverUUID = UUID.randomUUID();
            plugin.getConfig().set("server-id", serverUUID.toString());
            try {
                plugin.getConfig().save(new File(plugin.getDataFolder().toString(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.networkPlayerHandler = new NetworkPlayerHandler(this);
        Bukkit.getPluginManager().registerEvents(this.networkPlayerHandler, this.plugin);

        if (useBukkitEvents) {
            //Ensure primary namespace exists in redis.
            if (jedis.keys(namespace) == null || jedis.keys(namespace).size() == 0)
                jedis.set(namespace, "{}");

            checkMessages();
        }
    }

    /*
    Player specific messages.
     */
    public void sendPlayerMessage(Player player, String namespace, String message) {
        String json = jedis.get(this.namespace);
        JSONObject jsonObject = parseJsonSafe(json);

        if (jsonObject.containsKey(namespace)) {
            JSONObject nsObj = (JSONObject) jsonObject.get(namespace);
            List<String> received = new ArrayList<>();
            JSONObject entry = new JSONObject();
            entry.put("message", message);
            entry.put("received", received);
            entry.put("origin-server", WorldBlocksAPI.getAPI().getServerName());
            entry.put("uuid", player.getUniqueId().toString());
            nsObj.put(UUID.randomUUID(), entry);
            jsonObject.put(namespace, nsObj);
            jedis.set(this.namespace, jsonObject.toJSONString());
            if (ttl != 0)
                jedis.expire(this.namespace, playerMessageTtl);
        } else {
            JSONObject nsObj = new JSONObject();
            List<String> received = new ArrayList<>();
            JSONObject entry = new JSONObject();
            entry.put("message", message);
            entry.put("received", received);
            entry.put("origin-server", WorldBlocksAPI.getAPI().getServerName());
            entry.put("uuid", player.getUniqueId().toString());
            nsObj.put(UUID.randomUUID(), entry);
            jsonObject.put(namespace, nsObj);
            jedis.set(this.namespace, jsonObject.toJSONString());
            if (ttl != 0)
                jedis.expire(this.namespace, playerMessageTtl);

            Bukkit.getPluginManager().callEvent(new RedisPlayerMessageSendEvent(player.getUniqueId(), namespace, message, player));
        }
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

    /*
    General purpose messaging for redis.
     */
    public void sendMessage(String namespace, String message) {
        String json = jedis.get(this.namespace);
        JSONObject jsonObject = parseJsonSafe(json);

        if (jsonObject.containsKey(namespace)) {
            JSONObject nsObj = (JSONObject) jsonObject.get(namespace);
            List<String> received = new ArrayList<>();
            JSONObject entry = new JSONObject();
            entry.put("message", message);
            entry.put("received", received);
            entry.put("origin-server", WorldBlocksAPI.getAPI().getServerName());
            nsObj.put(UUID.randomUUID(), entry);
            jsonObject.put(namespace, nsObj);
            jedis.set(this.namespace, jsonObject.toJSONString());
            if (ttl != 0)
                jedis.expire(this.namespace, ttl);
        } else {
            JSONObject nsObj = new JSONObject();
            List<String> received = new ArrayList<>();
            JSONObject entry = new JSONObject();
            entry.put("message", message);
            entry.put("received", received);
            entry.put("origin-server", WorldBlocksAPI.getAPI().getServerName());
            nsObj.put(UUID.randomUUID(), entry);
            jsonObject.put(namespace, nsObj);
            jedis.set(this.namespace, jsonObject.toJSONString());
            if (ttl != 0)
                jedis.expire(this.namespace, ttl);

            Bukkit.getPluginManager().callEvent(new RedisMessageSentEvent(namespace, message));
        }
    }

    public void checkMessages() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!isEnabled) {
                Bukkit.getLogger().info("[WorldblocksAPI] The redis message channel receiver has started.");
                isEnabled = true;
            }

            String json = jedis.get(namespace);
            JSONObject parsed = parseJsonSafe(json);
            for (Object s : parsed.keySet()) {
                JSONObject ns = (JSONObject) parsed.get(s);
                for (Object message : ns.keySet()) {
                    JSONObject entry = (JSONObject) ns.get(message);
                    List<String> received = (List<String>) entry.get("received");
                    if (received != null && !received.contains(serverUUID.toString())) {
                        //Non-Player Send Message
                        if (!entry.containsKey("uuid")) {
                            String msg = (String) entry.get("message");
                            received.add(serverUUID.toString());
                            ns.put(message, entry);
                            parsed.put(s, ns);
                            jedis.set(namespace, parsed.toJSONString());
                            if (ttl != 0)
                                jedis.expire(namespace, ttl);

                            //Call bukkit event with namespace received.
                            Bukkit.getPluginManager().callEvent(new RedisMessageReceivedEvent((String) message, msg, (String) entry.get("origin-server")));
                        } else {
                            //Player Send Message
                            String uid = (String) entry.get("uuid");
                            Player player = Bukkit.getPlayer(UUID.fromString(uid));
                            Optional<Player> optionalPlayer = Optional.empty();
                            if (player != null) {
                                optionalPlayer = Optional.of(player);
                            }
                            String msg = (String) entry.get("message");
                            String origin = (String) entry.get("origin-server");
                            received.add(serverUUID.toString());
                            ns.put(message, entry);
                            parsed.put(s, ns);
                            jedis.set(namespace, parsed.toJSONString());
                            if (ttl != 0)
                                jedis.expire(namespace, ttl);

                            //Call bukkit event with namespace received.
                            Bukkit.getPluginManager().callEvent(new RedisPlayerMessageReceiveEvent(UUID.fromString(uid), msg, (String) message, origin, optionalPlayer));
                        }
                    }
                }
            }
        }, 20L, 80L);
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
