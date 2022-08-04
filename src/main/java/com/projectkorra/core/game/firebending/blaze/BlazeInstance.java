package com.projectkorra.core.game.firebending.blaze;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.Blocks;

public class BlazeInstance extends FireAbilityInstance {

	@Attribute(RANGE)
	private double range;
	@Attribute(DURATION)
	private long fireDuration;

	private double staminaDrain;
	private Location loc;
	private BlockData data;

	public BlazeInstance(Blaze provider, AbilityUser user) {
		super(provider, user);
		this.range = provider.range;
		this.staminaDrain = provider.staminaDrain;
		this.fireDuration = provider.fireDuration;
	}

	@Override
	protected boolean onStart() {
		Block target = user.getEntity().getTargetBlockExact((int) range, FluidCollisionMode.ALWAYS);
		if (target == null) {
			return false;
		}

		if (!Tag.FIRE.isTagged(target.getType())) {
			return false;
		}

		loc = target.getLocation();
		data = target.getBlockData();
		user.getStamina().pauseRegen(this);
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getStamina().consume(timeDelta * staminaDrain)) {
			return false;
		}

		Block target = user.getEntity().getTargetBlockExact((int) range, FluidCollisionMode.ALWAYS);
		if (target == null) {
			target = Blocks.findTop(user.getEyeLocation().add(user.getDirection().multiply(range)), 2);

			if (target == null) {
				return false;
			}
		}

		target = target.getRelative(BlockFace.UP);

		if ((!target.isPassable() && !Tag.FIRE.isTagged(target.getType())) || target.isLiquid()) {
			return false;
		} else if (target.getLocation().distance(loc) > 10) {
			return false;
		}

		for (int x = -1; x <= 1; ++x) {
			for (int z = -1; z <= 1; ++z) {
				Block block = Blocks.findTop(target.getRelative(x, 0, z).getLocation(), 2.0).getRelative(BlockFace.UP);
				if (!block.isPassable() || block.isLiquid() || Tag.FIRE.isTagged(block.getType())) {
					continue;
				} else if (block.getRelative(BlockFace.DOWN).isPassable()) {
					continue;
				} else if (TempBlock.exists(block)) {
					continue;
				}

				TempBlock.from(block).setData(data, fireDuration + (ThreadLocalRandom.current().nextLong(2000) - 1000));
			}
		}

		loc = target.getLocation();
		return true;
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return provider.getName();
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
