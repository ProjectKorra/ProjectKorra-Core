package com.projectkorra.core.event.user;

import com.projectkorra.core.ability.AbilityUser;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserCooldownStartEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private AbilityUser user;
    private String tag;
    private long duration;

    public UserCooldownStartEvent(AbilityUser user, String tag, long duration) {
        this.user = user;
        this.tag = tag;
        this.duration = duration;
    }

    public AbilityUser getUser() {
        return user;
    }

    public String getTag() {
        return tag;
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
