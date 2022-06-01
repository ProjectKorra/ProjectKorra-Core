package com.projectkorra.core.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.skill.Skill;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SkillChooseCommand extends PKSubCommand {

	public SkillChooseCommand() {
		super("choose", "Choose a user's skills", "/projectkorra choose [skill] <user>", Arrays.asList("ch"));
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
			if (!hasPermission(sender, "choose.others")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to choose skills for others.");
				return;
			}

			user = UserManager.from(Bukkit.getPlayer(args[1]));

			if (user == null) {
				sender.sendMessage(ChatColor.RED + "No player found by name '" + ChatColor.GOLD + args[1] + ChatColor.RED + "'");
				return;
			}
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This is a player only command argument.");
				return;
			}

			user = UserManager.from((Player) sender);
		}

		if (!user.getSkills().isEmpty() && !hasPermission(sender, "rechoose")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to rechoose skills.");
			return;
		}

		Optional<Skill> skill = Skill.of(args[0]);

		if (!skill.isPresent()) {
			sender.sendMessage(ChatColor.RED + "No skill found from '" + ChatColor.GOLD + args[0] + ChatColor.RED + "'");
			return;
		} else if (!skill.get().getParents().isEmpty()) {
			sender.sendMessage(ChatColor.RED + "Cannot choose a subskill, must choose a parent!");
			return;
		} else if (!user.hasPermission("projectkorra.skill." + skill.get().getInternalName())) {
			sender.sendMessage(ChatColor.RED + "User does not have permission to use that skill!");
			return;
		}

		Set<Skill> toAdd = new HashSet<>();
		toAdd.add(skill.get());
		for (Skill child : skill.get().getChildren()) {
			if (user.hasPermission("projectkorra.skill." + child.getInternalName())) {
				toAdd.add(child);
			}
		}

		user.setSkills(toAdd);
		sender.sendMessage(ChatColor.GOLD + "You have chosen " + skill.get().getDisplay().getColoredNoun());
	}

	@Override
	public List<String> tabComplete(CommandSender sender, int argCount) {
		if (argCount == 1) {
			return TabCompleteList.skills(true);
		} else if (argCount == 2) {
			return TabCompleteList.onlinePlayers();
		}

		return null;
	}

}
