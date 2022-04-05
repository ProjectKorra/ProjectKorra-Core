package com.projectkorra.core.game.firebending.fireball;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.type.BlastInstance;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;

public class FireballBlast extends FireAbilityInstance implements Collidable {

    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;

    private double size = 0.2;
    private Collider collider;
    private BlastInstance blast;

    public FireballBlast(FireballAbility provider, AbilityUser user) {
        super(provider, user);
        this.range = provider.blastRange;
        this.speed = provider.blastSpeed;
        this.damage = provider.blastDamage;
        this.cooldown = provider.blastCooldown;
    }

    @Override
    protected void onStart() {
        this.blast = new BlastInstance(user, (loc) -> loc.getBlock().isPassable());
        this.blast.setSpeed(speed);
        this.collider = new Collider(blast.getLocation());
        user.addCooldown(provider, cooldown);
        Effects.playSound(blast.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (!blast.onUpdate(timeDelta)) {
            return false;
        } else if ((range -= speed * timeDelta) <= 0) {
            return false;
        }

        this.collider.clear();
        this.collider.shift(blast.getLocation());
        
        Vector dir = blast.getDirection();
        double yaw = Math.toRadians(Vectors.getYaw(dir)), pitch = Math.toRadians(Vectors.getPitch(dir));
        double xz = Math.cos(pitch);
        double x = Math.cos(yaw) * xz * timeDelta * speed + 1.5 * size;
        double y = Math.sin(pitch) * timeDelta * speed + 1.5 * size;
        double z = Math.sin(yaw) * xz * timeDelta * speed + 1.5 * size;

        this.collider.add(BoundingBox.of(blast.getLocation(), x, y, z));
        this.particles(blast.getLocation(), (int) (size * 50), size, size, size);

        if (this.ticksLived() % 4 == 0) {
            Effects.playSound(blast.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1f, 1.3f);
        }

        RayTraceResult ray = blast.rayTrace(speed * timeDelta, FluidCollisionMode.ALWAYS, true, 2 * size + 0.1);
        if (ray != null) {
            if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity  && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
                Effects.damage((LivingEntity) ray.getHitEntity(), damage, this, false);
                ((LivingEntity)ray.getHitEntity()).setNoDamageTicks(0);
                Particles.spawn(Particle.SMOKE_LARGE, blast.getLocation(), 4, size, size, size, 0.1, null);
                return false;
            }

            if (ray.getHitBlock() != null) {
                Particles.spawnBlockDust(ray.getHitBlock().getBlockData(), blast.getLocation(), 6);
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onStop() {}

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
    public void remove() {
        AbilityManager.remove(this);
    }

	@Override
	protected void postUpdate() {}   
}
