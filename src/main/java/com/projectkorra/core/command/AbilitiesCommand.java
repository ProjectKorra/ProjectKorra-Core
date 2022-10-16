package com.projectkorra.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.skill.Skill;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Text;

public class AbilitiesCommand extends PKSubCommand {

	public AbilitiesCommand() {
		super("abilities", "Show the abilities for a skill", "/pk abilities <skill>", Arrays.asList("abils", "display", "list", "catalog"));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many args given: " + getUsage());
			return;
		}
		
		Skill skill = Skill.of(args[0]);
		
		if (skill == null) {
			sender.sendMessage(ChatColor.RED + "No skill found from '" + ChatColor.GOLD + args[0] + ChatColor.RED + "'");
			return;
		}
		
		Set<Ability> abils = AbilityManager.getAbilities(skill);
		
		if (abils.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "No " + skill.getDisplay().getColoredNoun() + ChatColor.RED + " abilities were found!");
			return;
		}
		
		ComponentBuilder builder = new ComponentBuilder(skill.getDisplay().getNoun().toUpperCase()).color(skill.getDisplay().getColor()).bold(true);
		
		for (Ability ability : abils) {
			if (sender.hasPermission("projectkorra.ability." + ability.getName())) {
				builder.append("\n" + ability.getName(), FormatRetention.NONE).color(ability.getDisplayColor()).event(new HoverEvent(Action.SHOW_TEXT, new Text(ability.getDescription())));
				
				if (ability instanceof Bindable) {
					builder.append(" ", FormatRetention.NONE).append("[bind]", FormatRetention.NONE)
					.event(new HoverEvent(Action.SHOW_TEXT, new Text("Click to bind this ability to your current slot")))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pk bind " + ability.getName()));
				}
			}
		}
		
		sender.spigot().sendMessage(builder.create());
	}

	@Override
	public List<String> tabComplete(CommandSender sender, int argCount) {
		if (argCount == 1) {
			return TabCompleteList.skills(false);
		}
		
		return null;
	}

	
}
