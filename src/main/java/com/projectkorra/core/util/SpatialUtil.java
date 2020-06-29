package com.projectkorra.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

import com.projectkorra.core.object.Angle;
import com.projectkorra.core.object.Plane;
import com.projectkorra.core.object.Angle.AngleMode;

public class SpatialUtil {
	
	private SpatialUtil() {}
	
	public static final Predicate<Entity> DEFAULT_FILTER = (entity) -> !(entity.isDead() || (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR));
	public static final Predicate<Block> PASSABLE_FILTER = (block) -> !block.isPassable();
	
	/**
	 * Create a List of locations for a horizontal circle with the given properties
	 * @param center center location of the circle
	 * @param radius distance from the center to any given point on the circle
	 * @param theta angular interval between points on the circle. <b>Using multiples of Math.PI is recommended</b>
	 * @return a List of points on the edge of a horizontal circle
	 */
	public static List<Location> getCircle(Location center, double radius, Angle theta) {
		return getCircle(center, radius, theta, Plane.XZ);
	}
	
	/**
	 * Create a List of locations for a circle with the given properties
	 * @param center center Location of the circle
	 * @param radius distance from the center to any given point on the circle
	 * @param theta angular interval between points on the circle.
	 * @param plane 
	 * @return a List of points on the edge of the circle
	 * @throws IllegalArgumentException when given Dimensions are the same
	 */
	public static List<Location> getCircle(Location center, double radius, Angle theta, Plane plane) {
		double interval = Math.abs(theta.getValue(AngleMode.RADIANS));
		
		Validate.isTrue(interval > 0, "Theta angle interval cannot be 0");
		
		List<Location> circle = new ArrayList<>();
		for (double angle = 0; angle < Math.PI * 2; angle += interval) {
			double av = Math.cos(angle) * radius;
			double bv = Math.sin(angle) * radius;
			
			Location point = center.clone();
			plane.moveAlongAxes(point, av, bv);
			circle.add(point);
		}
		
		return circle;
	}
	
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
	
	/**
	 * Create a List of locations for a sphere with the given properties
	 * @param center center Location of the sphere
	 * @param radius distance from the center to any given point on the sphere
	 * @param thetaInterval horizontal angular interval between points on the circle. <b>Using multiples of Math.PI is recommended</b>
	 * @param phiInterval vertical angular interval between points on the circle. <b>Using multiples of Math.PI is recommended</b>
	 * @return a List of locations on the edge of the sphere
	 */
	public static List<Location> getSphere(Location center, double radius, Angle thetaInterval, Angle phiInterval) {
		double tv = Math.abs(thetaInterval.getValue(AngleMode.RADIANS));
		double pv = Math.abs(phiInterval.getValue(AngleMode.RADIANS));
		
		Validate.isTrue(tv > 0, "Theta angle interval cannot be 0");
		Validate.isTrue(pv > 0, "Phi angle interval cannot be 0");
		
		List<Location> coords = new ArrayList<>();
		for (double theta = 0; theta < Math.PI; theta += tv) {
			for (double phi = 0; phi < Math.PI * 2; phi += pv) {
				double x = center.getX() + radius * Math.sin(theta) * Math.cos(phi);
				double y = center.getY() + radius * Math.cos(theta);
				double z = center.getZ() + radius * Math.sin(theta) * Math.sin(phi);
				
				coords.add(new Location(center.getWorld(), x, y, z));
			}
		}
		
		return coords;
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
