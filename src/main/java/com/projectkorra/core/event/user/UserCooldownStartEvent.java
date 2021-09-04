package com.projectkorra.core.event.user;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityUser;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserCooldownStartEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private AbilityUser user;
    private Ability ability;
    private long duration;

    public UserCooldownStartEvent(AbilityUser user, Ability ability, long duration) {
        this.user = user;
        this.ability = ability;
        this.duration = duration;
    }

    public AbilityUser getUser() {
        return user;
    }

    public Ability getAbility() {
        return ability;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
