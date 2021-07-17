package com.projectkorra.core.system.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public final class UserManager {
    
    private UserManager() {}

    public static final Map<UUID, User<?>> USERS = new HashMap<>();

    /**
     * Gets the user associated with the given uuid as the specified type
     * @param <T> Type of the user
     * @param uuid Unique id associated with the user (usually player or mob unique id)
     * @param as Class of the type
     * @return empty optional if either param is null or uuid has no association, otherwise optional with the user
     */
    public static <T extends User<?>> Optional<T> getUserAs(UUID uuid, Class<T> as) {
        if (uuid == null || as == null) {
            return Optional.empty();
        }

        User<?> user = USERS.get(uuid);

        if (as.isInstance(user)) {
            return Optional.of(as.cast(user));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Checks if the given uuid is registered with any user
     * @param uuid {@link UUID} to check for
     * @return true if a user is registered under the uuid
     */
    public static boolean hasUser(UUID uuid) {
        return USERS.containsKey(uuid);
    }

    /**
     * Attempts to register the given user object
     * @param <T> Type of the user
     * @param user User to register
     * @return the same user passed in
     * @throws IllegalArgumentException when the user is null, user's uuid is null, or the uuid is already registered
     */
    public static <T extends User<?>> T register(T user) {
        Validate.notNull(user, "Cannot register null user!");
        Validate.notNull(user.getUniqueID(), "Cannot register user with null uuid!");
        Validate.isTrue(!USERS.containsKey(user.getUniqueID()), "Cannot register existing user!");
        USERS.put(user.getUniqueID(), user);
        return user;
    }   

    /**
     * Gets the registered user for the given {@link Player} or creates a new user
     * for them if one does not exist, hence this never returns null.
     * @param player {@link Player} to get the user of
     * @return {@link PlayerUser} of the player
     */
    public static PlayerUser playerUser(Player player) {
        return (PlayerUser) USERS.get(player.getUniqueId());
    }

    /**
     * Gets the registered user for the given {@link Mob}
     * @param mob {@link Mob} to get the user of
     * @return empty optional if no user exists for the mob, otherwise optional with the user of the mob
     */
    public static Optional<MobUser> mobUser(Mob mob) {
        return Optional.ofNullable((MobUser) USERS.get(mob.getUniqueId()));
    }

    /**
     * Gets the registered user for the given {@link Mob} or registers a new user
     * for it if one does not exist, this should never return null.
     * @param mob {@link Mob} to get the user of
     * @param supplier {@link MobUser} supplier to use if no user is found
     * @return a {@link MobUser} for the given mob
     */
    public static MobUser mobUser(Mob mob, Supplier<MobUser> supplier) {
        return mobUser(mob).orElseGet(() -> register(supplier.get()));
    }
}
