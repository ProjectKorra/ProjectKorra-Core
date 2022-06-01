package com.projectkorra.core.skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.projectkorra.core.util.text.DisplayVariant;

import org.apache.commons.lang.Validate;

import net.md_5.bungee.api.ChatColor;

public class Skill {

	private static final Map<String, Skill> SKILLS = new HashMap<>();

	public static final Skill AIRBENDING = registerCore("airbending", ChatColor.of("#fcdc7b"));
	public static final Skill EARTHBENDING = registerCore("earthbending", ChatColor.of("#47b44f"));
	public static final Skill FIREBENDING = registerCore("firebending", ChatColor.of("#a10000"));
	public static final Skill WATERBENDING = registerCore("waterbending", ChatColor.of("#509bcd"));

	public static final Skill CHIBLOCKING = registerCore("chiblocking", ChatColor.GRAY);
	public static final Skill ENERGYBENDING = registerCore("energybending", ChatColor.DARK_PURPLE);

	public static final Skill FLYING = registerCore("flying", ChatColor.GRAY, AIRBENDING);
	public static final Skill SPIRITUAL = registerCore("spiritualism", ChatColor.of("#fff2cc"), AIRBENDING);

	public static final Skill LAVABENDING = registerCore("lavabending", ChatColor.of("#f74900"), EARTHBENDING);
	public static final Skill METALBENDING = registerCore("metalbending", ChatColor.of("#999999"), EARTHBENDING);
	public static final Skill SANDBENDING = registerCore("sandbending", ChatColor.of("#ffe599"), EARTHBENDING);

	public static final Skill COMBUSTIONBENDING = registerCore("combustionbending", ChatColor.DARK_RED, FIREBENDING);
	public static final Skill LIGHTNINGBENDING = registerCore("lightningbending", ChatColor.of("#71d9de"), FIREBENDING);
	public static final Skill BLUEFIREBENDING = registerCore("bluefirebending", ChatColor.BLUE, FIREBENDING);

	public static final Skill BLOODBENDING = registerCore("bloodbending", ChatColor.of("#e06666"), WATERBENDING);
	public static final Skill HEALING = registerCore("healing", ChatColor.of("#00ffff"), WATERBENDING);
	public static final Skill PLANTBENDING = registerCore("plantbending", ChatColor.of("#93c47d"), WATERBENDING);

	public static final Skill MUDBENDING = registerCore("mudbending", ChatColor.of("#783f04"), EARTHBENDING, WATERBENDING);
	public static final Skill PHYSIQUE = registerCore("physique", ChatColor.of("#a64d79"), AIRBENDING, EARTHBENDING, FIREBENDING, WATERBENDING);

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
	Skill(String name, DisplayVariant display, String description) {
		this.name = name.toLowerCase();
		this.display = display;
		this.description = description;
	}

	/**
	 * Returns the internal name of this skill, which is lowercased
	 * 
	 * @return internal skill name
	 */
	public String getInternalName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public DisplayVariant getDisplay() {
		return display;
	}

	public ImmutableSet<Skill> getChildren() {
		return children;
	}

	public ImmutableSet<Skill> getParents() {
		return parents;
	}

	public static Optional<Skill> of(String label) {
		return Optional.ofNullable(SKILLS.get(label.toLowerCase()));
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
	public static Skill register(String name, DisplayVariant display, String description, Skill... parents) {
		Validate.notEmpty(name, "Skill name cannot be empty!");
		Validate.notNull(display, "Attempted to register Skill with null display");
		Validate.isTrue(display.isValid(), "Attempted to register Skill with invalid display (data inside the display is null)");
		Validate.isTrue(!SKILLS.containsKey(name.toLowerCase()), "Attempted to register Skill with pre-existing name");
		Validate.isTrue(!SKILLS.containsKey(display.getNoun().toLowerCase()), "Attempted to register Skill with pre-existing display name");

		Skill skill = new Skill(name, display, description);
		SKILLS.put(display.getNoun().toLowerCase(), skill);
		SKILLS.put(name.toLowerCase(), skill);

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

	/*
	 * public static Skill register(String name, Config config, Skill...parents) {
	 * Validate.notNull(config, "Cannot register skill with null config");
	 * 
	 * String noun = config.getValue(c -> c.getString("noun")); String verb =
	 * config.getValue(c -> c.getString("verb")); String user = config.getValue(c ->
	 * c.getString("user")); String desc = config.getValue(c ->
	 * c.getString("description")); ChatColor color = ChatColor.of(config.getValue(c
	 * -> c.getString("color")));
	 * 
	 * return register(name, new DisplayVariant(noun, verb, user, color), desc,
	 * parents); }
	 */

	private static Skill registerCore(String name, ChatColor color, Skill... parents) {
		return register(name, new DisplayVariant(name, name, name, color), name, parents);
	}
}
