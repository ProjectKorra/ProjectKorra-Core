package com.projectkorra.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.projectkorra.core.UserManager;
import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.entity.PlayerUser;
import com.projectkorra.core.skill.Skill;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class WhoCommand extends PKSubCommand {

    protected WhoCommand() {
        super("who", "See information about users", "/projectkorra who [username]", Arrays.asList("whois", "user"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Too many args given: " + getUsage());
            return;
        }

        Player player;
        if (args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return;
            }
        } else if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(ChatColor.RED + "Console is not an ability user!");
            return;
        }

        PlayerUser user = UserManager.get(player.getUniqueId(), PlayerUser.class);
        if (user == null) {
            sender.sendMessage(ChatColor.RED + "Error occured while retrieving user!");
            return;
        }

        List<String> message = new ArrayList<>();

        message.add(ChatColor.GOLD + user.getEntity().getName());
        message.add("Skills: ");
        for (Skill skill : user.getSkills()) {
            message.add("- " + skill.getDisplay().getColoredNoun());
        }

        message.add("Binds: ");
        int i = 0;
        for (Ability ability : user.getBinds()) {
            message.add((++i) + " - " + (ability == null ? "empty" : ability.getDisplay()));
        }

        sender.sendMessage(message.toArray(new String[0]));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
