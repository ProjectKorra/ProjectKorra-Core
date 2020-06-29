package com.projectkorra.core.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;

public class MaterialUtil {
	
	private MaterialUtil() {}

	public static BlockData createWaterData(int level) {
		Levelled data = (Levelled) Material.WATER.createBlockData();
		data.setLevel(level);
		return data;
	}
	
	public static BlockData createLavaData(int level) {
		Levelled data = (Levelled) Material.LAVA.createBlockData();
		data.setLevel(level);
		return data;
	}
}
