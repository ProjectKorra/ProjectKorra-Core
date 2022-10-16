package com.projectkorra.core.ability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.event.user.UserBindChangeEvent;
import com.projectkorra.core.event.user.UserBindCopyEvent;
import com.projectkorra.core.event.user.UserCooldownEndEvent;
import com.projectkorra.core.event.user.UserCooldownStartEvent;
import com.projectkorra.core.skill.SkillHolder;
import com.projectkorra.core.util.Events;
import com.projectkorra.core.util.data.Stamina;

/**
 * Class for identifying something that can activate abilities
 */
public abstract class AbilityUser extends SkillHolder {

	private AbilityBinds binds = new AbilityBinds();
	private Map<String, Cooldown> cooldowns = new HashMap<>();
	private PriorityQueue<Cooldown> cdQueue = new PriorityQueue<>(32, (a, b) -> (int) (b.getEndTime() - a.getEndTime()));
	private LivingEntity entity;
	private Stamina stamina;
	private List<ComboValidator> sequences = new LinkedList<>();
	
	Set<AbilityInstance> active = new HashSet<>();

	public boolean immune = false;

	public AbilityUser(LivingEntity entity) {
		this.entity = entity;
		this.stamina = new Stamina(this, 0.1);
	}

	ComboValidator updateCombos(Ability ability, Activation trigger) {
		ComboValidator completed = null;
		sequences.add(new ComboValidator());
		Iterator<ComboValidator> iter = sequences.iterator();
		outer: while (iter.hasNext()) {
			ComboValidator agent = iter.next();

			switch (agent.update(ability, trigger)) {
				case COMPLETE:
					completed = agent;
					break outer;
				case FAILED:
					iter.remove();
				case INCOMPLETE:
					break;
			}
		}

		if (completed != null) {
			sequences.clear();
		}

		return completed;
	}
	
	public <T extends AbilityInstance> Optional<T> getInstance(Class<T> clazz) {
		return getInstance(clazz, (t) -> true);
	}
	
	public <T extends AbilityInstance> Optional<T> getInstance(Class<T> clazz, Predicate<T> filter) {
		for (AbilityInstance inst : active) {
			if (!inst.getClass().isAssignableFrom(clazz)) {
				continue;
			}
			
			T found = clazz.cast(inst);
			if (filter.test(found)) {
				return Optional.of(found);
			}
		}
		
		return Optional.empty();
	}
	
	public <T extends AbilityInstance> Set<T> getInstances(Class<T> clazz) {
		return getInstances(clazz, (t) -> true);
	}
	
