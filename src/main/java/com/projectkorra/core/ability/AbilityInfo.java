package com.projectkorra.core.ability;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	public AbilityInfo(String author, String version, String name, String configPath, boolean bindable, Skill... skills) {
		this.author = author;
		this.version = version;
		this.name = name;
		this.bindable = bindable;
		this.skills = Collections.unmodifiableList(Arrays.asList(skills));
		this.configPath = configPath;
		AbilityManager.infos.add(this);
		this.load();
	}

	public abstract Activation getActivation();
	public abstract void load();
	public abstract Ability createInstance(BendingUser user);
}
