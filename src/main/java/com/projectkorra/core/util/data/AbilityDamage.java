package com.projectkorra.core.util.data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityInstance;

public class AbilityDamage implements Projectile {
	
	private AbilityInstance provider;
	
	public AbilityDamage(AbilityInstance provider) {
		this.provider = provider;
	}

	@Override
	public Location getLocation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Location getLocation(Location loc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVelocity(Vector velocity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Vector getVelocity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getHeight() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getWidth() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOnGround() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isInWater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public World getWorld() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRotation(float yaw, float pitch) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean teleport(Location location) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean teleport(Location location, TeleportCause cause) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean teleport(Entity destination) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean teleport(Entity destination, TeleportCause cause) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getEntityId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFireTicks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFireTicks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFireTicks(int ticks) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVisualFire(boolean fire) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isVisualFire() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFreezeTicks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFreezeTicks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFreezeTicks(int ticks) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFrozen() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}

	@Override
	public boolean isPersistent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPersistent(boolean persistent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Entity getPassenger() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setPassenger(Entity passenger) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Entity> getPassengers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addPassenger(Entity passenger) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removePassenger(Entity passenger) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean eject() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getFallDistance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFallDistance(float distance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityDamageEvent getLastDamageCause() {
		throw new UnsupportedOperationException();
	}

	@Override
	public UUID getUniqueId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getTicksLived() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTicksLived(int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void playEffect(EntityEffect type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityType getType() {
		return EntityType.ARROW;
	}

	@Override
	public boolean isInsideVehicle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean leaveVehicle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Entity getVehicle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCustomNameVisible(boolean flag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCustomNameVisible() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setGlowing(boolean flag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isGlowing() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setInvulnerable(boolean flag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isInvulnerable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSilent() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSilent(boolean flag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasGravity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setGravity(boolean gravity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPortalCooldown() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPortalCooldown(int cooldown) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getScoreboardTags() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addScoreboardTag(String tag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeScoreboardTag(String tag) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BlockFace getFacing() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pose getPose() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SpawnCategory getSpawnCategory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Spigot spigot() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasMetadata(String metadataKey) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendMessage(String message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendMessage(String... messages) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendMessage(UUID sender, String message) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendMessage(UUID sender, String... messages) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return provider.getName();
	}

	@Override
	public boolean isPermissionSet(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(Permission perm) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void recalculatePermissions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOp() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOp(boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCustomName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCustomName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PersistentDataContainer getPersistentDataContainer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ProjectileSource getShooter() {
		return provider.getUser().getEntity();
	}

	@Override
	public void setShooter(ProjectileSource source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean doesBounce() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setBounce(boolean doesBounce) {
		throw new UnsupportedOperationException();
	}
}
