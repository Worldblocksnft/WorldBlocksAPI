package world.worldblocks.redis.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RedisMessageSentEvent extends Event {
    @Getter private static HandlerList handlers = new HandlerList();

    @Getter private String namespace;
    @Getter private String message;

    public RedisMessageSentEvent(String namespace, String message) {
        this.namespace = namespace;
        this.message = message;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}
