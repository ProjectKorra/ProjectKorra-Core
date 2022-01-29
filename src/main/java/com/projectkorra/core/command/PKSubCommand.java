package com.projectkorra.core.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class PKSubCommand {
    
    private String name, description, usage;
    private List<String> aliases;

    protected PKSubCommand(String name, String description, String usage, List<String> aliases) {
        this.name = name.toLowerCase();
        this.description = description;
        this.usage = usage;
        this.aliases = new ArrayList<>(aliases);
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final String getUsage() {
        return usage;
    }

    public final List<String> getAliases() {
        return new ArrayList<>(aliases);
    }

    /**
     * Check if the sender has the given permission
     * @param sender Who to check for permission
     * @param afterCommand the part of the permission that will go after <code>projectkorra.command.[command name].</code>
     * @return true if the sender has permission
     */
    public final boolean hasPermission(CommandSender sender, String afterCommand) {
        return sender.hasPermission("projectkorra.command." + name + ".");
    } 

    /**
     * Execute this command for the given command sender and arguments.
     * @param sender Who is using the command
     * @param args Command arguments after this command name
     */
    public abstract void execute(CommandSender sender, String[] args);
    public abstract List<String> tabComplete(CommandSender sender, String[] args);

}
