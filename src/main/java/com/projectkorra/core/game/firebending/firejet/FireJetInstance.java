package com.projectkorra.core.game.firebending.firejet;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Sound;
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

    public FireJetInstance(FireJetAbility provider, AbilityUser user) {
        super(provider, user);
        this.maxSpeed = provider.speed;
        this.duration = provider.duration;
        this.cooldown = provider.cooldown;
        this.acceleration = provider.acceleration;
    }

    @Override
    protected void onStart() {
        Effects.playSound(user.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.6f);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (this.timeLived() >= duration) {
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

        if (user.getLocation().getWorld().rayTraceBlocks(user.getLocation(), new Vector(0, -1, 0), 0.5, FluidCollisionMode.NEVER) != null) {
            Particles.firebending(user.getLocation(), 13, 0.4, 0.1, 0.4);
            velocity.setY(0.5 * (0.4 - (user.getLocation().getY() - user.getLocation().getBlockY())));
        }

        user.getEntity().setVelocity(velocity);
        user.getEntity().setFallDistance(0);
        Particles.firebending(user.getLocation(), 7, 0.2, 0.2, 0.2);
        return true;
    }

    @Override
    protected void postUpdate() {}

    @Override
    protected void onStop() {
        user.addCooldown(provider, cooldown);
    }

    @Override
    public String getName() {
        return provider.getName();
    }
    
    
}
