package com.projectkorra.core.game.firebending.firewheel;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.data.RemovalPolicy;

public class FireWheelInstance extends FireAbilityInstance {
	
	@Attribute(RANGE)
	private double range;
	@Attribute(DAMAGE)
	private double damage;
	@Attribute(SPEED)
	private double speed;
	@Attribute(RADIUS)
	private double radius;
	
	private Location loc;
	private Vector dir;
	private double travelled = 0, angle = 0;

	public FireWheelInstance(FireWheel provider, AbilityUser user) {
		super(provider, user);
		this.range = provider.range;
		this.damage = provider.damage;
		this.speed = provider.speed;
		this.radius = provider.radius;
	}

	@Override
	protected boolean onStart() {
		removal.add(RemovalPolicy.COMMON);
		removal.add((inst) -> travelled > range);
		dir = Vectors.direction(user.getLocation().getYaw(), 0);
		loc = user.getLocation();
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		double move = speed * timeDelta;
		travelled += move;
		loc.add(dir.normalize().multiply(move));
		
		Block top = Blocks.findTop(loc, radius);
		if (top.isPassable() || top.getRelative(BlockFace.UP).isLiquid()) {
			return false;
		}
		
		loc.setY(top.getY() + 1);
		angle += speed / radius;
		
		this.particles(loc.clone().add(0, radius, 0).add(dir.clone().normalize().multiply(Math.cos(angle)).setY(Math.sin(angle)).multiply(radius)), 2, 0.05, 0.05, 0.05);
		
		/*
		for (double d = 0; d < 2 * Math.PI; d += move / radius) {
			Vector onCircle = dir.clone().normalize().multiply(Math.cos(d + angle)).setY(Math.sin(d + angle)).multiply(radius);
			display.add(onCircle);
			display.getWorld().spawnParticle(getParticle(), display, 1, 0.05, 0.05, 0.05, 0, null);
			display.subtract(onCircle);
		}
		*/
		
		return true;
	}

	@Override
	protected void preUpdate() {
		
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

}
