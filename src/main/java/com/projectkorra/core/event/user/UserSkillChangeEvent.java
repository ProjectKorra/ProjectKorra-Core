package com.projectkorra.core.event.user;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.skill.SkillHolder;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserSkillChangeEvent extends Event {
    
    private static final HandlerList HANDLERS = new HandlerList();

    private SkillHolder holder;
    private Set<Skill> skills;

    public UserSkillChangeEvent(SkillHolder holder, Collection<Skill> skills) {
        this.holder = holder;
        this.skills = new ImmutableSet.Builder<Skill>().addAll(skills).build();
    }

    public SkillHolder getHolder() {
        return holder;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
