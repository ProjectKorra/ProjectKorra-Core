package com.projectkorra.core.ability;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.skills.Skill;

public abstract class AbilityInfo {
	public final List<Skill> skills;
	public final String author;
	public final String version;
	public final String name;
	public final boolean bindable;
	public final String configPath;
	public int priority = 1;

	public AbilityInfo(String author, String version, String name, boolean bindable, Skill... skills) {
		this(author, version, name, "DEFAULT", bindable, skills);
	}

	public AbilityInfo(String author, String version, String name, String configPath, boolean bindable,
			Skill... skills) {
		this.author = author;
		this.version = version;
		this.name = name;
		this.bindable = bindable;
		this.skills = Collections.unmodifiableList(Arrays.asList(skills));
		this.configPath = configPath;
		if (AbilityManager.infos.stream().filter(i -> i.name == this.name).toList().size() < 1) {
			AbilityManager.infos.add(this);
			this.load();
		} else {
			// TODO: add appropriate method later
			ProjectKorra.plugin.getLogger().log(Level.SEVERE,
					this.name + "'s info is already registered! Please use *INSERT APPROPRIATE METHOD");
		}
	}

	public abstract Activation getActivation();

	public abstract void load();

	public abstract Ability createInstance(User user);
}
