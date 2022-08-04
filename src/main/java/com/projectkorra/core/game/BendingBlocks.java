package com.projectkorra.core.game;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import com.projectkorra.core.ProjectKorra;

public final class BendingBlocks {

	private BendingBlocks() {
	}

	private static boolean init = false;
	private static EnumSet<Material> earthbendable = EnumSet.noneOf(Material.class);
	private static EnumSet<Material> lavabendable = EnumSet.of(Material.LAVA, Material.MAGMA_BLOCK);

	public static void init(ProjectKorra plugin) {
		if (init) {
			return;
		}

		init = true;
		String[] keywords = { "grass_block", "deepslate", "stone", "dirt", "ore", "crystal", "mycelium", "andesite", "diorite", "granite", "sand" };

		for (Material mat : Material.values()) {
			if (!mat.isBlock() || mat.isInteractable()) {
				continue;
			}

			for (String keyword : keywords) {
				if (mat.toString().contains(keyword.toUpperCase())) {
					earthbendable.add(mat);
					break;
				}
			}
		}
	}

	public static Set<Material> getEarthbendables() {
		return EnumSet.copyOf(earthbendable);
	}

	public static boolean isEarthbendable(Block block) {
		return block != null && earthbendable.contains(block.getType());
	}

	public static boolean isEarthbendable(BlockData data) {
		return data != null && earthbendable.contains(data.getMaterial());
	}

	public static boolean isEarthbendable(Material mat) {
		return earthbendable.contains(mat);
	}

	public static Set<Material> getLavabendables() {
		return EnumSet.copyOf(lavabendable);
	}

	public static boolean isLavabendable(Block block) {
		return block != null && lavabendable.contains(block.getType());
	}

	public static boolean isLavabendable(BlockData data) {
		return data != null && lavabendable.contains(data.getMaterial());
	}

	public static boolean isLavabendable(Material mat) {
		return lavabendable.contains(mat);
	}
}
