package com.projectkorra.core.system.entity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.projectkorra.core.event.user.UserDamageEntityEvent;
import com.projectkorra.core.event.user.UserKnockbackEntityEvent;
import com.projectkorra.core.system.ability.Ability;
import com.projectkorra.core.system.ability.AbilityBinds;
import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.type.Bindable;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.util.EventUtil;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public abstract class User<T extends LivingEntity> extends AbilityUser {

	protected final T entity;
	
	User(T entity, Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds) {
		super(skills, toggled, binds);
		this.entity = entity;
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
	public final Optional<Ability> getCurrentAbility() {
		return getBoundAbility(getCurrentSlot());
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
	
	@Override
	public void damage(LivingEntity target, double damage, boolean ignoreArmor, AbilityInstance provider) {
		if (target.getNoDamageTicks() > target.getMaximumNoDamageTicks() / 2.0f && damage <= target.getLastDamage()) {
			return;
		}

		UserDamageEntityEvent event = EventUtil.call(new UserDamageEntityEvent(this, target, damage, ignoreArmor, provider));
		if (event.isCancelled()) {
			return;
		}
		
		damage = event.getDamage();
		
		if (event.doesIgnoreArmor() && damage > 0) {
			double defense = target.getAttribute(Attribute.GENERIC_ARMOR).getValue();
			double toughness = target.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
			damage /= 1 - (Math.min(20, Math.max(defense / 5, defense - 4 * damage / (toughness + 8)))) / 25;
		}
		
		target.damage(damage, entity);
	}
	
	@Override
	public void knockback(LivingEntity target, Vector direction, boolean resetFallDistance, AbilityInstance provider) {
		UserKnockbackEntityEvent event = EventUtil.call(new UserKnockbackEntityEvent(this, target, direction, resetFallDistance, provider));
		if (event.isCancelled()) {
			return;
		}
		
		if (event.doesResetFallDistance()) {
			target.setFallDistance(0);
		}
		
		target.setVelocity(direction);
	}
}
