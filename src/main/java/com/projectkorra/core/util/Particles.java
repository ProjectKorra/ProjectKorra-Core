package com.projectkorra.core.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Particles {
	
	private static final Map<Particle, Object> DEFAULT_DATAS = new HashMap<>();
	private static final DustOptions AIR_DUST = new DustOptions(Color.fromRGB(148, 207, 224), 0.5f);
	private static final DustTransition LIGHTNING_DUST = new DustTransition(Color.fromRGB(220, 255, 252), Color.fromRGB(0, 199, 191), 0.9f);
	
	static {
		DEFAULT_DATAS.put(Particle.REDSTONE, new DustOptions(Color.RED, 0.8f));
		DEFAULT_DATAS.put(Particle.BLOCK_CRACK, Material.BARRIER.createBlockData());
		DEFAULT_DATAS.put(Particle.BLOCK_DUST, Material.BARRIER.createBlockData());
		DEFAULT_DATAS.put(Particle.FALLING_DUST, Material.BARRIER.createBlockData());
		DEFAULT_DATAS.put(Particle.ITEM_CRACK, new ItemStack(Material.BARRIER));
	}
	
	private Particles() {}

	/**
	 * Spawn a single particle at the given location
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 */
	public static void spawn(Particle particle, Location loc) {
		spawn(particle, loc, 1, 0, 0, 0, 0, null);
	}
	
	/**
	 * Spawn a single particle at the given location with any additional data
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 * @param data additional data attached to the particle. See {@link Particle} for applicable data types
	 */
	public static void spawn(Particle particle, Location loc, Object data) {
		spawn(particle, loc, 1, 0, 0, 0, 0, data);
	}
	
	/**
	 * Spawn an amount of particles at the given location
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawn(Particle particle, Location loc, int amount) {
		spawn(particle, loc, amount, 0, 0, 0, 0, null);
	}
	
	/**
	 * Spawn an amount of particles at the given location and with any additional data
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param extra additional number attached to the particle (usually speed in some way)
	 */
	public static void spawn(Particle particle, Location loc, int amount, double extra) {
		spawn(particle, loc, amount, 0, 0, 0, extra, null);
	}
	
	/**
	 * Spawn an amount of particles at the given location and with any additional data
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param extra additional number attached to the particle (usually speed in some way)
	 * @param data additional data attached to the particle. See {@link Particle} for applicable data types
	 */
	public static void spawn(Particle particle, Location loc, int amount, double extra, Object data) {
		spawn(particle, loc, amount, 0, 0, 0, extra, data);
	}
	
	/**
	 * Spawn an amount of particles at the given location within the specified bounds and with any additional data
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 * @param extra additional number attached to the particle (usually speed in some way)
	 * @param data additional data attached to the particle. See {@link Particle} for applicable data types
	 */
	public static void spawn(Particle particle, Location loc, int amount, double offsets, double extra, Object data) {
		spawn(particle, loc, amount, offsets, offsets, offsets, extra, data);
	}
	
	/**
	 * Spawn an amount of particles at the given location within the specified bounds and with any additional data
	 * @param particle what particle to spawn
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 * @param extra additional number attached to the particle (usually speed in some way)
	 * @param data additional data attached to the particle. See {@link Particle} for applicable data types
	 */
	public static void spawn(Particle particle, Location loc, int amount, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
		if (data == null && DEFAULT_DATAS.containsKey(particle)) {
			data = DEFAULT_DATAS.get(particle);
		}
		
		loc.getWorld().spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, extra, data);
	}

	public static void airbending(Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		loc.getWorld().spawnParticle(Particle.REDSTONE, loc, amount, offsetX, offsetY, offsetZ, 0.5, AIR_DUST);
	}

	public static void firebending(Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		loc.getWorld().spawnParticle(Particle.FLAME, loc, amount, offsetX, offsetY, offsetZ, 0.02);
	}

	public static void lightning(Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, amount, offsetX, offsetY, offsetZ, 0.6, LIGHTNING_DUST);
	}

	/**
	 * Spawn a single BLOCK_CRACK particle with the given BlockData at the given Location
	 * @param block BlockData for the BLOCK_CRACK particle to show
	 * @param loc where to spawn the particle
	 */
	public static void spawnBlockCrack(BlockData block, Location loc) {
		spawnBlockCrack(block, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the BLOCK_CRACK particle with the given BlockData at the given Location
	 * @param block BlockData for the BLOCK_CRACK particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnBlockCrack(BlockData block, Location loc, int amount) {
		spawnBlockCrack(block, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the BLOCK_CRACK particle with the given BlockData at the given Location within the specified bounds
	 * @param block BlockData for the BLOCK_CRACK particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnBlockCrack(BlockData block, Location loc, int amount, double offsets) {
		spawnBlockCrack(block, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of the BLOCK_CRACK particle with the given BlockData at the given Location within the specified bounds
	 * @param block BlockData for the BLOCK_CRACK particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnBlockCrack(BlockData block, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		if (block == null) {
			block = Material.BARRIER.createBlockData();
		}
		
		spawn(Particle.BLOCK_CRACK, loc, amount, offsetX, offsetY, offsetZ, 0, block);
	}

	/**
	 * Spawn a single BLOCK_DUST particle with the given BlockData at the given Location
	 * @param block BlockData for the BLOCK_DUST particle to show
	 * @param loc where to spawn the particle
	 */
	public static void spawnBlockDust(BlockData block, Location loc) {
		spawnBlockDust(block, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the BLOCK_DUST particle with the given BlockData at the given Location
	 * @param block BlockData for the BLOCK_DUST particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnBlockDust(BlockData block, Location loc, int amount) {
		spawnBlockDust(block, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the BLOCK_DUST particle with the given BlockData at the given Location within the specified bounds
	 * @param block BlockData for the BLOCK_DUST particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnBlockDust(BlockData block, Location loc, int amount, double offsets) {
		spawnBlockDust(block, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of the BLOCK_DUST particle with the given BlockData at the given Location within the specified bounds
	 * @param block BlockData for the BLOCK_DUST particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnBlockDust(BlockData block, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		if (block == null) {
			block = Material.BARRIER.createBlockData();
		}
		
		spawn(Particle.BLOCK_DUST, loc, amount, offsetX, offsetY, offsetZ, 0, block);
	}

	/**
	 * Spawn a single FALLING_DUST particle with the given BlockData at the given Location
	 * @param block BlockData for the FALLING_DUST particle to show
	 * @param loc where to spawn the particle
	 */
	public static void spawnFallingDust(BlockData block, Location loc) {
		spawnFallingDust(block, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the FALLING_DUST particle with the given BlockData at the given Location
	 * @param block BlockData for the FALLING_DUST particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnFallingDust(BlockData block, Location loc, int amount) {
		spawnFallingDust(block, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the FALLING_DUST particle with the given BlockData at the given Location within the specified bounds
	 * @param block BlockData for the FALLING_DUST particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnFallingDust(BlockData block, Location loc, int amount, double offsets) {
		spawnFallingDust(block, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of the FALLING_DUST particle with the given BlockData at the given Location within the specified bounds
	 * @param block BlockData for the FALLING_DUST particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnFallingDust(BlockData block, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		if (block == null) {
			block = Material.BARRIER.createBlockData();
		}
		
		spawn(Particle.FALLING_DUST, loc, amount, offsetX, offsetY, offsetZ, 0, block);
	}

	/**
	 * Spawn a single ITEM_CRACK particle with the given ItemStack at the given Location
	 * @param item ItemStack for the ITEM_CRACK particle to show
	 * @param loc where to spawn the particle
	 */
	public static void spawnItemCrack(ItemStack item, Location loc) {
		spawnItemCrack(item, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the ITEM_CRACK particle with the given ItemStack at the given Location
	 * @param item ItemStack for the ITEM_CRACK particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnItemCrack(ItemStack item, Location loc, int amount) {
		spawnItemCrack(item, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of the ITEM_CRACK particle with the given ItemStack at the given Location within the specified bounds
	 * @param item ItemStack for the ITEM_CRACK particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnItemCrack(ItemStack item, Location loc, int amount, double offsets) {
		spawnItemCrack(item, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of the ITEM_CRACK particle with the given ItemStack at the given location within the specified bounds
	 * @param item ItemStack for the ITEM_CRACK particle to show
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnItemCrack(ItemStack item, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		if (item == null) {
			item = new ItemStack(Material.BARRIER);
		}
		
		spawn(Particle.ITEM_CRACK, loc, amount, offsetX, offsetY, offsetZ, 0, item);
	}

	/**
	 * Spawn a single colored NOTE particle at the given Location
	 * @param color which color the note should be
	 * @param loc where to spawn the particle
	 */
	public static void spawnColoredNote(NoteColor color, Location loc) {
		spawnColoredNote(color, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of colored NOTE particles at the given Location
	 * @param color which color the note should be
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnColoredNote(NoteColor color, Location loc, int amount) {
		spawnColoredNote(color, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of colored NOTE particles at the given Location within the specified bounds
	 * @param color which color the note should be
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnColoredNote(NoteColor color, Location loc, int amount, double offsets) {
		spawnColoredNote(color, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of colored NOTE particles at the given Location within the specified bounds
	 * @param color which color the note should be
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnColoredNote(NoteColor color, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		World world = loc.getWorld();
		double value = color.ordinal() / 24D;
		
		for (int i = 0; i < amount; i++) {
			double x = loc.getX() + offsetX * 2 * (Math.random() - 0.5);
			double y = loc.getY() + offsetY * 2 * (Math.random() - 0.5);
			double z = loc.getZ() + offsetZ * 2 * (Math.random() - 0.5);
			
			world.spawnParticle(Particle.NOTE, x, y, z, 0, value, 0, 0, 1);
		}
	}

	/**
	 * Spawn a single colored REDSTONE particle at the given Location and a size of 1
	 * @param color color of the particle
	 * @param loc where to spawn the particle
	 */
	public static void spawnColoredRedstone(Color color, Location loc) {
		spawnColoredRedstone(color, 1, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn a single colored REDSTONE particle at the given Location
	 * @param color color of the particle
	 * @param size size of the particle [0, ~4] (size seems to be capped around 4)
	 * @param loc where to spawn the particle
	 */
	public static void spawnColoredRedstone(Color color, float size, Location loc) {
		spawnColoredRedstone(color, size, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of colored REDSTONE particles at the given Location and a size of 1
	 * @param color color of the particle
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnColoredRedstone(Color color, Location loc, int amount) {
		spawnColoredRedstone(color, 1, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of colored REDSTONE particles at the given Location
	 * @param color color of the particle
	 * @param size size of the particle [0, ~4] (size seems to be capped around 4)
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnColoredRedstone(Color color, float size, Location loc, int amount) {
		spawnColoredRedstone(color, size, loc, amount, 0, 0, 0);
	}

	/**
	 * Spawn an amount of colored REDSTONE particles at the given Location within the specified bounds and a size of 1
	 * @param color color of the particle
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnColoredRedstone(Color color, Location loc, int amount, double offsets) {
		spawnColoredRedstone(color, 1, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of colored REDSTONE particles at the given Location within the specified bounds
	 * @param color color of the particle
	 * @param size size of the particle [0, ~4] (size seems to be capped around 4)
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnColoredRedstone(Color color, float size, Location loc, int amount, double offsets) {
		spawnColoredRedstone(color, size, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of colored REDSTONE particles at the given Location within the specified bounds
	 * @param color color of the particle
	 * @param size size of the particle [0, ~4] (size seems to be capped around 4)
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnColoredRedstone(Color color, float size, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		if (color == null) {
			color = Color.RED;
		}
		
		size = Math.max(0, size);
		
		spawn(Particle.REDSTONE, loc, amount, offsetX, offsetY, offsetZ, 0, new DustOptions(color, size));
	}

	/**
	 * Spawn a single colored SPELL particle at the given Location
	 * @param color color of the particle
	 * @param ambient if true, the particles will be considerably transparent
	 * @param loc where to spawn the particle
	 */
	public static void spawnColoredSpell(Color color, boolean ambient, Location loc) {
		spawnColoredSpell(color, ambient, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of colored SPELL particles at the given Location
	 * @param color color of the particle
	 * @param ambient if true, the particles will be considerably transparent
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 */
	public static void spawnColoredSpell(Color color, boolean ambient, Location loc, int amount) {
		spawnColoredSpell(color, ambient, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawn an amount of colored SPELL particles at the given Location within the specified bounds
	 * @param color color of the particle
	 * @param ambient if true, the particles will be considerably transparent
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnColoredSpell(Color color, boolean ambient, Location loc, int amount, double offsets) {
		spawnColoredSpell(color, ambient, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawn an amount of colored SPELL particles at the given Location within the specified bounds
	 * @param color color of the particle
	 * @param ambient if true, the particles will be considerably transparent
	 * @param loc where to spawn the particle
	 * @param amount how many of the particle to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnColoredSpell(Color color, boolean ambient, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		if (color == null) {
			color = Color.WHITE;
		}
		
		Particle spell = ambient ? Particle.SPELL_MOB_AMBIENT : Particle.SPELL_MOB;
		World world = loc.getWorld();
		double r = Math.max(1, Math.min(color.getRed(), 255)) / 255D;
		double g = Math.max(0, Math.min(color.getGreen(), 255)) / 255D;
		double b = Math.max(0, Math.min(color.getBlue(), 255)) / 255D;
		
		for (int i = 0; i < amount; i++) {
			double x = loc.getX() + offsetX * 2 * (Math.random() - 0.5);
			double y = loc.getY() + offsetY * 2 * (Math.random() - 0.5);
			double z = loc.getZ() + offsetZ * 2 * (Math.random() - 0.5);
			
			world.spawnParticle(spell, x, y, z, 0, r, g, b, 1);
		}
	}

	/**
	 * Spawns a single particle that moves from the given Location in the given direction using the magnitude of the direction vector as the speed
	 * @param particle a particle which can have direction applied to it
	 * @param direction which direction to move the particles
	 * @param loc where the particles spawn at
	 */
	public static void spawnWithDirection(DirectionalParticle particle, Vector direction, Location loc) {
		spawnWithDirection(particle, direction, direction.length(), loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawns a single particle that moves from the given Location in the given direction at the given speed 
	 * @param particle a particle which can have direction applied to it
	 * @param direction which direction to move the particles
	 * @param speed how fast to move the particle
	 * @param loc where the particles spawn at
	 */
	public static void spawnWithDirection(DirectionalParticle particle, Vector direction, double speed, Location loc) {
		spawnWithDirection(particle, direction, speed, loc, 1, 0, 0, 0);
	}
	
	/**
	 * Spawns an amount of particles that move from the given Location in the given direction at the given speed 
	 * @param particle a particle which can have direction applied to it
	 * @param direction which direction to move the particles
	 * @param speed how fast to move the particle
	 * @param loc where the particles spawn at
	 * @param amount how many particles to spawn
	 */
	public static void spawnWithDirection(DirectionalParticle particle, Vector direction, double speed, Location loc, int amount) {
		spawnWithDirection(particle, direction, speed, loc, amount, 0, 0, 0);
	}
	
	/**
	 * Spawns an amount of particles that move from the given Location within the specified bounds in the given direction at the given speed 
	 * @param particle a particle which can have direction applied to it
	 * @param direction which direction to move the particles
	 * @param speed how fast to move the particle
	 * @param loc where the particles spawn at
	 * @param amount how many particles to spawn
	 * @param offsets offset bounds for equal randomized spawning on each axis
	 */
	public static void spawnWithDirection(DirectionalParticle particle, Vector direction, double speed, Location loc, int amount, double offsets) {
		spawnWithDirection(particle, direction, speed, loc, amount, offsets, offsets, offsets);
	}
	
	/**
	 * Spawns an amount of particles that move from the given Location within the specified bounds in the given direction at the given speed 
	 * @param particle a particle which can have direction applied to it
	 * @param direction which direction to move the particles
	 * @param speed how fast to move the particle
	 * @param loc where the particles spawn at
	 * @param amount how many particles to spawn
	 * @param offsetX x-axis offset bounds for randomized spawning
	 * @param offsetY y-axis offset bounds for randomized spawning
	 * @param offsetZ z-axis offset bounds for randomized spawning
	 */
	public static void spawnWithDirection(DirectionalParticle particle, Vector direction, double speed, Location loc, int amount, double offsetX, double offsetY, double offsetZ) {
		World world = loc.getWorld();
		
		for (int i = 0; i < amount; i++) {
			double x = loc.getX() + offsetX * 2 * (Math.random() - 0.5);
			double y = loc.getY() + offsetY * 2 * (Math.random() - 0.5);
			double z = loc.getZ() + offsetZ * 2 * (Math.random() - 0.5);
			
			world.spawnParticle(particle.get(), x, y, z, 0, direction.getX(), direction.getY(), direction.getZ(), speed);
		}
	}
	
	public static enum NoteColor {
		GREEN, 
		LIME, 
		YELLOW, 
		GOLD, 
		ORANGE, 
		RED_ORANGE,
		RED,
		CRIMSON,
		FUSCHIA,
		MAGENTA,
		PINK,
		PURPLE,
		VIOLET,
		INDIGO,
		NAVY,
		BLUE,
		LIGHT_BLUE,
		SKY_BLUE,
		AQUA,
		TEAL,
		MALACHITE,
		VIVID_GREEN,
		DULL_GREEN,
		NEON_GREEN,
		GREEN_YELLOW;
	}
	
	public static enum DirectionalParticle {
		EXPLOSION_NORMAL (Particle.EXPLOSION_NORMAL),
		FIREWORKS_SPARK (Particle.FIREWORKS_SPARK),
		WATER_BUBBLE (Particle.WATER_BUBBLE),
		WATER_WAKE (Particle.WATER_WAKE),
		CRIT (Particle.CRIT),
		CRIT_MAGIC (Particle.CRIT_MAGIC),
		SMOKE_NORMAL (Particle.SMOKE_NORMAL),
		SMOKE_LARGE (Particle.SMOKE_LARGE),
		PORTAL (Particle.PORTAL),
		ENCHANTMENT_TABLE (Particle.ENCHANTMENT_TABLE),
		FLAME (Particle.FLAME),
		CLOUD (Particle.CLOUD),
		DRAGON_BREATH (Particle.DRAGON_BREATH),
		END_ROD (Particle.END_ROD),
		DAMAGE_INDICATOR (Particle.DAMAGE_INDICATOR),
		TOTEM (Particle.TOTEM),
		SPIT (Particle.SPIT),
		SQUID_INK (Particle.SQUID_INK),
		BUBBLE_POP (Particle.BUBBLE_POP),
		BUBBLE_COLUMN_UP (Particle.BUBBLE_COLUMN_UP),
		NAUTILUS (Particle.NAUTILUS),
		CAMPFIRE_COZY_SMOKE (Particle.CAMPFIRE_COSY_SMOKE), CAMPFIRE_COSY_SMOKE (Particle.CAMPFIRE_COSY_SMOKE),
		CAMPFIRE_SIGNAL_SMOKE (Particle.CAMPFIRE_SIGNAL_SMOKE);
		
		private Particle particle;
		private DirectionalParticle(Particle particle) {
			this.particle = particle;
		}
		
		public Particle get() {
			return particle;
		}
	}
}
