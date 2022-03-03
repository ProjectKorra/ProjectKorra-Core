package com.projectkorra.core.game.firebending.flamingwall;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class FlamingWall extends AbilityInstance {

    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute("raise_radius")
    private double raiseRadius;
    @Attribute("shove_range")
    private double shoveRange;
    @Attribute(Attribute.WIDTH)
    private double width;
    @Attribute(Attribute.HEIGHT)
    private double height;
    @Attribute("shove_knockback")
    private double shoveKnockback;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute("shove_speed")
    private double shoveSpeed;

    private Location loc, shoveStart;
    private Vector dir;
    private boolean shoved = false;

    public FlamingWall(Ability provider, AbilityUser user, double damage, double raiseRadius, double width, double height, double shoveRange, double shoveSpeed, double shoveKnockback, long cooldown) {
        super(provider, user);
        this.damage = damage;
        this.raiseRadius = raiseRadius;
        this.width = width;
        this.height = height;
        this.shoveRange = shoveRange;
        this.shoveSpeed = shoveSpeed;
        this.shoveKnockback = shoveKnockback;
        this.cooldown = cooldown;
    }

    @Override
    protected void onStart() {
        this.loc = user.getLocation();
        this.dir = user.getDirection();
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (shoved) {
            if (shoveStart.distanceSquared(loc) >= shoveRange * shoveRange) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void postUpdate() {
        
    }

    @Override
    protected void onStop() {
        user.addCooldown(this.getProvider(), cooldown);
    }

    @Override
    public String getName() {
        return provider.getName();
    }
    
    public void click() {
        if (shoved) {
            return;
        }

        shoved = true;

    }

    public void releaseSneak() {
        if (shoved) {
            return;
        }

        AbilityManager.remove(this);
    }
}
