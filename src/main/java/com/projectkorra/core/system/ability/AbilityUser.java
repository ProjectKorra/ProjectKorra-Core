package com.projectkorra.core.system.ability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.projectkorra.core.system.ability.activation.Activation;
import com.projectkorra.core.system.ability.activation.ActivationCriteria;
import com.projectkorra.core.util.data.Holder;

/**
 * Interface for identifying something that can activate abilities
 */
public interface AbilityUser {
	
	final Set<ActivationCriteria> combos = new HashSet<>();
	final Map<Activation, ActivationCriteria> passives = new HashMap<>();
	Holder<ActivationCriteria> active = Holder.of(null);
	
	public default AbilityInstance trigger(Activation trigger) {
		if (getBoundAbility() != null) {
			if (active.getHeld() != null) {
				AbilityInstance instance = active.getHeld().update(this, getBoundAbility(), trigger);
				if (instance != null) {
					return instance;
				} else if (active.getHeld().getProgress() == 0) {
					active.setHeld(null);
				}
			}
			
			// this is not elseif because the above block can set it to null
			if (active.getHeld() == null) {
				for (ActivationCriteria criteria : combos) {
					AbilityInstance instance = criteria.update(this, getBoundAbility(), trigger);
					if (instance != null) {
						return instance;
					} else if (criteria.getProgress() > 0) {
						active.setHeld(criteria);
						break;
					}
				}
			}
			
			if (getBoundAbility().uses(trigger)) {
				return getBoundAbility().activate(this, trigger);
			}
		}
		
		if (passives.containsKey(trigger)) {
			return passives.get(trigger).update(this, getBoundAbility(), trigger);
		}
		
		return null;
	}
	
	/**
	 * Gets the bound ability of the user's current slot
	 * @return currently bound ability
	 */
	public Ability getBoundAbility();
	
	/**
	 * Gets the location of where abilities generally start
	 * from this user
	 * @return ability starting location
	 */
	public Location getLocation();
	
	/**
	 * Gets the direction for abilities to start in
	 * @return ability starting direction
	 */
	public Vector getDirection();
	
	/**
	 * Gets the unique id for this user
	 * @return user unique id
	 */
	public UUID getUniqueID();
	
	/**
	 * Checks whether the user should be removed from
	 * memory or not
	 * @return true to remove user from memory
	 */
	public boolean shouldRemove();
}
