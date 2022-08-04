package com.projectkorra.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.md_5.bungee.api.ChatColor;

public class PKCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("projectkorra")) {
			return false;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "Use [/projectkorra help] to see a list of commands");
			return true;
		}

		PKSubCommand cmd = CommandRegistry.from(args[0]).orElse(null);

		if (cmd == null) {
			sender.sendMessage(ChatColor.RED + "No subcommand found from '" + ChatColor.GOLD + args[0] + ChatColor.RED + "'");
			return true;
		}

		if (!sender.hasPermission("projectkorra.command." + cmd.getName())) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
			return true;
		}

		cmd.execute(sender, Arrays.copyOfRange(args, 1, args.length));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase("projectkorra")) {
			return null;
		}
		
		if (args.length == 1) {
			return CommandRegistry.registered().stream().filter((s) -> s.toLowerCase().contains(args[0].toLowerCase())).collect(Collectors.toList());
		}

		PKSubCommand cmd = CommandRegistry.from(args[0]).orElse(null);

		if (cmd == null) {
			return null;
		}

		List<String> list = TabCompleteList.filter(cmd.tabComplete(sender, args.length - 1), args[args.length - 1]);
		if (list == null || list.isEmpty()) {
			list = Arrays.asList("Unexpected argument at index " + (args.length - 1) + ": '" + args[args.length - 1] + "'");
		}

		return list;
	}
}
