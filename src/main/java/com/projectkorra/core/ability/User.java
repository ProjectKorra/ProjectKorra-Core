package com.projectkorra.core.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.util.Pair;
import com.projectkorra.core.game.InputType;

public abstract class User {

	private List<Pair<AbilityInfo, Activation>> activations = new ArrayList<>();
	private List<Ability> instances = new ArrayList<>();
	private Map<AbilityInfo, Cooldown> cooldowns = new HashMap<>();
	private Map<InputType, Pair<Boolean, Event>> inputs = new HashMap<>();
	private Entity entity;

	public User(Entity entity) {
		this.entity = entity;
		for (InputType t : InputType.values()) {
			inputs.put(t, new Pair<>(false, null));
		}
	}

	public abstract AbilityInfo getCurrentBind();

	protected final void does(InputType p, Event e) {
		this.inputs.put(p, new Pair<>(true, e));
	}

	public final boolean did(InputType p) {
		return this.inputs.get(p).getKey();
	}

	public final boolean canBend(AbilityInfo info) {
		return true;
	}

	public final void addCooldown(AbilityInfo info, long cooldown) {
		Cooldown c = new Cooldown(cooldown);
		c.addCooldown();
		cooldowns.put(info, c);
	}

	public final boolean onCooldown(AbilityInfo info) {
		Cooldown cooldown = cooldowns.get(info);
		if (cooldown == null) {
			return false;
		} else if (System.currentTimeMillis() - cooldown.getCooldown() >= cooldown.getChecked()) {
			return false;
		} else {
			return true;
		}
	}

	public Entity getEntity() {
		return entity;
	}

	protected final Map<InputType, Pair<Boolean, Event>> getInputs() {
		return inputs;
	}

	protected final List<Pair<AbilityInfo, Activation>> getActivations() {
		return activations;
	}

	protected final List<Ability> getInstances() {
		return instances;
	}

	private final class Cooldown {
		private long checked;
		private long cooldown;

		public Cooldown(long cooldown) {
			this.cooldown = cooldown;
		}

		public void addCooldown() {
			this.checked = System.currentTimeMillis();
		}

		public long getCooldown() {
			return this.cooldown;
		}

		public long getChecked() {
			return this.checked;
		}
	}

	public final boolean toggled() {
		return false;
	}
}
