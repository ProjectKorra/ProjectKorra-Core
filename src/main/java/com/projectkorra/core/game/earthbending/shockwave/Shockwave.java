package com.projectkorra.core.game.earthbending.shockwave;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.activation.Activation;
import com.projectkorra.core.ability.type.Bindable;
import com.projectkorra.core.game.AvatarSkills;
import com.projectkorra.core.game.BendingBlocks;
import com.projectkorra.core.temporary.TempBlock;
import com.projectkorra.core.util.configuration.Configure;

public class Shockwave extends Ability implements Bindable {

	@Configure
	double angle = 36;
	@Configure
	double range = 16;
	@Configure
	double speed = 20;
	@Configure
	double staminaCost = 0.3;
	@Configure
	long cooldown = 4500;
	@Configure
	double damage = 1;

	public Shockwave() {
		super("Shockwave", "Send a small shockwave through the earth to knock foes off balance", "ProjectKorra", "CORE", AvatarSkills.EARTHBENDING);
	}

	@Override
	public String getInstructions() {
		return "Click while on earth to send a shockwave forwards";
	}

	@Override
	protected AbilityInstance activate(AbilityUser user, Activation trigger, Event provider) {
		if (user.hasCooldown(this)) {
			return null;
		}

		if (trigger == Activation.LEFT_CLICK && BendingBlocks.isEarthbendable(user.getLocation().getBlock().getRelative(BlockFace.DOWN)) && user.getStamina().consume(staminaCost)) {
			return new ShockwaveInstance(this, user);
		}

		return null;
	}

	@EventHandler
	private void onPhysicsBlock(EntityChangeBlockEvent event) {
		if (!(event.getEntity() instanceof FallingBlock)) {
			return;
		} else if (!event.getEntity().hasMetadata("shockwave")) {
			return;
		}

		event.setCancelled(true);
		TempBlock.from(event.getBlock()).setData(((FallingBlock) event.getEntity()).getBlockData());
	}
}
