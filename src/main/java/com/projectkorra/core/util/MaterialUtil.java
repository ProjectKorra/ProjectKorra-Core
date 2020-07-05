package com.projectkorra.core.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;

public class MaterialUtil {

	private MaterialUtil() {}
	/**
	 * Determines whether the given Block has a material type corresponding to air.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and material is air or cave air, false otherwise
	 */
	public static boolean isAir(Block block) {
		return block != null && isAir(block.getType());
	}
	
	/**
	 * Determines whether the given BlockData has a material type corresponding to air.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if data is not null and material is air or cave air, false otherwise
	 */
	
	public static boolean isAir(BlockData data) {
		return data != null && isAir(data.getMaterial());
	}
	
	/**
	 * Determines whether the given Material corresponds to air.
	 * 
	 * @param material - the Material to be compared
	 * @return true if material equals AIR or CAVE AIR
	 */
	public static boolean isAir(Material material) {
		return material == Material.AIR|| material == Material.CAVE_AIR;
	}
	
	/**
	 * Determines whether the given Block is a type which corresponds to a fluid.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and block's data is an instanceof Levelled.
	 */
	public static boolean isFluid(Block block) {
		return block != null && isFluid(block.getBlockData());
	}
	
	/**
	 * Determines whether the given BlockData corresponds to a fluid. 
	 * This checks for whether the data is a type which can be levelled.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if the data is not null and data is an instance of Levelled.
	 */
	public static boolean isFluid(BlockData data) {
		return data != null && data instanceof Levelled;
	}
	
	/**
	 * Determines whether the given Material is a type which corresponds to a fluid.
	 * 
	 * @param material - the Material to be compared
	 * @return true if the material's generated block data is an instance of Levelled.
	 */
	public static boolean isFluid(Material material) {
		return isFluid(material.createBlockData());
	}
	
	/**
	 * Determines whether the given Block corresponds to Water.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and block's type is Water.
	 */
	public static boolean isWater(Block block) {
		return block != null && isWater(block.getType());
	}
	/**
	 * Determines whether the given BlockData corresponds to Water.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if data is not null and data's material equals Water.
	 */
	public static boolean isWater(BlockData data) {
		return data != null && isWater(data.getMaterial());
	}
	/**
	 * Determines whether the given Material corresponds to Water.
	 * 
	 * @param material - the Material to be compared
	 * @return true if material equals Water.
	 */
	public static boolean isWater(Material material) {
		return material == Material.WATER;
	}
	/**
	 * Determines whether the given Block is waterlogged.
	 * 
	 * @param block - the Block to be compared
	 * @return true if block is not null and block's data corresponds to a Waterlogged block.
	 */
	public static boolean isWaterlogged(Block block) {
		return block != null && isWaterlogged(block.getBlockData());
	}
	/**
	 * Determines whether the given BlockData corresponds to a Waterlogged block.
	 * 
	 * @param data - the BlockData to be compared
	 * @return true if data is not null and data is an instance of Waterlogged.
	 */
	public static boolean isWaterlogged(BlockData data) {
		return data != null && data instanceof Waterlogged;
	}
	
}
