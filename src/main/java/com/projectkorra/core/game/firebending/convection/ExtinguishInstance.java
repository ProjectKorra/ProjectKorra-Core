package com.projectkorra.core.game.firebending.convection;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.Blocks;

public class ExtinguishInstance extends FireAbilityInstance {

	private static Material[] STONES = { Material.GRANITE, Material.STONE, Material.ANDESITE, Material.DIORITE };

	@Attribute(value = Attribute.RANGE, group = AttributeGroup.RANGE)
	private double range;
	@Attribute(value = Attribute.RADIUS, group = AttributeGroup.SIZE)
	private double radius;
	@Attribute(value = Attribute.COOLDOWN, group = AttributeGroup.COOLDOWN)
	private long cooldown;
	@Attribute(value = Attribute.STAMINA_COST, group = AttributeGroup.STAMINA)
	private double staminaCost;

	private Set<Block> affected = new HashSet<>();

	public ExtinguishInstance(Convection provider, AbilityUser user) {
		super(provider, user);
		this.range = provider.extinguishRange;
		this.radius = provider.extinguishRadius;
		this.cooldown = provider.extinguishCooldown;
		this.staminaCost = provider.extinguishStaminaCost;
	}

	@Override
	protected void onStart() {
		user.addCooldown(provider, cooldown);
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		Location eye = user.getEyeLocation();
		double inc = Math.min(0.1, 0.1 * range);
		Vector dir = eye.getDirection().multiply(inc);

		for (double d = 0; d < range; d += inc) {
			eye.add(dir);

			if (Tag.FIRE.isTagged(eye.getBlock().getType()) || eye.getBlock().getType() == Material.LAVA || eye.getBlock().getType().isSolid()) {
				break;
			}
		}

		for (int i = 0; i <= radius + 1; ++i) {
			Blocks.forNearby(eye, i, this::affect);
		}
		return false;
	}

	private void affect(Block block) {
		if (affected.contains(block)) {
			return;
		}

		BlockData data = null;
		if (Tag.FIRE.isTagged(block.getType())) {
			data = Material.AIR.createBlockData();
		} else if (block.getType() == Material.LAVA) {
			data = Material.MAGMA_BLOCK.createBlockData();
		} else if (block.getType() == Material.MAGMA_BLOCK) {
			data = STONES[ThreadLocalRandom.current().nextInt(STONES.length)].createBlockData();
		} else {
			return;
		}

		if (!user.getStamina().consume(staminaCost / (radius * radius * radius))) {
			return;
		}

		block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.2f, 1.8f);
		TempBlock.from(block).setData(data);
		affected.add(block);
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return provider.getName() + "Extinguish";
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
