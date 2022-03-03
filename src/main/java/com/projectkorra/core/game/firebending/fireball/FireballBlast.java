package com.projectkorra.core.game.firebending.fireball;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.type.BlastInstance;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;

public class FireballBlast extends BlastInstance {

    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;

    private double size = 0.2;

    public FireballBlast(FireballAbility provider, AbilityUser user) {
        super(provider, user, (loc) -> loc.getBlock().isPassable());
        this.range = provider.blastRange;
        this.speed = provider.blastSpeed;
        this.damage = provider.blastDamage;
        this.cooldown = provider.blastCooldown;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setSpeed(speed);
        user.addCooldown(provider, cooldown);
        Effects.playSound(location, Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (!super.onUpdate(timeDelta)) {
            return false;
        } else if ((range -= speed * timeDelta) <= 0) {
            return false;
        }

        Particles.firebending(location, (int) (size * 50), size, size, size);
        if (this.ticksLived() % 4 == 0) {
            Effects.playSound(location, Sound.BLOCK_FIRE_AMBIENT, 1f, 1.3f);
        }

        RayTraceResult ray = super.rayTrace(speed * timeDelta, FluidCollisionMode.ALWAYS, true, 2 * size + 0.1);
        if (ray != null) {
            if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity  && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
                Effects.damage((LivingEntity) ray.getHitEntity(), damage, this, false);
                ((LivingEntity)ray.getHitEntity()).setNoDamageTicks(0);
                Particles.spawn(Particle.SMOKE_LARGE, location, 4, size, size, size, 0.1, null);
                return false;
            }

            if (ray.getHitBlock() != null) {
                Particles.spawnBlockDust(ray.getHitBlock().getBlockData(), location, 6);
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onStop() {}

    @Override
    public String getName() {
        return provider.getName() + "Blast";
    }
    
}
