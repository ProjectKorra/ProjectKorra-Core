package com.projectkorra.core.util.material;

import java.util.Arrays;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.inventory.ItemStack;

/**
 * A class to help package Material related methods frequently used throughout ProjectKorra by developers.
 */
public class MaterialUtil {

	private MaterialUtil() {}
	
	private static final Material[] COOKABLES = { Material.BEEF, Material.CACTUS, Material.CHICKEN, Material.COD, Material.KELP, Material.MUTTON, Material.PORKCHOP, Material.POTATO, Material.RABBIT, Material.SALMON, Material.SEA_PICKLE, Material.WET_SPONGE };
	private static final Material[] FLUID_CONTAINERS = { Material.POTION, Material.WATER_BUCKET };
	private static final Material[] FREEZABLES = { Material.LAVA, Material.WATER };
	private static final Material[] MELTABLES = { Material.BLUE_ICE, Material.ICE, Material.PACKED_ICE, Material.SNOW, Material.SNOW_BLOCK};
	
	// TODO update API so that the below don't cause compilation errors.
	
	// private static final Material[] PLANTS = { Material.ACACIA_SAPLING, Material.ACACIA_LEAVES, Material.ALLIUM, Material.ATTACHED_MELON_STEM, Material.ATTACHED_PUMPKIN_STEM, Material.AZURE_BLUET, Material.BAMBOO, Material.BAMBOO_SAPLING, Material.BEETROOTS, Material.BIRCH_LEAVES, Material.BIRCH_SAPLING, Material.BLUE_ORCHID, Material.BROWN_MUSHROOM, Material.BROWN_MUSHROOM_BLOCK, Material.CACTUS, Material.CARROTS, Material.CHORUS_FLOWER, Material.CHORUS_PLANT, Material.COCOA, Material.CORNFLOWER, Material.CRIMSON_FUNGUS, Material.CRIMSON_ROOTS, Material.DANDELION, Material.DARK_OAK_LEAVES, Material.DARK_OAK_SAPLING, Material.FERN, Material.GRASS, Material.JUNGLE_LEAVES, Material.JUNGLE_SAPLING, Material.KELP, Material.KELP_PLANT, Material.LILAC, Material.LILY_OF_THE_VALLEY, Material.LILY_PAD, Material.MELON, Material.MELON_STEM, Material.OAK_LEAVES, Material.OAK_SAPLING, Material.ORANGE_TULIP, Material.OXEYE_DAISY, Material.PEONY, Material.PINK_TULIP, Material.POPPY, Material.POTATOES, Material.POTTED_ACACIA_SAPLING, Material.POTTED_ALLIUM, Material.POTTED_AZURE_BLUET, Material.POTTED_BAMBOO, Material.POTTED_BIRCH_SAPLING, Material.POTTED_BLUE_ORCHID, Material.POTTED_BROWN_MUSHROOM, Material.POTTED_CACTUS, Material.POTTED_CORNFLOWER, Material.POTTED_CRIMSON_FUNGUS, Material.POTTED_CRIMSON_ROOTS, Material.POTTED_DANDELION, Material.POTTED_DARK_OAK_SAPLING, Material.POTTED_FERN, Material.POTTED_JUNGLE_SAPLING, Material.POTTED_LILY_OF_THE_VALLEY, Material.POTTED_OAK_SAPLING, Material.POTTED_ORANGE_TULIP, Material.POTTED_OXEYE_DAISY, Material.POTTED_PINK_TULIP, Material.POTTED_POPPY, Material.POTTED_RED_MUSHROOM, Material.POTTED_RED_TULIP, Material.POTTED_SPRUCE_SAPLING, Material.POTTED_WARPED_FUNGUS, Material.POTTED_WARPED_ROOTS, Material.POTTED_WHITE_TULIP, Material.POTTED_WITHER_ROSE, Material.PUMPKIN, Material.PUMPKIN_STEM, Material.RED_TULIP, Material.ROSE_BUSH, Material.SEAGRASS, Material.SHROOMLIGHT, Material.SPRUCE_LEAVES, Material.SPRUCE_SAPLING, Material.SUGAR_CANE, Material.SUNFLOWER, Material.SWEET_BERRY_BUSH, Material.TALL_GRASS, Material.TALL_SEAGRASS, Material.TWISTING_VINES, Material.TWISTING_VINES_PLANT, Material.VINE, Material.WARPED_FUNGUS, Material.WARPED_ROOTS, Material.WEEPING_VINES, Material.WEEPING_VINES_PLANT, Material.WHEAT, Material.WHITE_TULIP, Material.WITHER_ROSE};
	// private static final Material[] ROCKS = { Material.ANCIENT_DEBRIS, Material.ANDESITE, Material.ANDESITE_SLAB, Material.ANDESITE_STAIRS, Material.ANDESITE_WALL, Material.BLACK_CONCRETE, Material.BLACK_GLAZED_TERRACOTTA, Material.BLACK_TERRACOTTA, Material.BLUE_CONCRETE, Material.BLUE_GLAZED_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BRICK, Material.BRICK_SLAB, Material.BRICK_STAIRS, Material.BRICK_WALL, Material.BROWN_CONCRETE, Material.BROWN_GLAZED_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.CHISELED_NETHER_BRICKS, Material.CHISELED_POLISHED_BLACKSTONE, Material.CHISELED_QUARTZ_BLOCK, Material.CHISELED_RED_SANDSTONE, Material.CHISELED_SANDSTONE, Material.CHISELED_STONE_BRICKS, Material.CLAY, Material.COAL_BLOCK, Material.COAL_ORE, Material.COARSE_DIRT, Material.COBBLESTONE, Material.COBBLESTONE_SLAB, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_WALL, Material.CRACKED_NETHER_BRICKS, Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.CRYING_OBSIDIAN, Material.EMERALD_BLOCK, Material.EMERALD_ORE, Material.END_STONE, Material.END_STONE_BRICK_SLAB, Material.END_STONE_BRICK_STAIRS, Material.END_STONE_BRICK_WALL, Material.END_STONE_BRICKS, Material.FARMLAND, Material.GILDED_BLACKSTONE, Material.GLOWSTONE, Material.GRANITE, Material.GRANITE_SLAB, Material.GRANITE_STAIRS, Material.GRANITE_WALL, };
	
