package com.projectkorra.core.game.firebending.blazingarc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;

public class BlazingArcInstance extends FireAbilityInstance implements Collidable {

	@Attribute(DAMAGE)
	private double damage;
	@Attribute(RANGE)
	private double range;
	@Attribute(SPEED)
	private double speed;
	@Attribute(COOLDOWN)
	private long cooldown;
	@Attribute(KNOCKBACK)
	private double knockback;

	private Location[] locs = new Location[3];
	private Location start;
	private Vector[] dirs = new Vector[3]; // 0 left, 1 mid, 2 right
	private double currRange = 0;
	private double angle, delay;
	private boolean started = false;
	private boolean[] blocked = new boolean[51];
	private Map<BoundingBox, Double> ts = new HashMap<>();
	private Collider collider;

	public BlazingArcInstance(BlazingArc provider, AbilityUser user) {
		super(provider, user);
		this.damage = provider.damage;
		this.range = provider.range;
		this.speed = provider.speed;
		this.cooldown = provider.cooldown;
		this.knockback = provider.knockback;
		this.delay = provider.delay;
	}

	@Override
	protected boolean onStart() {
		dirs[0] = user.getDirection();
		user.addCooldown(provider, cooldown);
		this.collider = new Collider(user.getLocation());
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (this.timeLived() <= delay) {
			return true;
		} else if (!started) {
			started = true;
			start = user.getEyeLocation();
			for (int i = 0; i < 3; ++i) {
				locs[i] = start.clone();
			}

			dirs[2] = user.getDirection();
			dirs[1] = dirs[0].clone().add(dirs[2]);
			angle = dirs[0].angle(dirs[2]);
		}

		currRange += speed * timeDelta;

		ts.clear();
		collider.shift(locs[1]);

		for (int i = 0; i < 3; ++i) {
			locs[i].add(dirs[i].normalize().multiply(speed * timeDelta * (i % 2 == 0 ? 0.7 : 1)));
		}

		Effects.playSound(locs[1], Sound.BLOCK_FIRE_AMBIENT, 1f, 1.8f);
		double increment = Math.min(1.0 / blocked.length, 0.25 / (angle * currRange));
		for (double t = 0; t <= 1; t += increment) {
			if (blocked[(int) Math.round(t * (blocked.length - 1))]) {
				continue;
			}

			Location display = bezier(t);
			display.setDirection(Vectors.direction(start, display));

			BoundingBox hitbox = BoundingBox.of(display, 0.1, 0.1, 0.1);
			ts.put(hitbox, t);
			collider.add(hitbox);

			if (Math.random() < 0.4) {
				particles(display, 1, 0, 0, 0);
			}

			if (rayCast(display, speed * timeDelta)) {
				blocked[(int) Math.round(t * (blocked.length - 1))] = true;
			}
		}

		if (currRange >= range) {
			return false;
		}

		return true;
	}

	@Override
	protected void preUpdate() {
		collider.clear();
	}

	@Override
	protected void postUpdate() {
		if (!started) {
			return;
		}
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return provider.getName();
	}

	// t is between 0 and 1
	private Location bezier(double t) {
		return locs[1].clone().add(locs[0].clone().subtract(locs[1]).multiply((1 - t) * (1 - t))).add(locs[2].clone().subtract(locs[1]).multiply(t * t));
	}

	private boolean rayCast(Location loc, double dist) {
		Vector kb = loc.getDirection().setY(0).multiply(knockback);
		Optional.ofNullable(loc.getWorld().rayTrace(loc, loc.getDirection(), dist, FluidCollisionMode.NEVER, true, 0.4, null)).map(RayTraceResult::getHitEntity).filter(e -> e instanceof LivingEntity && !e.getUniqueId().equals(user.getUniqueID()) && !Velocity.isAffected(e)).ifPresent(e -> {
			Effects.damage((LivingEntity) e, damage, this, false);
			Velocity.knockback(e, kb, this);
		});

		return Optional.ofNullable(loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), dist, FluidCollisionMode.ALWAYS)).filter(ray -> ray.getHitBlock() != null).isPresent();
	}

	@Override
	public String getTag() {
		return provider.getName();
	}

	@Override
	public Collider getHitbox() {
		return collider;
	}

	@Override
	public World getWorld() {
		return locs[1].getWorld();
	}

	@Override
	public void onCollide(BoundingBox hitbox) {
		if (ts.containsKey(hitbox)) {
			blocked[(int) Math.round(ts.get(hitbox) * (blocked.length - 1))] = true;
		}
	}
}
