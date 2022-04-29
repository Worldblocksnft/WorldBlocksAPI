package net.worldblocks.libs.worldblocksapi.redis.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Optional;
import java.util.UUID;

public class RedisPlayerMessageReceiveEvent extends Event {

    private static HandlerList handlerList = new HandlerList();
    @Getter private UUID sender;
    @Getter private String message;
    @Getter private String namespace;
    @Getter private String origin;
    @Getter private Optional<Player> player;

    public RedisPlayerMessageReceiveEvent(UUID sender, String message, String namespace, String origin, Optional<Player> player) {
        this.sender = sender;
        this.message = message;
        this.namespace = namespace;
        this.origin = origin;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
