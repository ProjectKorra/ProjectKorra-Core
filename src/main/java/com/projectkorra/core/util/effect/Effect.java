package com.projectkorra.core.util.effect;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.World;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.event.effect.EffectBuildEvent;
import com.projectkorra.core.util.Events;

public class Effect implements Spawnable {
	
	private Set<Spawnable> spawns;
	
	private Effect(Set<Spawnable> copy) {
		this.spawns = new HashSet<>(copy);
	}
	
	private Effect(EffectBuilder build) {
		this.spawns = build.spawns;
	}

	@Override
	public void spawn(Location loc) {
		spawns.forEach((effect) -> effect.spawn(loc));
	}

	@Override
	public void spawn(World world, double x, double y, double z) {
		this.spawn(new Location(world, x, y, z));
	}
	
	public static EffectBuilder builder() {
		return new EffectBuilder();
	}
	
	public static class EffectBuilder {
		
		private Set<Spawnable> spawns = new HashSet<>();
		
		public EffectBuilder clear() {
			spawns.clear();
			return this;
		}
		
		public EffectBuilder add(Spawnable spawnable) {
			spawns.add(spawnable);
			return this;
		}
		
		public EffectBuilder remove(Spawnable spawnable) {
			spawns.remove(spawnable);
			return this;
		}
		
		public EffectBuilder contains(Spawnable spawnable) {
			spawns.contains(spawnable);
			return this;
		}
		
		public EffectBuilder removeLike(Predicate<Spawnable> condition) {
			spawns.removeIf(condition);
			return this;
		}
		
		public EffectBuilder apply(Consumer<Spawnable> mod) {
			spawns.forEach(mod);
			return this;
		}
		
		public Effect build(String tag, Optional<AbilityInstance> provider) {
			Events.call(new EffectBuildEvent(this, tag, provider));
			return new Effect(this);
		}
	}
}
