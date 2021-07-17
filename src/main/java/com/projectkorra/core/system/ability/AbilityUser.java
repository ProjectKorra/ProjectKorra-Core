package com.projectkorra.core.system.ability;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.projectkorra.core.event.user.UserBindChangeEvent;
import com.projectkorra.core.event.user.UserBindCopyEvent;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.system.skill.SkillHolder;
import com.projectkorra.core.util.EventUtil;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

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
	
	/**
	 * Binds the given ability to the specified slot
	 * @param slot Where to bind the ability on the hotbar, slots range [0, 8]
	 * @param ability {@link Ability} to bind at the slot
	 * @return false if the slot is out of bounds or the ability is null
	 */
	public final boolean bindAbility(int slot, Ability ability) {
		if (slot < 0 || slot > 8 || ability == null) {
			return false;
		}

		setSlot(slot, ability);
		return true;
	}

	/**
	 * Binds the given abilities to the specified slots, such that elements in slots
	 * correspond to elements at the same index in abilities
	 * @param slots Array of slots to bind to
	 * @param abilities Array of abilities to bind
	 * @return empty boolean array if given arrays do not match length, otherwise a boolen array returning the value of
	 * {@link AbilityUser#bindAbility(int, Ability)} for each pair <code>slots[i], abilities[i]</code> that is the same length as the given arrays
	 */
	public final boolean[] bindAbilities(int[] slots, Ability[] abilities) {
		if (slots.length != abilities.length) {
			return new boolean[0];
		}

		boolean[] bools = new boolean[slots.length];
		for (int i = 0; i < slots.length; ++i) {
			bools[i] = bindAbility(slots[i], abilities[i]);
		}

		return bools;
	}

	/**
	 * Clears the bind of the given slot. Silently ignored if slot is out of bounds or no ability is bound
	 * @param slot Where to clear the bind from
	 */
	public final void clearBind(int slot) {
		if (slot < 0 || slot > 8 || binds.get(slot) == null) {
			return;
		}

		setSlot(slot, null);
	}

	/**
	 * Clears the binds for the given slots, directly calling {@link #clearBind(int)} for each slot.
	 * @param slots Slots to be cleared
	 */
	public final void clearBinds(int...slots) {
		for (int slot : slots) {
			clearBind(slot);
		}
	}

	public final void copyBinds(AbilityBinds other) {
		if (EventUtil.call(new UserBindCopyEvent(this, other)).isCancelled()) {
			return;
		}

		binds.copy(other);
	}

	public final Optional<Ability> getBoundAbility(int slot) {
		if (slot < 0 || slot > 8) {
			return Optional.empty();
		}

		return Optional.ofNullable(binds.get(slot));
	}

	// helper function to avoid rewriting code for bind and clear functions
	private void setSlot(int slot, Ability ability) {
		UserBindChangeEvent event = EventUtil.call(new UserBindChangeEvent(this, slot, ability));

		if (event.isCancelled()) {
			return;
		}

		binds.set(slot, event.getResult());
	}

	/**
	 * Returns a copy of the player's current {@link AbilityBinds}. This is a <b>copy</b> and thus changes
	 * to the returned object will not be reflected in the user's binds. See {@link #copyBinds(AbilityBinds)},
	 * {@link #bindAbility(int, Ability)}, and {@link #clearBind(int)} to modify the user's binds.
	 * @return copy of the player's binds
	 */
	public final AbilityBinds getBinds() {
		return binds.clone();
	}
	
	/**
	 * Checks if this the given location is protected from this {@link AbilityUser}
	 * @param loc Where to check
	 * @return true if the location is protected
	 */
	public abstract boolean checkDefaultProtections(Location loc);
	
	/**
	 * Gets the bound ability of the user's current slot
	 * @return The currently bound ability
	 */
	public abstract Optional<Ability> getCurrentAbility();
	
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
	public abstract void damage(LivingEntity target, double damage, boolean ignoreArmor, AbilityInstance provider);
	
	/**
	 * Cause this user to knockback a target in the given direction
	 * <br><br>If called in conjunction with {@link #damage(LivingEntity, double, boolean, AbilityInstance)} on the same target,
	 * make sure to call this method after the damage so the knockback from the damage itself won't override this knockback.
	 * @param target The entity to knockback
	 * @param direction Where to knockback toward
	 * @param resetFallDistance Whether to set the target's fall distance to zero or not
	 * @param source The {@link AbilityInstance} which caused the knockback, if applicable
	 */
	public abstract void knockback(LivingEntity target, Vector direction, boolean resetFallDistance, AbilityInstance provider);
}
