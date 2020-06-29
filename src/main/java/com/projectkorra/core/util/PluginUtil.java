package com.projectkorra.core.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;

public class PluginUtil {
	
	private static List<String> weapons = Arrays.asList("axe", "sword", "pickaxe", "shovel", "hoe")

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
	
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
	}
	
	public static boolean isWeapon(final Material mat) {
		if (mat == null) {
			return false;
		}
		
		String[] split = mat.toString().split("_");
		
		if (split.length != 2) { 
			return false;
		}
		
		return weapons.contains(split[1].toLowerCase());
	}
}
