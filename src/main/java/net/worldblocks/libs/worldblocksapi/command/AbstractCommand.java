package net.worldblocks.libs.worldblocksapi.command;

import net.worldblocks.libs.worldblocksapi.message.LibMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand extends Command {

    private boolean requiresPlayer = false;

    private final List<AbstractSubCommand> subCommandList;

    public AbstractCommand(String name) {
        super(name);
        this.subCommandList = new ArrayList<>();
    }

    public abstract void perform(CommandContext commandContext);

    public boolean execute(CommandContext commandContext) {
        if (commandContext.getArgs().length > 0) {
            for (AbstractSubCommand abstractSubCommand : subCommandList) {
                if (!abstractSubCommand.getAliasList().contains(commandContext.getArgs()[0])) {
                    continue;
                }

                if (abstractSubCommand.getPermission() != null && !commandContext.getCommandSender().hasPermission(abstractSubCommand.getPermission())) {
                    LibMessages.NO_PERMISSION.send(commandContext.getCommandSender());
                    return false;
                }

                if (abstractSubCommand.isPlayer() && !(commandContext.getCommandSender() instanceof Player)) {
                    LibMessages.PLAYERS_ONLY.send(commandContext.getCommandSender());
                    return false;
                }

                abstractSubCommand.perform(commandContext);
                return true;
            }
        }

        if (getPermission() != null && !commandContext.getCommandSender().hasPermission(getPermission())) {
            LibMessages.NO_PERMISSION.send(commandContext.getCommandSender());
            return false;
        }

        if (requiresPlayer && !(commandContext.getCommandSender() instanceof Player)) {
            LibMessages.PLAYERS_ONLY.send(commandContext.getCommandSender());
            return false;
        }

        perform(commandContext);
        return true;
    }

    public void setRequiresPlayer(boolean requiresPlayer) {
        this.requiresPlayer = requiresPlayer;
    }

    public void addSubCommands(AbstractSubCommand... abstractSubCommands) {
        subCommandList.addAll(Arrays.asList(abstractSubCommands));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return this.execute(new CommandContext(sender, args, commandLabel));
    }

}
