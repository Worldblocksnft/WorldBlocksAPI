package net.worldblocks.libs.worldblocksapi.redis.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class RedisPlayerMessageSendEvent extends Event {

    private static HandlerList handlerList = new HandlerList();
    @Getter private UUID sender;
    @Getter private String message;
    @Getter private String namespace;
    @Getter private Player player;

    public RedisPlayerMessageSendEvent(UUID sender, String message, String namespace, Player player) {
        this.sender = sender;
        this.message = message;
        this.namespace = namespace;
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
