package com.projectkorra.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.Validate;

import com.projectkorra.core.system.ability.AbilityUser;
import com.projectkorra.core.system.ability.activation.Activation;

public final class UserManager {

	private static final Map<UUID, AbilityUser> USERS = new HashMap<>();
	
	public static boolean register(AbilityUser user) {
		if (user.getUniqueID() == null || !USERS.containsKey(user.getUniqueID())) {
			return false;
		}
		
		USERS.put(user.getUniqueID(), user);
		return true;
	}
	
	/**
	 * Return the AbilityUser associated with the given unique id, but
	 * only if the given class can be cast upon it, or null otherwise.
	 * @param <T> type of the class to cast
	 * @param uuid unique id associated with the AbilityUser
	 * @param clazz what to return the AbilityUser as
	 * @return AbilityUser of the given type
	 */
	public static <T extends AbilityUser> T get(UUID uuid, Class<T> clazz) {
		Validate.notNull(uuid, "Ability user cannot have a null unique id");
		Validate.notNull(clazz, "User class cannot be null");
		
		if (!USERS.containsKey(uuid)) {
			return null;
		}
		
		AbilityUser user = USERS.get(uuid);
		if (clazz.isInstance(user)) {
			return clazz.cast(user);
		}
		
		return null;
	}
	
	/**
	 * Return the AbilityUser associated with the given unique id, or null
	 * if not found
	 * @param uuid unique id of the AbilityUser
	 * @return an AbilityUser from the given id
	 */
	public static AbilityUser get(UUID uuid) {
		Validate.notNull(uuid, "Ability user cannot have a null unique id");
		return USERS.get(uuid);
	}
	
	public static void trigger(AbilityUser user, Activation trigger) {
		if (user.getBoundAbility() == null) {
			
		}
	}
	
	static void tick() {
		Iterator<Entry<UUID, AbilityUser>> iter = USERS.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<UUID, AbilityUser> next = iter.next();
			if (next.getValue().shouldRemove()) {
				iter.remove();
			}
		}
	}
}
