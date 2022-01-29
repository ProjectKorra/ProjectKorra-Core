package com.projectkorra.core.util;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.ability.AbilityInstance;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Velocity {

    private static final String LOCKED = "velocity_locked";
    private static final String METAKEY = "velocity";

    public static void apply(Entity entity, Vector velocity, AbilityInstance provider) {
        apply(entity, velocity, provider, false);
    }

    public static void apply(Entity entity, Vector velocity, AbilityInstance provider, boolean lock) {
        if (entity == null || velocity == null) {
            return;
        } else if (entity.hasMetadata(LOCKED) && !entity.getMetadata(LOCKED).get(0).value().equals(provider)) {
            return;
        } else if (lock && !entity.hasMetadata(LOCKED)) {
            entity.setMetadata(LOCKED, new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), provider));
            Threads.runDelayedTask(() -> entity.removeMetadata(LOCKED, JavaPlugin.getPlugin(ProjectKorra.class)), 60);
        }

        entity.setMetadata(METAKEY, new FixedMetadataValue(JavaPlugin.getPlugin(ProjectKorra.class), provider));
        Threads.runDelayedTask(() -> entity.removeMetadata(METAKEY, JavaPlugin.getPlugin(ProjectKorra.class)), 60);
        entity.setVelocity(entity.getVelocity().add(velocity));
    }

    public static boolean isAffected(Entity entity) {
        return entity.hasMetadata(METAKEY);
    }
}
