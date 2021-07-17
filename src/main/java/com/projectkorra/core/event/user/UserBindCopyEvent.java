package com.projectkorra.core.event.user;

import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.ability.AbilityUser;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserBindCopyEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private AbilityUser user;
    private AbilityBinds copying;

    public UserBindCopyEvent(AbilityUser user, AbilityBinds copying) {
        this.user = user;
        this.copying = copying;
    }

    public AbilityUser getUser() {
        return user;
    }

    public AbilityBinds getCopying() {
        return copying;
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