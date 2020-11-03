package com.projectkorra.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.projectkorra.core.util.ParticleUtil.DirectionalParticle;
import com.projectkorra.core.util.ParticleUtil.NoteColor;
import com.projectkorra.core.util.shape.Shape;

public class ShapeUtil {

	private ShapeUtil() {}
	
	/**
	 * Take the given shape and construct its locations to the given collection
	 * @param shape shape to construct
	 * @param locs where to collect to
	 */
	public static void collect(Shape shape, Collection<Location> locs) {
		shape.construct((loc) -> locs.add(loc));
	}
	
	/**
	 * Take the given shape and construct only the locations which pass the
	 * filter test to the given collection
	 * @param shape shape to construct
	 * @param locs where to collect to
	 * @param filter any location that passes this test is added to the collection
	 */
	public static void collect(Shape shape, Collection<Location> locs, Predicate<Location> filter) {
		shape.construct((loc) -> {
			if (filter.test(loc)) {
				locs.add(loc);
			}
		});
	}
	
	/**
	 * Show the shape using the given particle
	 * @param shape shape to show
	 * @param particle particle to show with
	 */
	public static void showParticle(Shape shape, Particle particle) {
		shape.construct((loc) -> ParticleUtil.spawn(particle, loc));
	}
	
	/**
	 * Show the shape using BLOCK_CRACK and the given data
	 * @param shape shape to show
	 * @param block data to use for particle
	 */
	public static void showBlockCrack(Shape shape, BlockData block) {
		shape.construct((loc) -> ParticleUtil.spawnBlockCrack(block, loc));
	}
	
	/**
	 * Show the shape using BLOCK_DUST and the given data
	 * @param shape shape to show
	 * @param block data to use for particle
	 */
	public static void showBlockDust(Shape shape, BlockData block) {
		shape.construct((loc) -> ParticleUtil.spawnBlockDust(block, loc));
	}
	
	/**
	 * Show the shape using colored NOTE particles
	 * @param shape shape to show
	 * @param color note color
	 */
	public static void showColoredNote(Shape shape, NoteColor color) {
		shape.construct((loc) -> ParticleUtil.spawnColoredNote(color, loc));
	}
	
	/**
	 * Show the shape using colored REDSTONE particles
	 * @param shape shape to show
	 * @param color redstone color
	 * @param size redstone size
	 */
	public static void showColoredRedstone(Shape shape, Color color, float size) {
		shape.construct((loc) -> ParticleUtil.spawnColoredRedstone(color, size, loc));
	}
	
	/**
	 * Show the shape using colored MOB_SPELL particles
	 * @param shape shape to show
	 * @param color spell color
	 * @param ambient true for transparency
	 */
	public static void showColoredSpell(Shape shape, Color color, boolean ambient) {
		shape.construct((loc) -> ParticleUtil.spawnColoredSpell(color, ambient, loc));
	}
	
	/**
	 * Show the shape using a directional particle
	 * @param shape shape to show
	 * @param type particle to show
	 * @param direction to move particles in
	 */
	public static void showDirectional(Shape shape, DirectionalParticle type, Vector direction) {
		shape.construct((loc) -> ParticleUtil.spawnWithDirection(type, direction, loc));
	}
	
	/**
	 * Show the shape using FALLING_DUST particles
	 * @param shape shape to show
	 * @param block data to use for particles
	 */
	public static void showFallingDust(Shape shape, BlockData block) {
		shape.construct((loc) -> ParticleUtil.spawnFallingDust(block, loc));
	}
	
	/**
	 * Show the shape using ITEM_CRACK particles
	 * @param shape shape to show
	 * @param item data to use for particles
	 */
	public static void showItemCrack(Shape shape, ItemStack item) {
		shape.construct((loc) -> ParticleUtil.spawnItemCrack(item, loc));
	}
	
	/**
	 * Construct the given shape into a List
	 * @param shape shape to construct
	 * @return List of shape locations
	 */
	public static List<Location> toList(Shape shape) {
		List<Location> locs = new ArrayList<>();
		shape.construct((loc) -> locs.add(loc));
		return locs;
	}
	
	/**
	 * Construct the given shape into a Set
	 * @param shape shape to construct
	 * @return Set of shape locations
	 */
	public static Set<Location> toSet(Shape shape) {
		Set<Location> locs = new HashSet<>();
		shape.construct((loc) -> locs.add(loc));
		return locs;
	}
}
