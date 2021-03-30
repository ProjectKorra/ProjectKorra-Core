package com.projectkorra.core.system.user;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.type.Bindable;
import com.projectkorra.core.system.skill.Skill;

public abstract class SkilledEntity<T extends LivingEntity> extends AbilityUser {

	protected final T entity;
	private AbilityBinds binds;
	
	SkilledEntity(T entity, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds) {
		super(skills, toggled);
		this.entity = entity;
		this.binds = AbilityBinds.copyOf(binds);
	}
	
	public abstract int getCurrentSlot();
	public abstract void sendMessage(String message);
	public abstract boolean hasPermission(String perm);
	
	public T getEntity() {
		return entity;
	}
	
	public boolean canBind(Ability ability) {
		return ability instanceof Bindable && this.hasPermission("bending.ability." + ability.getName());
	}
	
	@Override
	public Ability getBoundAbility() {
		return binds.get(getCurrentSlot()).orElse(null);
	}
	
	@Override
	public Location getLocation() {
		return entity.getEyeLocation();
	}
	
	@Override
	public Vector getDirection() {
		return entity.getLocation().getDirection();
	}
	
	@Override
	public UUID getUniqueID() {
		return entity.getUniqueId();
	}
}
