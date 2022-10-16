package com.projectkorra.core.skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.util.configuration.Configurable;
import com.projectkorra.core.util.effect.Effect;
import com.projectkorra.core.util.effect.Effect.EffectBuilder;
import com.projectkorra.core.util.effect.ParticleData;
import com.projectkorra.core.util.effect.SoundData;
import com.projectkorra.core.util.text.DisplayVariant;

public abstract class Skill implements Configurable {

	private static final Map<String, Skill> SKILLS = new HashMap<>();

	private DisplayVariant display;
	private String name, description;
	private ImmutableSet<Skill> parents, children;

	/**
	 * A skill is something that entities can do, common examples of this are
	 * elemental bending, martial arts, weapon using, and spirit powers.
	 * 
	 * @param display     {@link DisplayVariant} for this skill
	 * @param description What this skill enables players to do
	 */
	protected Skill(String name, DisplayVariant display, String description) {
		this.name = name.toLowerCase();
		this.display = display;
		this.description = description;
	}

	/**
	 * Returns the internal name of this skill, which is lowercased
	 * 
	 * @return internal skill name
	 */
	public final String getInternalName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	public final DisplayVariant getDisplay() {
		return display;
	}

	public final ImmutableSet<Skill> getChildren() {
		return children;
	}

	public final ImmutableSet<Skill> getParents() {
		return parents;
	}

	public static Skill of(String label) {
		return SKILLS.get(label.toLowerCase());
	}

	public static Set<Skill> values() {
		return new HashSet<>(SKILLS.values());
	}

	/**
	 * Attempt to register a new skill with the given parameters
	 * 
	 * @param name        the internal name for the skill
	 * @param display     how the skill will be displayed
	 * @param description description of the skill
	 * @param parents     which skills this one derives from
	 * @return newly registered skill
	 */
	public static Skill register(Skill skill, Skill...parents) {
		Validate.notNull(skill, "Cannot register null skill");
		Validate.notEmpty(skill.name, "Skill name cannot be empty!");
		Validate.notNull(skill.display, "Attempted to register Skill with null display");
		Validate.isTrue(skill.display.isValid(), "Attempted to register Skill with invalid display (data inside the display is null)");
		Validate.isTrue(!SKILLS.containsKey(skill.name.toLowerCase()), "Attempted to register Skill with pre-existing name");
		Validate.isTrue(!SKILLS.containsKey(skill.display.getNoun().toLowerCase()), "Attempted to register Skill with pre-existing display name");

		SKILLS.put(skill.display.getNoun().toLowerCase(), skill);
		SKILLS.put(skill.name.toLowerCase(), skill);

		for (Skill parent : parents) {
			if (parent == null) {
				continue;
			}

			parent.children = new ImmutableSet.Builder<Skill>().addAll(parent.children).add(skill).build();
		}

		skill.parents = ImmutableSet.copyOf(parents);
		skill.children = ImmutableSet.of();

		return skill;
	}

	@Override
	public String getFileName() {
		return name;
	}

	@Override
	public String getFolderName() {
		return "skills";
	}
	
	public abstract ParticleData getParticle(AbilityUser user);
	public abstract SoundData getSound(AbilityUser user);
	
	public EffectBuilder getParticleEffect(AbilityUser user) {
		return Effect.builder().add(getParticle(user));
	}
	
	public EffectBuilder getSoundEffect(AbilityUser user) {
		return Effect.builder().add(getSound(user));
	}
	
	public EffectBuilder getEffectBuilder(AbilityUser user) {
		return Effect.builder().add(getParticle(user)).add(getSound(user));
	}
}
