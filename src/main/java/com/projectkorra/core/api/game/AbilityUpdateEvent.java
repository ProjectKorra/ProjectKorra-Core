package com.projectkorra.core.api.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.projectkorra.core.api.Ability;
import com.projectkorra.core.api.AbilityManager;
import com.projectkorra.core.api.User;

public class AbilityUpdateEvent extends BlockBreakEvent {

    private User user;
    private Ability ability;

    public AbilityUpdateEvent(Block theBlock, User player, Ability ability) {
        super(theBlock, player.getEntity() instanceof Player ? (Player) player.getEntity() : null);
        this.user = player;
        this.ability = ability;
    }

    @Override
    public void setCancelled(boolean cancel) {
        super.setCancelled(cancel);
        AbilityManager.flagRemoval(user, ability, cancel);
    }

}
