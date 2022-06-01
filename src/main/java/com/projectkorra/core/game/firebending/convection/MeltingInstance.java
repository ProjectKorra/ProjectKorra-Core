package com.projectkorra.core.game.firebending.convection;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.util.RayTraceResult;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.attribute.AttributeGroup;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.temporary.TempBlock;

public class MeltingInstance extends FireAbilityInstance {

	@Attribute(value = Attribute.STAMINA_DRAIN, group = AttributeGroup.STAMINA)
	private double staminaDrain;
	@Attribute(value = Attribute.RANGE, group = AttributeGroup.RANGE)
	private double range;
	@Attribute(value = "melt_time", group = AttributeGroup.CHARGE_TIME)
	private long meltTime;

	private Map<Block, Long> times = new HashMap<>();

	public MeltingInstance(Convection provider, AbilityUser user) {
		super(provider, user);
		this.staminaDrain = provider.meltStaminaDrain;
		this.range = provider.meltRange;
		this.meltTime = provider.meltTime;
	}

	@Override
	protected void onStart() {
		this.user.getStamina().pauseRegen(this);
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!user.getBoundAbility().filter((a) -> a == provider).isPresent()) {
			return false;
		}

		Location eye = user.getEyeLocation();
		RayTraceResult ray = eye.getWorld().rayTraceBlocks(eye, eye.getDirection(), range, FluidCollisionMode.NEVER, true);

		if (ray != null && Tag.ICE.isTagged(ray.getHitBlock().getType())) {
			if (!user.getStamina().consume(timeDelta * staminaDrain)) {
				return false;
			}

			long time = times.computeIfAbsent(ray.getHitBlock(), (b) -> 0L) + (long) (timeDelta * 1000);
			if (time >= meltTime) {
				TempBlock.from(ray.getHitBlock()).setData(Material.AIR.createBlockData());
				times.remove(ray.getHitBlock());
			} else {
				times.put(ray.getHitBlock(), time);
			}

			Location hit = ray.getHitPosition().toLocation(eye.getWorld());
			hit.getWorld().playSound(hit, Sound.BLOCK_FIRE_AMBIENT, 0.2f, 0.9f);
			this.particles(hit, 2, 0.2, 0.2, 0.2);
		}

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
		return provider.getName() + "Melt";
	}

	@Override
	protected void preUpdate() {
		// TODO Auto-generated method stub

	}

}
