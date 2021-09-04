package com.projectkorra.core.event.user;

import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.Cooldown;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserCooldownEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private AbilityUser user;
    private Cooldown cooldown;

    public UserCooldownEndEvent(AbilityUser user, Cooldown cooldown) {
        this.user = user;
        this.cooldown = cooldown;
    }

    public AbilityUser getUser() {
        return user;
    }

    public Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
