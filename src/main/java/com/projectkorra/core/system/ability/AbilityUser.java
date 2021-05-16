package com.projectkorra.core.system.ability;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.system.skill.SkillHolder;

/**
 * Interface for identifying something that can activate abilities
 */
public abstract class AbilityUser extends SkillHolder {
	
	private AbilityBinds binds = new AbilityBinds();
	
	public boolean immune = false;
	
	public AbilityUser(Collection<Skill> skills) {
		super(skills);
	}
	
	public AbilityUser(Collection<Skill> skills, Collection<Skill> toggled) {
		super(skills, toggled);
	}
	
	public AbilityUser(Collection<Skill> skills, Collection<Skill> toggled, AbilityBinds binds) {
		super(skills, toggled);
		this.binds.copy(binds);
	}
	
	public final AbilityBinds getBinds() {
		return binds;
	}
	
	/**
	 * Gets the bound ability of the user's current slot
	 * @return The currently bound ability
	 */
	public abstract Ability getBoundAbility();
	
	/**
	 * Gets the location of where abilities generally start
	 * from this user
	 * @return The ability starting location of this user
	 */
	public abstract Location getLocation();
	
	/**
	 * Gets the direction for abilities to start in
	 * @return The ability starting direction of this user
	 */
	public abstract Vector getDirection();
	
	/**
	 * Gets the unique id for this user
	 * @return The user's unique id
	 */
	public abstract UUID getUniqueID();
	
	/**
	 * Checks whether the user should be removed from
	 * memory or not
	 * @return True to remove user from memory
	 */
	public abstract boolean shouldRemove();
	
	/**
	 * Cause this user to inflict damage to the given target
	 * @param target The entity to damage
	 * @param damage How much damage to inflict
	 * @param ignoreArmor If true, the damage will be increased so that any reductions from armor are <b>approximately</b> negated
	 * @param source The {@link AbilityInstance} which caused the knockback, if applicable
	 */
	public abstract void damage(LivingEntity target, double damage, boolean ignoreArmor, AbilityInstance source);
	
	/**
	 * Cause this user to knockback a target in the given direction
	 * <br><br>If called in conjunction with {@link #damage(LivingEntity, double, boolean, AbilityInstance)} on the same target,
	 * make sure to call this method after the damage so the knockback from the damage itself won't override this knockback.
	 * @param target The entity to knockback
	 * @param direction Where to knockback toward
	 * @param resetFallDistance Whether to set the target's fall distance to zero or not
	 * @param source The {@link AbilityInstance} which caused the knockback, if applicable
	 */
	public abstract void knockback(LivingEntity target, Vector direction, boolean resetFallDistance, AbilityInstance source);
}
