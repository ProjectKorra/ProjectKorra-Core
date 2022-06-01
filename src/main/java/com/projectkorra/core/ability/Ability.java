package com.projectkorra.core.ability;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.configuration.Configurable;

import net.md_5.bungee.api.ChatColor;

public abstract class Ability implements Configurable, Listener {

	private String name, description, author, version;
	private Skill skill;

	public Ability(String name, String description, String author, String version, Skill skill) {
		this.name = name;
		this.description = description;
		this.author = author;
		this.version = version;
		this.skill = skill;
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
	public String getFileName() {
		return name;
	}

	@Override
	public String getFolderName() {
		return skill.getInternalName();
	}

	@Override
	public void postProcessed() {
	}

	/**
	 * Called when an activation attempt for this ability is made by the given user
	 * for the trigger.
	 * 
	 * @param user
	 * @param trigger
	 * @param provider can be null depending on the trigger
	 * @return
	 */
	protected abstract AbilityInstance activate(AbilityUser user, Activation trigger, Event provider);

	protected void onRegister() {
	};

	public boolean canActivate(AbilityUser user, Activation trigger) {
		return user.hasSkill(getSkill());
	}
}
