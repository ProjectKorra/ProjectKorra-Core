package com.projectkorra.core;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.entity.PlayerUser;
import com.projectkorra.core.event.user.UserCreationEvent;
import com.projectkorra.core.skill.Skill;
import com.projectkorra.core.util.Events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class UserManager {

	private static final Map<UUID, AbilityUser> USERS = new HashMap<>();
	private static boolean init = false;
	private static long prevTime = System.currentTimeMillis();

	public static void init(ProjectKorra plugin) {
		if (init)
			return;

		init = true;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, UserManager::updateUsers, 1, 1);
	}

	private static void updateUsers() {
		double deltaTime = (System.currentTimeMillis() - prevTime) / 1000D;

		for (AbilityUser user : USERS.values()) {
			user.getStamina().regen(deltaTime);
			user.progressCooldowns();
		}

		prevTime = System.currentTimeMillis();
	}

	public static boolean register(AbilityUser user) {
		if (user.getUniqueID() == null || !USERS.containsKey(user.getUniqueID())) {
			return false;
		}

		USERS.put(user.getUniqueID(), user);
		return true;
	}

	/**
	 * Return the AbilityUser associated with the given unique id, but only if the
	 * given class can be cast upon it, or null otherwise.
	 * 
	 * @param <T>   type of the class to cast
	 * @param uuid  unique id associated with the AbilityUser
	 * @param clazz what to return the AbilityUser as
	 * @return AbilityUser of the given type
	 */
	public static <T extends AbilityUser> T getAs(UUID uuid, Class<T> clazz) {
		if (uuid == null || clazz == null || !USERS.containsKey(uuid)) {
			return null;
		}

		AbilityUser user = USERS.get(uuid);
		if (clazz.isInstance(user)) {
			return clazz.cast(user);
		}

		return null;
	}

	/**
	 * Return the AbilityUser associated with the given unique id, or null if not
	 * found
	 * 
	 * @param uuid unique id of the AbilityUser
	 * @return an AbilityUser from the given id
	 */
	public static AbilityUser get(UUID uuid) {
		return USERS.get(uuid);
	}

	public static AbilityUser from(LivingEntity lent) {
		if (lent == null) {
			return null;
		}

		return USERS.get(lent.getUniqueId());
	}

	public static AbilityUser load(Player player) {
		PlayerUser user = new PlayerUser(player);

		try {
			if (ProjectKorra.database().read("SELECT * FROM t_pk_player WHERE uuid = '" + player.getUniqueId() + "'").next()) {
				ResultSet skillQuery = ProjectKorra.database().read("SELECT * FROM t_pk_player_skills WHERE uuid = '" + player.getUniqueId() + "'");
				while (skillQuery.next()) {
					Optional<Skill> skill = Skill.of(skillQuery.getString("skill_name"));

					if (!skill.isPresent()) {
						continue;
					}

					user.addSkill(skill.get());
					if (skillQuery.getInt("toggled") != 0) {
						user.toggle(skill.get());
					}
				}

				ResultSet abilityQuery = ProjectKorra.database().read("SELECT * FROM t_pk_player_binds WHERE uuid = '" + player.getUniqueId() + "'");
				while (abilityQuery.next()) {
					Optional<Ability> ability = AbilityManager.getAbility(abilityQuery.getString("ability_name"));

					if (!ability.isPresent()) {
						continue;
					}

					user.bindAbility(abilityQuery.getInt("bound_slot"), ability.get());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		USERS.put(player.getUniqueId(), user);
		Events.call(new UserCreationEvent(user));
		return user;
	}

	public static void save(Player player) {
		AbilityUser user = from(player);
		if (user == null) {
			return;
		}

		try {
			if (ProjectKorra.database().read("SELECT * FROM t_pk_player WHERE uuid = '" + player.getUniqueId() + "'").next()) {
				// update eventually when t_pk_player has more columns
			} else {
				ProjectKorra.database().modify("INSERT INTO t_pk_player VALUES ('" + player.getUniqueId() + "')");
			}

			for (Skill skill : Skill.values()) {
				if (!user.hasSkill(skill)) {
					ProjectKorra.database().modify("DELETE FROM t_pk_player_skills WHERE uuid = '" + player.getUniqueId() + "' AND skill_name = '" + skill.getInternalName() + "'");
				} else if (ProjectKorra.database().read("SELECT * FROM t_pk_player_skills WHERE uuid = '" + player.getUniqueId() + "' AND skill_name = '" + skill.getInternalName() + "'").next()) {
					ProjectKorra.database().modify("UPDATE t_pk_player_skills SET toggled = " + (user.isToggled(skill) ? 1 : 0) + " WHERE uuid = '" + player.getUniqueId() + "' AND skill_name = '" + skill.getInternalName() + "'");
				} else {
					ProjectKorra.database().modify("INSERT INTO t_pk_player_skills VALUES ('" + player.getUniqueId() + "', '" + skill.getInternalName() + "', " + (user.isToggled(skill) ? 1 : 0) + ")");
				}
			}

			int slot = -1;
			for (Ability ability : user.getBinds()) {
				++slot;
				if (ability == null) {
					ProjectKorra.database().modify("DELETE FROM t_pk_player_binds WHERE uuid = '" + player.getUniqueId() + "' AND bound_slot = " + slot);
				} else if (ProjectKorra.database().read("SELECT * FROM t_pk_player_binds WHERE uuid = '" + player.getUniqueId() + "' AND bound_slot = " + slot).next()) {
					ProjectKorra.database().modify("UPDATE t_pk_player_binds SET ability_name = '" + ability.getName() + "' WHERE uuid = '" + player.getUniqueId() + "' AND bound_slot = " + slot);
				} else {
					ProjectKorra.database().modify("INSERT INTO t_pk_player_binds VALUES ('" + player.getUniqueId() + "', " + slot + ", '" + ability.getName() + "')");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		USERS.remove(player.getUniqueId());
	}

	public static <T> Set<T> collect(Function<AbilityUser, T> map) {
		return USERS.values().stream().map(map).collect(Collectors.toSet());
	}

	public static Set<AbilityUser> online() {
		return new HashSet<>(USERS.values());
	}
}
