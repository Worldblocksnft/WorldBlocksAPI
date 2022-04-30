package net.worldblocks.libs.worldblocksapi.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
@AllArgsConstructor
public class CommandContext {

    private CommandSender commandSender;
    private String[] args;
    private String label;
}
