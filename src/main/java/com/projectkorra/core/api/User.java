package com.projectkorra.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.projectkorra.core.util.Pair;
import com.projectkorra.core.api.game.Input;

public abstract class User {

	protected List<Pair<AbilityInfo, Sequence<?>>> activations = new ArrayList<>(20);
	protected List<AbilityInstance> instances = new ArrayList<>();
	protected Map<AbilityInfo, Cooldown> cooldowns = new HashMap<>(20);
	protected Entity entity;

	private Map<Input, Pair<Boolean, Event>> inputs = new HashMap<>(30);
	private boolean toggledBindable = false;
	private boolean toggledNonBindable = false;

	public AbilityInfo getInfo(Sequence<?> a) {
		for (Pair<AbilityInfo, Sequence<?>> e : activations) {
			if (e.getValue() == a) {

			}
		}
		return null;
	}

	public User(Entity entity) {
		this.entity = entity;
		for (Input t : Input.values) {
			inputs.put(t, new Pair<>(false, null));
		}
	}

	protected final void does(Input p, Event e) {
		this.inputs.put(p, new Pair<>(true, e));
	}

	public final boolean did(Input p) {
		return this.inputs.get(p).getKey();
	}

	public final boolean canBend(AbilityInfo info) {
		return true;
	}

	protected final void addCooldown(AbilityInfo info, long cooldown) {
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

	protected final Map<Input, Pair<Boolean, Event>> getInputs() {
		return inputs;
	}

	protected final List<AbilityInstance> getInstances() {
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

	public abstract AbilityInfo getCurrentBind();

	protected abstract List<Pair<AbilityInfo, Sequence<?>>> getSequences(boolean current);

	public abstract List<AbilityInfo> getBinds();

	public List<AbilityInstance> getInstances(AbilityInfo info) {
		return instances.stream().filter(i -> i.getInfo().equals(info)).toList();
	}

	public List<AbilityInstance> apply(AbilityInfo info, Consumer<AbilityInstance> action) {
		return instances.stream().filter(i -> i.getInfo().equals(info)).peek(action).toList();
	}

}
