package com.projectkorra.core.game.firebending.flameguard;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.physics.Collider;

public class FlameShieldInstance extends FireAbilityInstance implements Collidable {
	
	@Attribute(Attribute.RADIUS)
	private double radius;
	@Attribute(Attribute.COOLDOWN)
	private long cooldown;
	@Attribute("stamina_drain")
	private double staminaDrain;
	@Attribute("collision_damage")
	private double collisionDamage;
	
	private Collider collider;
	private double angle = 0, health = 1;

	public FlameShieldInstance(FlameShield provider, AbilityUser user) {
		super(provider, user);
		this.radius = provider.radius;
		this.cooldown = provider.cooldown;
		this.staminaDrain = provider.staminaDrain;
		this.collisionDamage = provider.collisionDamage;
	}

	@Override
	protected void onStart() {
		this.collider = new Collider(user.getLocation());
		this.user.getStamina().pauseRegen(this);
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!this.user.getStamina().consume(timeDelta * staminaDrain)) {
			return false;
		}
		
		Location loc = user.getLocation().add(0, user.getEntity().getHeight() / 2, 0);
		collider.clear();
		collider.shift(loc);
		collider.add(BoundingBox.of(user.getLocation(), 4 * radius, 4 * radius, 4 * radius));
		
		for (double phi = -Math.PI / 2; phi <= Math.PI / 2; phi += Math.PI / 12) {
			double xz = Math.cos(phi);
			double sinPhi = Math.sin(phi);
			for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 12) {
				if (Math.random() > 0.6) {
					double x = loc.getX() - xz * Math.cos(theta + angle) * 1.5 * radius + 0.2 * (Math.random() - 0.5);
					double y = loc.getY() + sinPhi * 1.5 * radius + 0.2 * (Math.random() - 0.5);
					double z = loc.getZ() + xz * Math.sin(theta + angle) * 1.5 * radius + 0.2 * (Math.random() - 0.5);
					double ox = 0.1 * Math.cos(theta) * (Math.random() - 0.5);
					double oz = 0.1 * Math.sin(theta) * (Math.random() - 0.5);
					
					loc.getWorld().spawnParticle(getParticle(), x, y, z, 0, ox, 0.1, oz, 0.5);
				}
			}

			angle += 0.01;
		}
		return true;
	}

	@Override
	protected void postUpdate() {
	}

	@Override
	protected void onStop() {
		user.addCooldown(provider, cooldown);
	}

	@Override
	public String getName() {
		return "FlameShield";
	}

	@Override
	public String getTag() {
		return "FlameShield";
	}

	@Override
	public Collider getHitbox() {
		return collider;
	}

	@Override
	public World getWorld() {
		return user.getLocation().getWorld();
	}

	@Override
	public void remove() {
		health -= collisionDamage;
		if (health <= 0) {
			AbilityManager.remove(this);
		}
	}

}
