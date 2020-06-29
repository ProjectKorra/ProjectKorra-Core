package com.projectkorra.core.util;

import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;
import org.bukkit.util.Vector;

public class SpatialUtil {
	
	public static final Predicate<Entity> DEFAULT_FILTER = (entity) -> !(entity.isDead() || (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR));
	public static final Predicate<Block> PASSABLE_FILTER = (block) -> !block.isPassable();
	
	public static Entity getClosestEntity(Location center, double radius) {
		return getClosestEntity(center, radius, DEFAULT_FILTER);
	}
	
	public static Entity getClosestEntity(Location center, double radius, Predicate<Entity> filter) {
		double curr = 0.1;
		Collection<Entity> entities = getEntitiesAroundPoint(center, curr);
		
		while (entities.isEmpty() && curr <= radius) {
			curr += 0.1;
			entities = getEntitiesAroundPoint(center, curr);
		}
		
		if (entities.isEmpty()) {
			return null;
		} else if (entities.size() == 1) {
			return entities.iterator().next();
		}
		
		double dist = radius * radius;
		Entity closest = null;
		
		for (Entity e : entities) {
			double d = e.getLocation().distance(center);
			if (d < dist) {
				closest = e;
				dist = d;
			}
		}
		
		return closest;
	}
	
	public static Collection<Entity> getEntitiesAroundPoint(Location center, double radius) {
		return getEntitiesAroundPoint(center, radius, DEFAULT_FILTER);
	}
	
	public static Collection<Entity> getEntitiesAroundPoint(Location center, double radius, Predicate<Entity> filter) {
		return center.getWorld().getNearbyEntities(center, radius, radius, radius, filter);
	}

	public static Location getMainHandLocation(Player player) {
		if (player.getMainHand() == MainHand.LEFT) {
			return moveLeft(player.getLocation().add(0, 1.2, 0), 0.55);
		} else {
			return moveRight(player.getLocation().add(0, 1.2, 0), 0.55);
		}
	}
	
	public static Block getTopBlock(Location loc, int range) {
		Location curr = loc.clone();
		int delta = 0;
		
		while (isAir(curr.getBlock()) && delta * delta < range * range) {
			curr.subtract(0, 1, 0);
			delta++;
		}
		
		delta = 0;
		
		while (!isAir(curr.getBlock().getRelative(BlockFace.UP)) && delta * delta < range * range) {
			curr.add(0, 1, 0);
			delta++;
		}
		
		return curr.getBlock();
	}
	
	public static boolean isAir(Block block) {
		return isAir(block.getType());
	}
	
	public static boolean isAir(Location loc) {
		return isAir(loc.getBlock().getType());
	}
	
	public static boolean isAir(Material mat) {
		return mat == Material.AIR || mat == Material.CAVE_AIR || mat == Material.VOID_AIR;
	}
	
	public static boolean isObstructed(Location from, Location to) {
		return isObstructed(from, to, PASSABLE_FILTER);
	}
	
	public static boolean isObstructed(Location from, Location to, Predicate<Block> obstructed) {
		Validate.isTrue(from.getWorld().equals(to.getWorld()), "Locations must be in same world!");
		
		Vector dir = VectorUtil.direction(from, to).normalize().multiply(0.25);
		double dist = from.distance(to);
		
		Location current = from.clone();
		
		for (double d = 0; d < dist; d += 0.25) {
			current.add(dir);
			
			if (obstructed.test(current.getBlock())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean matches(Location a, Location b, boolean position, boolean direction, boolean world) {
		if (position) {
			if (a.getX() != b.getX()) {
				return false;
			} else if (a.getY() != b.getY()) {
				return false;
			} else if (a.getZ() != b.getZ()) {
				return false;
			}
		}
		
		if (direction) {
			if (a.getYaw() != b.getYaw()) {
				return false;
			} else if (a.getPitch() != b.getPitch()) {
				return false;
			}
		}
		
		if (world) {
			if (!a.getWorld().equals(b.getWorld())) {
				return false;
			}
		}
		
		return true;
	}
	
	public static Location moveLeft(Location loc, double distance) {
		double angle = Math.toRadians(loc.getYaw() / 60);
		return loc.add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}
	
	public static Location moveRight(Location loc, double distance) {
		double angle = Math.toRadians(loc.getYaw() / 60);
		return loc.subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}
	
	public static FallingBlock spawnFallingBlock(Location loc, Material mat) {
		return spawnFallingBlock(loc, mat.createBlockData());
	}
	
	public static FallingBlock spawnFallingBlock(Location loc, BlockData data) {
		return loc.getWorld().spawnFallingBlock(loc, data);
	}
	
}
