package net.worldblocks.libs.worldblocksapi.configuration;

import org.bukkit.command.CommandSender;
import worlds.worldblock.supplydrops.util.Placeholder;
import worlds.worldblock.supplydrops.util.Util;

import java.util.List;

public interface Message {

    String getString();

    List<String> getStringList();

    String getPrefix();

    default void send(CommandSender player, Placeholder... placeholders) {

        if (player == null) {
            return;
        }

        String text = this.getString();

        text = Placeholder.apply(text, placeholders);

        player.sendMessage(Util.c(text.replace("{prefix}", getPrefix())));
    }

    default void sendList(CommandSender player, Placeholder... placeholders) {

        if(player == null){
            return;
        }

        StringBuilder text = new StringBuilder();
        for (String message : this.getStringList()) {
            text.append(Placeholder.apply(message, placeholders)).append("\n");
        }

        player.sendMessage(Util.c(text.toString().replace("{prefix}", (String) getPrefix())));
    }

}
