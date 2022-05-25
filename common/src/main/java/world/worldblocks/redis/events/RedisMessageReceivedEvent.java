package world.worldblocks.redis.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RedisMessageReceivedEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    @Getter private String namespace;
    @Getter private String message;
    @Getter private String origin;

    public RedisMessageReceivedEvent(String namespace, String message, String origin) {
        this.message = message;
        this.namespace = namespace;
        this.origin = origin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {return handlers;}
}
