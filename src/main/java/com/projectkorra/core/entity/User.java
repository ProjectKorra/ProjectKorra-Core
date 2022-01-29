package com.projectkorra.core.entity;

import java.util.UUID;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.type.Bindable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class User<T extends LivingEntity> extends AbilityUser {

	protected final T typed;
	
	User(T entity) {
		super(entity);
		this.typed = entity;
	}
	
	public T getEntity() {
		return typed;
	}
	
	public boolean canBind(Ability ability) {
		return ability instanceof Bindable && hasPermission("bending.ability." + ability.getName());
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
}
