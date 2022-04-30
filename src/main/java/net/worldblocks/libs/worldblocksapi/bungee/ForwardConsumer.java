package net.worldblocks.libs.worldblocksapi.bungee;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ForwardConsumer {
    void accept(String channel, Player player, byte[] data);
}
