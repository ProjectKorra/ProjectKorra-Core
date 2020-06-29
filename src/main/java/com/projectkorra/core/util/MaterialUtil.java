package com.projectkorra.core.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;

public class MaterialUtil {
	
	private MaterialUtil() {}

	public static BlockData createLavaData(int level) {
		Levelled data = (Levelled) Material.LAVA.createBlockData();
		data.setLevel(level);
		return data;
	}
	
	public static BlockData createWaterData(int level) {
		Levelled data = (Levelled) Material.WATER.createBlockData();
		data.setLevel(level);
		return data;
	}
	
	public static boolean isWeapon(final Material mat) {
		if (mat == null) {
			return false;
		}
		
		switch (mat) {
			case WOODEN_AXE:
			case WOODEN_PICKAXE:
			case WOODEN_SHOVEL:
			case WOODEN_SWORD:
			case STONE_AXE:
			case STONE_PICKAXE:
			case STONE_SHOVEL:
			case STONE_SWORD:
			case IRON_AXE:
			case IRON_PICKAXE:
			case IRON_SHOVEL:
			case IRON_SWORD:
			case GOLDEN_AXE:
			case GOLDEN_PICKAXE:
			case GOLDEN_SHOVEL:
			case GOLDEN_SWORD:
			case DIAMOND_AXE:
			case DIAMOND_PICKAXE:
			case DIAMOND_SHOVEL:
			case DIAMOND_SWORD:
			//case NETHERITE_AXE:
			//case NETHERITE_PICKAXE:
			//case NETHERITE_SHOVEL:
			//case NETHERITE_SWORD:
				return true;
			default:
				return false;
		}
	}
}