	public <T extends AbilityInstance> Set<T> getInstances(Class<T> clazz, Predicate<T> filter) {
		Set<T> found = new HashSet<>();
		
		for (AbilityInstance inst : active) {
			if (!inst.getClass().isAssignableFrom(clazz)) {
				continue;
			}
			
			T tInst = clazz.cast(inst);
			if (filter.test(tInst)) {
				found.add(tInst);
			}
		}
		
		return found;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public final Stamina getStamina() {
		return stamina;
	}

	/**
	 * Binds the given ability to the specified slot
	 * 
	 * @param slot    Where to bind the ability on the hotbar, slots range [0, 8]
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
	 * 
	 * @param slots     Array of slots to bind to
	 * @param abilities Array of abilities to bind
	 * @return empty boolean array if given arrays do not match length, otherwise a
	 *         boolean array returning the value of
	 *         {@link AbilityUser#bindAbility(int, Ability)} for each pair
	 *         <code>slots[i], abilities[i]</code> that is the same length as the
	 *         given arrays
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

	public final boolean canBind(Ability ability) {
		return ability instanceof Bindable && hasPermission("bending.ability." + ability.getName()) && this.hasSkill(ability.getSkill());
	}

	/**
	 * Clears the bind of the given slot. Silently ignored if slot is out of bounds
	 * or no ability is bound
	 * 
	 * @param slot Where to clear the bind from
	 */
	public final void clearBind(int slot) {
		if (slot < 0 || slot > 8 || binds.get(slot) == null) {
			return;
		}

		setSlot(slot, null);
	}

	/**
	 * Clears the binds for the given slots, directly calling
	 * {@link #clearBind(int)} for each slot.
	 * 
	 * @param slots Slots to be cleared
	 */
	public final void clearBinds(int...slots) {
		for (int slot : slots) {
			clearBind(slot);
		}
	}

	/**
	 * Copies the binds from another {@link AbilityBinds} object
	 * 
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
	 * 
	 * @return ability bound to the current slot
	 */
	public final Optional<Ability> getBoundAbility() {
		return binds.get(getCurrentSlot());
	}

	/**
	 * Gets the ability bound to the given slot
	 * 
	 * @param slot ability bind slot
	 * @return ability bound to the given slot
	 */
	public final Optional<Ability> getBoundAbility(int slot) {
		return binds.get(slot);
	}

	/**
	 * Gets which slots the given ability is bound to
	 * 
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
	 * Returns a copy of the player's current {@link AbilityBinds}. This is a
	 * <b>copy</b> and thus changes to the returned object will not be reflected in
	 * the user's binds. See {@link #copyBinds(AbilityBinds)},
	 * {@link #bindAbility(int, Ability)}, and {@link #clearBind(int)} to modify the
	 * user's binds.
	 * 
	 * @return copy of the player's binds
	 */
	public final AbilityBinds getBinds() {
		return binds.clone();
	}

	public final boolean addCooldown(Ability ability, long cooldown) {
		return addCooldown(ability.getName(), cooldown, false);
	}

	public final boolean addCooldown(Ability ability, long cooldown, boolean cumulative) {
		return addCooldown(ability.getName(), cooldown, cumulative);
	}

	public final boolean addCooldown(String tag, long cooldown) {
		return addCooldown(tag, cooldown, false);
	}

	/**
	 * Adds a cooldown for this user on the given ability, ignored if the tag is
	 * null, cooldown is non-positive, or the ability is already on cooldown (and
	 * noncumulative)
	 * 
	 * @param tag        A form of id for the cooldown, usually relating it to an
	 *                   {@link AbilityInstance}
	 * @param cooldown   Duration of the cooldown
	 * @param cumulative Should the cooldown be added to the existing duration
	 * @return true if the cooldown was successfully added
	 */
	public final boolean addCooldown(String tag, long cooldown, boolean cumulative) {
		if (tag == null || cooldown <= 0) {
			return false;
		}

		if (cooldowns.containsKey(tag) && !cumulative) {
			return false;
		}

		UserCooldownStartEvent event = Events.call(new UserCooldownStartEvent(this, tag, cooldown));

		if (event.isCancelled()) {
			return false;
		}

		Cooldown cd = cooldowns.computeIfAbsent(tag, Cooldown::new);
		cd.addDuration(event.getDuration());
		cdQueue.add(cd);
		return true;
	}

	public final void updateCooldowns() {
		while (cdQueue.peek() != null) {
			if (cdQueue.peek().getRemaining() >= 0) {
				break;
			}

			removeCooldown(cdQueue.poll().getTag());
		}
	}

	/**
	 * Checks if this user has a cooldown for any of the given ability's tags from
	 * {@link Ability#getCooldownTags()}
	 * 
	 * @param ability Ability to check for cooldowns
	 * @return true if any of the ability's cooldown tags are found for this user
	 */
	public final boolean hasCooldown(Ability ability) {
		return cooldowns.containsKey(ability.getName());
	}

	public final boolean hasCooldown(String tag) {
		return cooldowns.containsKey(tag);
	}

	public final void removeCooldown(Ability ability) {
		removeCooldown(ability.getName());
	}

	public final void removeCooldown(String tag) {
		if (!cooldowns.containsKey(tag)) {
			return;
		}

		Cooldown cd = cooldowns.remove(tag);
		Events.call(new UserCooldownEndEvent(this, cd));
	}
	
	public Location getTargetLocation(double distance, FluidCollisionMode fluids, boolean ignorePassable, Predicate<Entity> filter) {
		return rayTrace(distance, fluids, ignorePassable, 1, filter)
				.map((r) -> r.getHitPosition().toLocation(entity.getWorld()))
				.orElseGet(() -> entity.getEyeLocation().add(entity.getLocation().getDirection().multiply(distance)));
	}
	
	public Optional<RayTraceResult> rayTrace(double maxDistance, FluidCollisionMode fluids, boolean ignorePassable, double raySize, Predicate<Entity> filter) {
		return Optional.ofNullable(rayTraceHelper(entity.getEyeLocation(), maxDistance, fluids, ignorePassable, raySize, filter));
	}
	
	private RayTraceResult rayTraceHelper(Location loc, double maxDistance, FluidCollisionMode fluids, boolean ignorePassable, double raySize, Predicate<Entity> filter) {
		return entity.getWorld().rayTrace(loc, loc.getDirection(), maxDistance, fluids, ignorePassable, raySize, filter);
	}
	
	public boolean isOnline() {
		return !entity.isDead();
	}
	
	public abstract void sendMessage(String message);

	public abstract boolean hasPermission(String permission);

	/**
	 * Checks if this the given location is protected from this {@link AbilityUser}
	 * 
	 * @param loc Where to check
	 * @return true if the location is protected
	 */
	public abstract boolean checkDefaultProtections(Location loc);

	/**
	 * Gets the user's currently hovered slot
	 * 
	 * @return Hovered slot
	 */
	public abstract int getCurrentSlot();

	/**
	 * Gets the location of where abilities generally start from this user
	 * 
	 * @return The ability starting location of this user
	 */
	public abstract Location getLocation();

	/**
	 * Gets the eye level location of this user
	 * 
	 * @return eye height location
	 */
	public abstract Location getEyeLocation();

	/**
	 * Gets the direction for abilities to start in
	 * 
	 * @return The ability starting direction of this user
	 */
	public abstract Vector getDirection();

	public abstract Optional<Entity> getTargetEntity(double range, double raySize, FluidCollisionMode fluid, Predicate<Entity> filter);

	/**
	 * Gets the unique id for this user
	 * 
	 * @return The user's unique id
	 */
	public abstract UUID getUniqueID();

	/**
	 * Checks whether the user should be removed from memory or not
	 * 
	 * @return True to remove user from memory
	 */
	public abstract boolean shouldRemove();

	public abstract MainHand getMainHand();

	public final MainHand getOffHand() {
		return getMainHand() == MainHand.RIGHT ? MainHand.LEFT : MainHand.RIGHT;
	}
}
