package com.projectkorra.core.util.data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.projectkorra.core.system.ability.AbilityInstance;
import com.projectkorra.core.system.ability.AbilityUser;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class AbilityDamage implements Entity {

    private AbilityInstance instance;

    public AbilityDamage(AbilityInstance source) {
        this.instance = source;
    }
    
    @Override
    public String getName() {
        return instance.getClass().getSimpleName();
    }

    public AbilityUser getUser() {
        return instance.getUser();
    }

    public AbilityInstance getInstance() {
        return instance;
    }

    @Override @Deprecated public void setMetadata(String metadataKey, MetadataValue newMetadataValue) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public List<MetadataValue> getMetadata(String metadataKey) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean hasMetadata(String metadataKey) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void removeMetadata(String metadataKey, Plugin owningPlugin) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void sendMessage(String message) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void sendMessage(String[] messages) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void sendMessage(UUID sender, String message) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void sendMessage(UUID sender, String[] messages) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isPermissionSet(String name) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isPermissionSet(Permission perm) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean hasPermission(String name) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean hasPermission(Permission perm) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public PermissionAttachment addAttachment(Plugin plugin) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public PermissionAttachment addAttachment(Plugin plugin, int ticks) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void removeAttachment(PermissionAttachment attachment) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void recalculatePermissions() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Set<PermissionAttachmentInfo> getEffectivePermissions() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isOp() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setOp(boolean value) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public String getCustomName() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setCustomName(String name) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public PersistentDataContainer getPersistentDataContainer() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Location getLocation() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Location getLocation(Location loc) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setVelocity(Vector velocity) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Vector getVelocity() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public double getHeight() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public double getWidth() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public BoundingBox getBoundingBox() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isOnGround() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public World getWorld() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setRotation(float yaw, float pitch) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean teleport(Location location) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean teleport(Location location, TeleportCause cause) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean teleport(Entity destination) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean teleport(Entity destination, TeleportCause cause) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public List<Entity> getNearbyEntities(double x, double y, double z) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public int getEntityId() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public int getFireTicks() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public int getMaxFireTicks() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setFireTicks(int ticks) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void remove() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isDead() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isValid() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Server getServer() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isPersistent() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setPersistent(boolean persistent) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Entity getPassenger() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean setPassenger(Entity passenger) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public List<Entity> getPassengers() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean addPassenger(Entity passenger) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean removePassenger(Entity passenger) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isEmpty() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean eject() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public float getFallDistance() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setFallDistance(float distance) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setLastDamageCause(EntityDamageEvent event) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public EntityDamageEvent getLastDamageCause() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public UUID getUniqueId() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public int getTicksLived() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setTicksLived(int value) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void playEffect(EntityEffect type) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public EntityType getType() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isInsideVehicle() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean leaveVehicle() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Entity getVehicle() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setCustomNameVisible(boolean flag) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isCustomNameVisible() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setGlowing(boolean flag) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isGlowing() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setInvulnerable(boolean flag) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isInvulnerable() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean isSilent() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setSilent(boolean flag) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean hasGravity() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setGravity(boolean gravity) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public int getPortalCooldown() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public void setPortalCooldown(int cooldown) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Set<String> getScoreboardTags() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean addScoreboardTag(String tag) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public boolean removeScoreboardTag(String tag) { throw new UnsupportedOperationException(); }
    @Override @Deprecated public PistonMoveReaction getPistonMoveReaction() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public BlockFace getFacing() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Pose getPose() { throw new UnsupportedOperationException(); }
    @Override @Deprecated public Spigot spigot() { throw new UnsupportedOperationException(); }
}
