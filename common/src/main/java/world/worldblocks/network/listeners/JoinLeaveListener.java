package world.worldblocks.network.listeners;

import world.worldblocks.network.redis.Redis;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
    private final Redis redis;

    public JoinLeaveListener(Redis redis) {
        this.redis = redis;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        redis.addToCache(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        redis.removeFromCache(event.getPlayer());
    }
}
