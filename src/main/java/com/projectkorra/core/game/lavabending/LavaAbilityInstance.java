package com.projectkorra.core.game.lavabending;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;

public abstract class LavaAbilityInstance extends AbilityInstance {
	
	private static Material[] STONES = { Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE };
	private static Material[] LAVAS = {Material.LAVA, Material.MAGMA_BLOCK};

	public LavaAbilityInstance(Ability provider, AbilityUser user) {
		super(provider, user);
	}
	
	public Material getRandomStone() {
		return STONES[ThreadLocalRandom.current().nextInt(STONES.length)];
	}
	
	public Material getRandomLava() {
		return LAVAS[ThreadLocalRandom.current().nextInt(LAVAS.length)];
	}
}
