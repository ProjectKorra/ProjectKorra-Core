package com.projectkorra.core.game.firebending.fireball;

import java.util.Optional;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.type.BlastInstance;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.data.RemovalPolicy;
import com.projectkorra.core.util.effect.Effect;

public class FireballInstance extends AbilityInstance implements Collidable {

	@Attribute(RANGE)
	private double range;
	@Attribute(SPEED)
	private double speed;
	@Attribute(DAMAGE)
	private double damage;
	@Attribute(COOLDOWN)
	private long cooldown;

	private double size = 0.25;
	private Collider collider;
	private BlastInstance blast;
	private Effect flames;

	public FireballInstance(Fireball provider, AbilityUser user, boolean combo) {
		super(provider, user);
		this.range = provider.blastRange;
		this.speed = combo ? provider.comboSpeed : provider.blastSpeed;
		this.damage = combo ? provider.comboDamage : provider.blastDamage;
		this.cooldown = combo ? provider.comboCooldown : provider.blastCooldown;
	}

	@Override
	protected boolean onStart() {
		this.blast = new BlastInstance(user, (loc) -> loc.getBlock().isPassable());
		this.blast.setSpeed(speed);
		this.collider = new Collider(blast.getLocation());
		user.addCooldown(provider, cooldown, true);
		Effects.playSound(blast.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
		removal.add(RemovalPolicy.COMMON);
		flames = Effect.builder().add(provider.getSkill().getParticle(user).offsets(size).amount((int) (size * 25))).build("fireball_flames", Optional.of(this));
		return true;
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (!blast.onUpdate(timeDelta)) {
			return false;
		} else if ((range -= speed * timeDelta) <= 0) {
			return false;
		}

		this.collider.shift(blast.getLocation());

		Vector dir = blast.getDirection();
		double yaw = Math.toRadians(Vectors.getYaw(dir)), pitch = Math.toRadians(Vectors.getPitch(dir));
		double xz = Math.cos(pitch);
		double x = Math.cos(yaw) * xz * timeDelta * speed + 1.5 * size;
		double y = Math.sin(pitch) * timeDelta * speed + 1.5 * size;
		double z = Math.sin(yaw) * xz * timeDelta * speed + 1.5 * size;

		this.collider.add(BoundingBox.of(blast.getLocation(), x, y, z));
		flames.spawn(blast.getLocation());

		if (this.ticksLived() % 4 == 0) {
			Effects.playSound(blast.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1f, 1.3f);
		}

		RayTraceResult ray = blast.rayTrace(speed * timeDelta, FluidCollisionMode.ALWAYS, true, 2 * size + 0.1);
		if (ray != null) {
			if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
				Effects.damage((LivingEntity) ray.getHitEntity(), damage, this, false);
				((LivingEntity) ray.getHitEntity()).setNoDamageTicks(0);
				Particles.spawn(Particle.SMOKE_LARGE, blast.getLocation(), 4, size, size, size, 0.1, null);
				return false;
			}

			if (ray.getHitBlock() != null) {
				Block place = ray.getHitBlock().getRelative(ray.getHitBlockFace());
				if (place.isPassable() && !Tag.FIRE.isTagged(place.getType()) && !place.isLiquid() && place.getRelative(BlockFace.DOWN).getType().isSolid()) {
					//TempBlock.from(place).setData(getFireType().createBlockData(), 10000);
				}

				Particles.spawnBlockDust(ray.getHitBlock().getBlockData(), blast.getLocation(), 6);
				return false;
			}
		}

		return true;
	}

	@Override
	protected void onStop() {
	}

	@Override
	public String getName() {
		return provider.getName();
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
		return blast.getLocation().getWorld();
	}

	@Override
	public void onCollide(BoundingBox hitbox, Location center) {
		AbilityManager.remove(this);
		//Particles.spawnBlockCrack(getFireType().createBlockData(), blast.getLocation(), 4);
	}

	@Override
	protected void preUpdate() {
		collider.clear();
	}
}
