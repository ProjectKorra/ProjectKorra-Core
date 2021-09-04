package com.projectkorra.core.system.ability;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.projectkorra.core.event.user.UserBindChangeEvent;
import com.projectkorra.core.event.user.UserBindCopyEvent;
import com.projectkorra.core.event.user.UserCooldownStartEvent;
import com.projectkorra.core.system.skill.Skill;
import com.projectkorra.core.system.skill.SkillHolder;
import com.projectkorra.core.util.Events;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Interface for identifying something that can activate abilities
 */
public abstract class AbilityUser extends SkillHolder {
	
	private AbilityBinds binds = new AbilityBinds();
	private Map<Ability, Cooldown> cooldowns = new HashMap<>();
	
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

	/**
	 * Copies the binds from another {@link AbilityBinds} object
	 * @param other Binds to copy
	 */
	public final void copyBinds(AbilityBinds other) {
		if (Events.call(new UserBindCopyEvent(this, other)).isCancelled()) {
			return;
		}

		binds.copy(other);
	}

	/**
	 * Gets the bound ability using {@link #getCurrentSlot()}
	 * @return ability bound to the current slot
	 */
	public final Optional<Ability> getBoundAbility() {
		return binds.get(getCurrentSlot());
	}

	/**
	 * Gets the ability bound to the given slot
	 * @param slot ability bind slot
	 * @return ability bound to the given slot
	 */
	public final Optional<Ability> getBoundAbility(int slot) {
		return binds.get(slot);
	}

	/**
	 * Gets which slots the given ability is bound to
	 * @param ability Ability to check for
	 * @return slots the ability is bound to
	 */
	public final Set<Integer> getSlotsWith(Ability ability) {
		return binds.slotsOf(ability);
	}

	// helper function to avoid rewriting code for bind and clear functions
	private void setSlot(int slot, Ability ability) {
		UserBindChangeEvent event = Events.call(new UserBindChangeEvent(this, slot, ability));

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

	public final boolean addCooldown(Ability ability, long cooldown) {
		return addCooldown(ability, cooldown, false);
	}

	/**
	 * Adds a cooldown for this user on the given ability, ignored if the tag is null, cooldown is non-positive, or 
	 * the ability is already on cooldown (and noncumulative)
	 * @param tag A form of id for the cooldown, usually relating it to an {@link AbilityInstance}
	 * @param cooldown Duration of the cooldown
	 * @param cumulative Should the cooldown be added to the existing duration
	 * @return true if the cooldown was successfully added
	 */
	public final boolean addCooldown(Ability ability, long cooldown, boolean cumulative) {
		if (ability == null || cooldown <= 0) {
			return false;
		}

		if (cooldowns.containsKey(ability) && !cumulative) {
			return false;
		}

		UserCooldownStartEvent event = Events.call(new UserCooldownStartEvent(this, ability, cooldown));

		if (event.isCancelled()) {
			return false;
		}

		cooldowns.computeIfAbsent(ability, Cooldown::new).addDuration(event.getDuration());
		return true;
	}

	/**
	 * Checks if this user has a cooldown for any of the given ability's tags
	 * from {@link Ability#getCooldownTags()}
	 * @param ability Ability to check for cooldowns
	 * @return true if any of the ability's cooldown tags are found for this user
	 */
	public final boolean isOnCooldown(Ability ability) {
		return cooldowns.containsKey(ability);
	}

	public final void removeCooldown(Ability ability) {
		cooldowns.remove(ability);
	}
	
	/**
	 * Checks if this the given location is protected from this {@link AbilityUser}
	 * @param loc Where to check
	 * @return true if the location is protected
	 */
	public abstract boolean checkDefaultProtections(Location loc);

	/**
	 * Gets the user's currently hovered slot
	 * @return Hovered slot
	 */
	public abstract int getCurrentSlot();
	
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
}