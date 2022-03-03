package com.projectkorra.core.game.airbending.gust;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Velocity;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class GustStreamInstance extends AbilityInstance {

    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.KNOCKBACK)
    private double knockback;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute(Attribute.DURATION)
    private long duration;
    @Attribute("dynamicFactor")
    private double dynamicFactor;

    private double currentRange = 0;
    private double rangeIncrement;

    public GustStreamInstance(GustAbility provider, AbilityUser user) {
        super(provider, user);
        this.range = provider.streamRange;
        this.knockback = provider.streamKnockback;
        this.speed = provider.streamSpeed;
        this.cooldown = provider.streamCooldown;
        this.duration = provider.streamDuration;
        this.dynamicFactor = provider.streamDynamicFactor;
    }

    @Override
    protected void onStart() {
        this.rangeIncrement = Math.min(0.1 * range, 0.1);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (duration > 0 && this.timeLived() >= duration) {
            return false;
        }

        currentRange = Math.min(currentRange + speed * timeDelta, range);

        Location loc = user.getEyeLocation().subtract(0, 0.1, 0);
        Vector dir = loc.getDirection().multiply(rangeIncrement);

        for (double d = 0; d < currentRange; d += rangeIncrement) {
            Particles.airbending(loc.add(dir), (int) Math.ceil(d/range/4), d / range / 4, d / range / 4, d / range / 4);
            RayTraceResult ray = loc.getWorld().rayTrace(loc, loc.getDirection(), rangeIncrement, FluidCollisionMode.ALWAYS, true, 0.5 + d / 4, null);
            if (ray == null) {
                continue;
            }

            if (ray.getHitEntity() != null && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
                Velocity.apply(ray.getHitEntity(), loc.getDirection().multiply(knockback), this);
            }

            if (ray.getHitBlock() != null) {
                break;
            }
        }

        return true;
    }

    @Override
    protected void postUpdate() {}

    @Override
    protected void onStop() {
        user.addCooldown(provider, cooldown + (long) (dynamicFactor * (double) Math.min(this.timeLived(), duration)));
    }

    @Override
    public String getName() {
        return provider.getName() + "Stream";
    }
    
}
