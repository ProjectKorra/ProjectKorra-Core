package com.projectkorra.core.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;

public abstract class User<T extends LivingEntity> extends AbilityUser {

	protected final T typed;

	User(T entity) {
		super(entity);
		this.typed = entity;
	}

	public T getEntity() {
		return typed;
	}

	@Override
	public final UUID getUniqueID() {
		return typed.getUniqueId();
	}

	@Override
	public Location getEyeLocation() {
		return typed.getEyeLocation();
	}

	@Override
	public Location getLocation() {
		return typed.getLocation();
	}

	@Override
	public Vector getDirection() {
		return typed.getLocation().getDirection();
	}

	@Override
	public Optional<Entity> getTargetEntity(double range, double raySize, FluidCollisionMode fluid, Predicate<Entity> filter) {
		Location eye = getEyeLocation();
		return Optional.ofNullable(eye.getWorld().rayTrace(eye, eye.getDirection(), range, fluid, true, raySize, filter)).map(RayTraceResult::getHitEntity);
	}
}
