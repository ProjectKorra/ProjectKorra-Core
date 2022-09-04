package com.projectkorra.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.projectkorra.core.util.Pair;
import com.projectkorra.core.api.activation.Activation;
import com.projectkorra.core.api.game.InputType;

public abstract class User {

	private List<Pair<AbilityInfo, Activation>> activations = new ArrayList<>(20);
	private List<Ability> instances = new ArrayList<>();
	private Map<AbilityInfo, Cooldown> cooldowns = new HashMap<>(20);
	private Map<InputType, Pair<Boolean, Event>> inputs = new HashMap<>();
	private Entity entity;

	private boolean toggledBindable = false;
	private boolean toggledNonBindable = false;

	public User(Entity entity) {
		this.entity = entity;
		for (InputType t : InputType.values) {
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

	private final void addCooldown(AbilityInfo info, long cooldown) {
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

	protected final List<Pair<AbilityInfo, Activation>> getActivations(boolean current) {
		if (current) {
			List<Pair<AbilityInfo, Activation>> filtered = new ArrayList<>();
			for (Pair<AbilityInfo, Activation> e : activations) {
				if (e.getKey() == this.getCurrentBind() || !e.getKey().bindable) {
					filtered.add(e);
				}
			}
			return filtered;
		} else {
			return activations;
		}
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

	public final boolean toggledAll() {
		return toggledBindable && toggledNonBindable;
	}

	public final boolean toggledNonBindable() {
		return !toggledBindable && toggledBindable;
	}

	public final boolean toggledBindable() {
		return toggledBindable && !toggledNonBindable;
	}

	public final void toggleNonBindable(boolean on) {
		this.toggledNonBindable = on;
	}

	public final void toggleBindable(boolean on) {
		this.toggledBindable = on;

	}

	public final void toggleAll(boolean on) {
		toggleBindable(on);
		toggleNonBindable(on);
	}

	public final void toggleBindable() {
		toggleBindable(!this.toggledBindable);
	}

	public final void toggleNonBindable() {
		toggleNonBindable(!this.toggledNonBindable);
	}

	public final void toggleAll() {
		toggleNonBindable();
		toggleBindable();
	}

	public Object getBinds() {
		return null;
	}

}
