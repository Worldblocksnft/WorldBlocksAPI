package world.worldblocks.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public abstract class AbstractSubCommand {


    private final List<String> aliasList;
    private final String usage;

    @Setter
    private String permission;

    @Setter
    private boolean player;

    public AbstractSubCommand(String usage, List<String> aliases) {
        this.usage = usage;
        this.aliasList = aliases;
    }

    public AbstractSubCommand(String usage, String permission, List<String> aliases) {
        this(usage, aliases);
        this.permission = permission;
    }

    public abstract void perform(CommandContext commandContext);
}
