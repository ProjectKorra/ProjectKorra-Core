package com.projectkorra.core.system.entity;

import java.util.Collection;
import java.util.UUID;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.type.Bindable;
import com.projectkorra.core.system.skill.Skill;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class User<T extends LivingEntity> extends AbilityUser {

	protected final T entity;
	
	User(T entity, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds) {
		super(skills, toggled, binds);
		this.entity = entity;
	}
	
	public abstract void sendMessage(String message);
	public abstract boolean hasPermission(String perm);
	
	public T getEntity() {
		return entity;
	}
	
	public boolean canBind(Ability ability) {
		return ability instanceof Bindable && this.hasPermission("bending.ability." + ability.getName());
	}
	
	@Override
	public final UUID getUniqueID() {
		return entity.getUniqueId();
	}
	
	@Override
	public Location getLocation() {
		return entity.getEyeLocation();
	}
	
	@Override
	public Vector getDirection() {
		return entity.getLocation().getDirection();
	}
}
