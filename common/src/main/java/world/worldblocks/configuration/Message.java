package world.worldblocks.configuration;

import world.worldblocks.utilities.ColorUtils;
import world.worldblocks.utilities.Placeholder;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface Message {

    String getString();

    List<String> getStringList();

    String getPrefix();

    default void send(CommandSender player, Placeholder... placeholders) {

        if (player == null) {
            return;
        }

        try{
            sendList(player, placeholders);
            return;
        }catch (ClassCastException ignored){

        }

        String text = this.getString();

        text = Placeholder.apply(text, placeholders);

        player.sendMessage(ColorUtils.spigotColor(text.replace("{prefix}", getPrefix())));
    }

    default void sendList(CommandSender player, Placeholder... placeholders) {

        if(player == null){
            return;
        }

        StringBuilder text = new StringBuilder();
        for (String message : this.getStringList()) {
            text.append(Placeholder.apply(message, placeholders)).append("\n");
        }

        player.sendMessage(ColorUtils.spigotColor(text.toString().replace("{prefix}", (String) getPrefix())));
    }

}
