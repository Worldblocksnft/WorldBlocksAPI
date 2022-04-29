package net.worldblocks.libs.worldblocksapi.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class RedisEventListener implements Listener {

    private RedisModule redisModule;
    public RedisEventListener(RedisModule redisModule) {
        this.redisModule = redisModule;
    }

    @EventHandler
    public void playerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String message = event.getMessage();

        System.out.println("Player Message: " + message);

        if (redisModule.hasRedisClient()) {
            Bukkit.getScheduler().runTaskLater(redisModule.getPlugin(), () -> {
                RedisClient client = redisModule.getRedisClient();
                client.sendPlayerMessage(uuid, "playermessage", message);
            }, 1L);
        }
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {

    }

    @EventHandler
    public void playerDisconnectEvent(PlayerQuitEvent e) {

    }

}
