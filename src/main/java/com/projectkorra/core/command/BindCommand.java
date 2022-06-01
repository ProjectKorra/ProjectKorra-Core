package com.projectkorra.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.entity.PlayerUser;

import net.md_5.bungee.api.ChatColor;

public class BindCommand extends PKSubCommand {

	public BindCommand() {
		super("bind", "Bind an ability to your hotbar", "/projectkorra bind <ability> [slot]", Arrays.asList("b"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Player only command!");
			return;
		} else if (args.length < 1 || args.length > 2) {
			sender.sendMessage(ChatColor.RED + "Incomprehensible, expected: " + ChatColor.GOLD + this.getUsage());
			return;
		}

		Player player = (Player) sender;
		PlayerUser user = UserManager.getAs(player.getUniqueId(), PlayerUser.class);
		if (user == null) {
			sender.sendMessage(ChatColor.RED + "Something went wrong retrieving your user!");
			return;
		}

		Optional<Ability> ability = AbilityManager.getAbility(args[0]);
		if (!ability.isPresent()) {
			sender.sendMessage(ChatColor.RED + "No ability found from '" + ChatColor.GOLD + args[0] + ChatColor.RED + "'");
			return;
		} else if (!user.canBind(ability.get())) {
			sender.sendMessage(ChatColor.RED + "Cannot bind the ability! Check if you have the ability's skill and permission node!");
			return;
		}

		int slot = user.getCurrentSlot();
		if (args.length == 2) {
			slot = Integer.parseInt(args[1]) - 1;
		}

		if (slot < 0 || slot > 8) {
			sender.sendMessage(ChatColor.RED + "Ability slots are only 1-9 on the hotbar!");
			return;
		}

		user.bindAbility(slot, ability.get());
		sender.sendMessage(ChatColor.GOLD + "Successfully bound " + ability.get().getDisplay() + ChatColor.GOLD + " to slot " + (slot + 1));
	}

	@Override
	public List<String> tabComplete(CommandSender sender, int argCount) {
		if (argCount == 1) {
			if (!(sender instanceof Player)) {
				return null;
			}

			return TabCompleteList.bindables(UserManager.get(((Player) sender).getUniqueId()));
		} else if (argCount == 2) {
			return TabCompleteList.slots();
		}

		return null;
	}

}
