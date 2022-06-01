package com.projectkorra.core.util;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.AbilityInstance;

public class Velocity {

	private static final String LOCKED = "velocity_locked";
	private static final String METAKEY = "velocity";

	public static void knockback(Entity entity, Vector velocity, AbilityInstance provider) {
		apply(entity, velocity, provider, false, true);
	}

	public static void move(Entity entity, Vector velocity, AbilityInstance provider) {
		apply(entity, velocity, provider, false, false);
	}

	public static void move(Entity entity, Vector velocity, AbilityInstance provider, boolean lock) {
		apply(entity, velocity, provider, lock, false);
	}

	public static void apply(Entity entity, Vector velocity, AbilityInstance provider, boolean lock, boolean cumulative) {
		if (entity == null || velocity == null) {
			return;
		} else if (entity.hasMetadata(LOCKED) && !entity.getMetadata(LOCKED).get(0).value().equals(provider)) {
			return;
		} else if (lock && !entity.hasMetadata(LOCKED)) {
			entity.setMetadata(LOCKED, new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), provider));
			Threads.runDelayedTask(() -> entity.removeMetadata(LOCKED, JavaPlugin.getPlugin(ProjectKorra.class)), 60);
		}

		velocity = velocity.clone();
		if (cumulative) {
			velocity.add(entity.getVelocity());
		}

		entity.setMetadata(METAKEY, new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), provider));
		Threads.runDelayedTask(() -> entity.removeMetadata(METAKEY, JavaPlugin.getPlugin(ProjectKorra.class)), 60);

		entity.setVelocity(velocity);
	}

	public static boolean isAffected(Entity entity) {
		return entity.hasMetadata(METAKEY);
	}
}
