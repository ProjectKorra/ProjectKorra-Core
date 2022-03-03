package com.projectkorra.core.game.airbending.gust;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.ability.type.BlastInstance;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;

import org.bukkit.FluidCollisionMode;
import org.bukkit.util.RayTraceResult;

public class GustBlastInstance extends BlastInstance {

    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.KNOCKBACK)
    private double knockback;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;

    private double maxRange;

    public GustBlastInstance(GustAbility provider, AbilityUser user) {
        super(provider, user, (loc) -> loc.getBlock().isPassable());
        this.range = provider.blastRange;
        this.knockback = provider.blastKnockback;
        this.speed = provider.blastSpeed;
        this.cooldown = provider.blastCooldown;
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.setSpeed(this.speed);
        this.maxRange = range;
        user.addCooldown(provider, cooldown);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (!super.onUpdate(timeDelta)) {
            return false;
        } else if ((range -= speed * timeDelta) <= 0) {
            return false;
        }

        Particles.airbending(location, 4, 0.2, 0.2, 0.2);
        RayTraceResult ray = super.rayTrace(speed * timeDelta, FluidCollisionMode.ALWAYS, true, 0.8);
        if (ray != null) {
            if (ray.getHitEntity() != null && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
                Velocity.apply(ray.getHitEntity(), super.getDirection().multiply(knockback), this);
            }

            if (ray.getHitBlock() != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void postUpdate() {
        super.setDirection(Vectors.direction(location, user.getEyeLocation().add(user.getDirection().multiply(maxRange))));
    }

    @Override
    protected void onStop() {}

    @Override
    public String getName() {
        return provider.getName() + "Blast";
    }
    
}
