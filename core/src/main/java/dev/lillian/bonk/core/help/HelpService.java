package dev.lillian.bonk.core.help;

import dev.lillian.bonk.core.command.AbstractCommandRegistry;
import dev.lillian.bonk.core.command.CommandContainer;
import dev.lillian.bonk.core.command.WrappedCommand;
import dev.lillian.bonk.core.executor.CommandExecutor;
import dev.lillian.bonk.core.messages.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HelpService {
    private final AbstractCommandRegistry<?> commandService;
    private HelpFormatter helpFormatter;

    public HelpService(AbstractCommandRegistry<?> commandService) {
        this.commandService = commandService;
        this.helpFormatter = (sender, container) -> {
            sender.sendMessage("--------------------------------");
            sender.sendMessage("Help - " + container.getName());
            for (WrappedCommand c : container.getCommands().values()) {
                sender.sendMessage(container.getName() + (c.getName().length() > 0 ? " " + c.getName() : "") + " " + c.getMostApplicableUsage() + " - " + c.getShortDescription());
            }
            sender.sendMessage("--------------------------------");
        };
    }

    public void sendHelpFor(CommandExecutor<?> sender, CommandContainer container) {
        this.helpFormatter.sendHelpFor(sender, container);
    }

    public void sendUsageMessage(CommandExecutor<?> sender, CommandContainer container, WrappedCommand command) {
        sender.sendMessage(getUsageMessage(container, command));
    }

    public String getUsageMessage(CommandContainer container, WrappedCommand command) {
        String usage = Messages.format(Messages.usageFormat, container.getName()) + " ";
        if (command.getName().length() > 0) {
            usage += command.getName() + " ";
        }
        if (command.getUsage() != null && command.getUsage().length() > 0) {
            usage += command.getUsage();
        } else {
            usage += command.getGeneratedUsage();
        }
        return usage;
    }
}
