package world.worldblocks.message;

import world.worldblocks.utilities.ColorUtils;
import world.worldblocks.utilities.Placeholder;
import org.bukkit.command.CommandSender;
public enum LibMessages {

    NO_PERMISSION("no-permission", "&cYou do not have permission to perform this command!"),
    PLAYERS_ONLY("players-only", "&cOnly players can perform this command!"),
    PLAYER_NOT_FOUND("player-not-found", "&cCould not find player: {player}!"),
    USAGE("usage", "&cIncorrect Usage! Use: {usage}");

    private final String key;
    private final Object value;

    LibMessages(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public void send(CommandSender commandSender, Placeholder... placeholders) {
        commandSender.sendMessage(ColorUtils.spigotColor(Placeholder.apply((String) value, placeholders)));
    }

}
