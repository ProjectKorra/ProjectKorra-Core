package com.projectkorra.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.skill.Skill;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SkillAddCommand extends PKSubCommand {

	public SkillAddCommand() {
		super("add", "Add a skill to a user", "/projectkorra add <skill> [user]", Arrays.asList("a"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "Specify a skill!");
			return;
		} else if (args.length < 1 || args.length > 2) {
			sender.sendMessage(ChatColor.RED + "Incorrect amount of arguments given: " + getUsage());
			return;
		}

		AbilityUser user;

		if (args.length == 2) {
			if (!hasPermission(sender, "add.others")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to add skills to others.");
				return;
			}

			user = UserManager.from(Bukkit.getPlayer(args[1])).get();

			if (user == null) {
				sender.sendMessage(ChatColor.RED + "No player found by name '" + ChatColor.GOLD + args[1] + ChatColor.RED + "'");
				return;
			}
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This is a player only command argument.");
				return;
			}

			user = UserManager.from((Player) sender).get();
		}

		Optional<Skill> skill = Skill.of(args[0]);

		if (!skill.isPresent()) {
			sender.sendMessage(ChatColor.RED + "No skill found from '" + ChatColor.GOLD + args[0] + ChatColor.RED + "'");
			return;
		} else if (!skill.get().getParents().isEmpty()) {
			boolean hasParent = false;
			for (Skill parent : skill.get().getParents()) {
				hasParent |= user.hasSkill(parent);
			}

			if (!hasParent) {
				sender.sendMessage(ChatColor.RED + "You don't have any of the parent skills required for that subskill!");
				return;
			}
		}

		user.addSkill(skill.get());
		sender.sendMessage(ChatColor.GOLD + "You have added " + skill.get().getDisplay().getColoredNoun());
	}

	@Override
	public List<String> tabComplete(CommandSender sender, int argCount) {
		if (argCount == 1) {
			return TabCompleteList.skills(false);
		} else if (argCount == 2) {
			return TabCompleteList.onlinePlayers();
		}

		return null;
	}

}
