package com.projectkorra.core.game.firebending.firejet;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Sound;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class FireJetInstance extends AbilityInstance {

    @Attribute("max_" + Attribute.SPEED)
    private double maxSpeed;
    @Attribute(Attribute.DURATION)
    private long duration;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute("acceleration")
    private double acceleration;
    @Attribute("stamina_cost")
    private double staminaCost;

    public FireJetInstance(FireJetAbility provider, AbilityUser user) {
        super(provider, user);
        this.maxSpeed = provider.speed;
        this.duration = provider.duration;
        this.cooldown = provider.cooldown;
        this.acceleration = provider.acceleration;
        this.staminaCost = provider.jetDrain;
    }

    @Override
    protected void onStart() {
        Effects.playSound(user.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (!user.getStamina().consume(timeDelta * staminaCost)) {
            return false;
        } else if (user.getLocation().getBlock().isLiquid()) {
            return false;
        } else if (!user.getBoundAbility().isPresent() || user.getBoundAbility().get() != provider) {
            return false;
        }

        Vector velocity = user.getDirection().multiply(acceleration * timeDelta);
        velocity.setY(velocity.getY() * 0.8);
        velocity.add(user.getEntity().getVelocity());
        if (velocity.length() > maxSpeed * timeDelta) {
            velocity.normalize().multiply(maxSpeed * timeDelta);
        }

        RayTraceResult ray = user.getLocation().getWorld().rayTraceBlocks(user.getLocation(), new Vector(0, -1, 0), 1.3, FluidCollisionMode.ALWAYS, true);
        if (ray != null) {
            Particles.firebending(user.getLocation(), 13, 0.4, 0.1, 0.4);
            velocity.setY(0.5 * (1.1 - (user.getLocation().getY() - ray.getHitBlock().getY() - 1)));
        }

        user.getEntity().setVelocity(velocity);
        user.getEntity().setFallDistance(0);
        Particles.firebending(user.getLocation(), 7, 0.2, 0.2, 0.2);
        Effects.playSound(user.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1f, 1.8f);
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
        return provider.getName();
    }
    
    
}
