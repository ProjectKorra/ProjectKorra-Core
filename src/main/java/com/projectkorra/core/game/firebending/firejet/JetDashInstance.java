package com.projectkorra.core.game.firebending.firejet;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;

import org.bukkit.Location;
import org.bukkit.Sound;

public class JetDashInstance extends AbilityInstance {

    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;

    public JetDashInstance(Ability provider, AbilityUser user, double speed, long cooldown) {
        super(provider, user);
        this.speed = speed;
        this.cooldown = cooldown;
    }

    @Override
    protected void onStart() {
        if (user.getEntity().isOnGround()) {
            speed *= 1.5;
        }

        Location loc = user.getLocation();
        if (loc.getPitch() < -35) {
            loc.setPitch(-35);
        }
        
        user.getEntity().setVelocity(loc.getDirection().multiply(speed));
        user.addCooldown("JetExtra", cooldown);
        user.getEntity().setFallDistance(0);
        Particles.firebending(user.getLocation(), 30, 0.4, 0.2, 0.4);
        Effects.playSound(user.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 0.4f);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        return false;
    }

    @Override
    protected void postUpdate() {}

    @Override
    protected void onStop() {}

    @Override
    public String getName() {
        return "JetDash";
    }
    
}
