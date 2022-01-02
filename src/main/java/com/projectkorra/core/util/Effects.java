package com.projectkorra.core.util;

import com.projectkorra.core.event.ability.InstanceDamageEntityEvent;
import com.projectkorra.core.event.ability.InstanceIgniteEntityEvent;
import com.projectkorra.core.event.ability.InstanceMoveEntityEvent;
import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.util.data.AbilityDamage;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public final class Effects {
    
    private Effects() {}

	/**
	 * Apply damage to the given target from the ability source, with the option to calculate ignore armor
	 * @param target The entity to damage
	 * @param damage How much damage to apply
	 * @param source The ability causing the damage
	 * @param ignoreArmor Whether to ignore armor stats
	 */
    public static void damage(LivingEntity target, double damage, AbilityInstance source, boolean ignoreArmor) {
        if (target.getNoDamageTicks() > target.getMaximumNoDamageTicks() / 2.0f && damage <= target.getLastDamage()) {
			return;
		}

		InstanceDamageEntityEvent event = Events.call(new InstanceDamageEntityEvent(target, damage, source, ignoreArmor));
		if (event.isCancelled()) {
			return;
		}
		
		damage = event.getDamage();
		
		if (event.doesIgnoreArmor() && damage > 0) {
			double defense = target.getAttribute(Attribute.GENERIC_ARMOR).getValue();
			double toughness = target.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
			damage /= 1 - (Math.min(20, Math.max(defense / 5, defense - 4 * damage / (toughness + 8)))) / 25;
		}
		
		target.damage(damage, new AbilityDamage(source));
    }

	/**
	 * Checks the given damage event to see if the damage was caused by an ability instance
	 * @param damageEvent The damage event to check
	 * @return true if the damage was caused by an ability instance
	 */
	public static boolean isAbilityDamage(EntityDamageByEntityEvent damageEvent) {
		return damageEvent.getDamager() instanceof AbilityDamage;
	}

	/**
	 * Checks the given damage event to see if it was caused by the specified ability instance
	 * @param damageEvent The damage event to check
	 * @param clazz The class of the ability instance to check for
	 * @return true if the damage was caused by the specific ability instance
	 */
	public static boolean isAbilityDamage(EntityDamageByEntityEvent damageEvent, Class<? extends AbilityInstance> clazz) {
		if (damageEvent == null || clazz == null) {
			return false;
		}

		return damageEvent.getDamager() instanceof AbilityDamage && clazz.isInstance(((AbilityDamage) damageEvent.getDamager()).getInstance());
	}

	/**
	 * Apply movement to the target entity in the given direction from the ability source,
	 * with options to flag it as knockback and to reset the entity's fall distance
	 * @param target The entity to move
	 * @param direction How to move the entity
	 * @param source The ability applying movement
	 * @param knockback Whether the movement was knockback
	 * @param resetFallDistance Whether the movement should reset fall distance to zero
	 */
	public static void move(LivingEntity target, Vector direction, AbilityInstance source, boolean knockback, boolean resetFallDistance) {
		InstanceMoveEntityEvent event = Events.call(new InstanceMoveEntityEvent(target, direction, source, knockback, resetFallDistance));
		if (event.isCancelled()) {
			return;
		}
		
		if (event.doesResetFallDistance()) {
			target.setFallDistance(0);
		}

		if (knockback) {
			// track knockback for impact damage
		}
		
		target.setVelocity(direction);
	}

	public static void ignite(LivingEntity target, int fireTicks, AbilityInstance source) {
		InstanceIgniteEntityEvent event = Events.call(new InstanceIgniteEntityEvent(target, fireTicks, source));

		if (event.isCancelled()) {
			return;
		}

		target.setFireTicks(target.getFireTicks() + event.getFireTicks());
	}
}