	/**
	 * Determines whether the given Block has a material type corresponding to air.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and material is AIR or CAVE_AIR, false otherwise
	 */
	public static boolean isAir(Block block) {
		return block != null && isAir(block.getType());
	}

	/**
	 * Determines whether the given BlockData has a material type corresponding to air.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if data is not null and material is AIR or CAVE_AIR, false otherwise
	 */
	
	public static boolean isAir(@Nonnull BlockData data) {
		return data != null && isAir(data.getMaterial());
	}
	
	/**
	 * Determines whether the given Material corresponds to air.
	 * 
	 * @param material - the Material to be compared
	 * @return true if material equals AIR or CAVE_AIR
	 */
	public static boolean isAir(Material material) {
		return material == Material.AIR|| material == Material.CAVE_AIR;
	}

	/**
	 * 
	 */
	public static boolean isCookable(@Nonnull ItemStack stack) {
		return stack != null && isCookable(stack.getType());
	}

	/** Determines whether a material can be cooked.
	 * 
	 * @param material
	 * @return true if material is in list of cookable materials.
	 */
	public static boolean isCookable(@Nonnull Material material) {
		return Arrays.asList(COOKABLES).contains(material);
	}

	/**
	 * Determines whether the given Block is a type which corresponds to a fluid.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and block's material is LAVA or WATER.
	 */
	public static boolean isFluid(@Nonnull Block block) {
		return block != null && isFluid(block.getType());
	}
	
	/**
	 * Determines whether the given BlockData corresponds to a fluid.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if the data is not null and data's material is LAVA or WATER.
	 */
	public static boolean isFluid(@Nonnull BlockData data) {
		return data != null && isFluid(data.getMaterial());
	}
	
	/**
	 * Determines whether the given Material is a type which corresponds to a fluid.
	 * 
	 * @param material - the Material to be compared
	 * @return true if the material is LAVA or WATER.
	 */
	public static boolean isFluid(@Nonnull Material material) {
		return material == Material.WATER || material == Material.LAVA;
	}
	
	/**
	 * Determines whether the given Block corresponds to Water.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and block's type is Water.
	 */
	public static boolean isWater(@Nonnull Block block) {
		return block != null && isWater(block.getType());
	}

	/**
	 * Determines whether the given BlockData corresponds to Water.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if data is not null and data's material equals Water.
	 */
	public static boolean isWater(@Nonnull BlockData data) {
		return data != null && isWater(data.getMaterial());
	}

	/**
	 * Determines whether the given Material corresponds to Water.
	 * 
	 * @param material - the Material to be compared
	 * @return true if material equals Water.
	 */
	public static boolean isWater(@Nonnull Material material) {
		return material == Material.WATER;
	}

	/**
	 * Determines whether the given Block is waterlogged.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and block's data corresponds to a Waterlogged block.
	 */
	public static boolean isWaterlogged(@Nonnull Block block) {
		return block != null && isWaterlogged(block.getBlockData());
	}
	
	/**
	 * Determines whether the given BlockData corresponds to a Waterlogged block.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if data is not null and data is an instance of Waterlogged.
	 */
	public static boolean isWaterlogged(@Nonnull BlockData data) {
		return data != null && data instanceof Waterlogged;
	}

	/**
	 * Determines whether the given Block contains water.
	 * @param block - the Block to be checked
	 * @return true if block isWater, isWaterlogged, or is a cauldron that contains water.
	 */
	public static boolean containsWater(@Nonnull Block block) {
		return block != null && containsWater(block.getBlockData());
	}

	/**
	 * Determines whether the given BlockData contains water.
	 * @param data - the BlockData to be checked
	 * @return true if block isWater, isWaterlogged, or is a cauldron that contains water.
	 */
	public static boolean containsWater(@Nonnull BlockData data) {
		if (isWater(data) || isWaterlogged(data)){
			return true;
		} else if (data != null && data.getMaterial() == Material.CAULDRON && data instanceof Levelled) {
			Levelled waterLevel = (Levelled) data;
			return waterLevel.getLevel() > 0;
		} else {
			return false;
		}
	}
}
