package com.projectkorra.core.system.ability;

import java.io.File;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.configuration.Configurable;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public abstract class Ability implements Configurable, Listener {
	
	private String name, description, author, version;
	private Skill skill;
	private File file;
	
	public Ability(String name, String description, String author, String version, Skill skill) {
		this.name = name;
		this.description = description;
		this.author = author;
		this.version = version;
		this.skill = skill;
		this.file = new File(JavaPlugin.getPlugin(ProjectKorra.class).getDataFolder(), "/configuration/abilities/" + name + ".yml");
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public final String getAuthor() {
		return author;
	}

	public final String getVersion() {
		return version;
	}

	public final Skill getSkill() {
		return skill;
	}

	public ChatColor getDisplayColor() {
		return skill.getDisplay().getColor();
	}

	public String getDisplay() {
		return getDisplayColor() + name;
	}
	
	@Override
	public final File getFile() {
		return file;
	}

	protected abstract AbilityInstance activate(AbilityUser user, Activation trigger, Event provider);
	public abstract boolean uses(Activation trigger);
	public abstract ImmutableSet<Class<? extends AbilityInstance>> instanceClasses();
	public abstract ImmutableSet<String> getCooldownTags();
	public abstract boolean allowSourcing();
	
	public boolean canActivate(AbilityUser user, Activation trigger) {
		return uses(trigger) && user.hasSkill(getSkill());
	}
}
