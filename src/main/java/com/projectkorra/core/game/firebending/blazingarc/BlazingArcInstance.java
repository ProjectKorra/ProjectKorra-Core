package com.projectkorra.core.game.firebending.blazingarc;

import java.util.Arrays;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BlazingArcInstance extends AbilityInstance {

    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute(Attribute.KNOCKBACK)
    private double knockback;
    @Attribute("Delay")
    private long delay;

    private Location[] locs;
    private Location start;
    private Vector[] dirs; // 0 left, 1 mid, 2 right
    private double currRange = 0;
    private double angle;
    private boolean started = false;
    private boolean[] blocked = new boolean[21];

    public BlazingArcInstance(Ability provider, AbilityUser user, double damage, double range, double speed, long cooldown, double knockback, long delay) {
        super(provider, user);
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.cooldown = cooldown;
        this.knockback = knockback;
        this.delay = delay;
        dirs = new Vector[3];
        locs = new Location[3];
        Arrays.fill(blocked, false);
    }

    @Override
    protected void onStart() {
        dirs[0] = user.getDirection();
        user.addCooldown(provider, cooldown);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (this.timeLived() <= delay) {
            return true;
        } else if (!started) {
            started = true;
            start = user.getEyeLocation();
            for (int i = 0; i < 3; ++i) {
                locs[i] = start.clone();
            }
            dirs[2] = user.getDirection();
            dirs[1] = dirs[0].clone().add(dirs[2]);
            angle = dirs[0].angle(dirs[2]);
        }

        currRange += speed * timeDelta;
        if (currRange > range) {
            return false;
        }

        for (int i = 0; i < 3; ++i) {
            locs[i].add(dirs[i].multiply(speed * timeDelta));
        }

        Effects.playSound(locs[1], Sound.BLOCK_FIRE_AMBIENT, 1f, 1.8f);
        for (double t = 0; t <= 1; t += 0.25 / (angle * currRange)) {
            if (blocked[(int) Math.round(t * (blocked.length - 1))]) {
                continue;
            }
            
            Location display = bezier(t);
            display.setDirection(Vectors.direction(start,  display));
            Particles.firebending(display, 2, 0.05, 0.05, 0.05);

            RayTraceResult ray = display.getWorld().rayTrace(display, display.getDirection(), speed * timeDelta, FluidCollisionMode.ALWAYS, true, 0.4, null);
            if (ray != null) {
                if (ray.getHitEntity() != null && !Velocity.isAffected(ray.getHitEntity()) && ray.getHitEntity() instanceof LivingEntity && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
                    Effects.damage((LivingEntity) ray.getHitEntity(), damage, this, false);
                    Velocity.apply(ray.getHitEntity(), display.getDirection().setY(0).multiply(knockback), this);
                }

                if (ray.getHitBlock() != null) {
                    blocked[(int) Math.round(t * (blocked.length - 1))] = true;
                }
            }
        }

        return true;
    }

    @Override
    protected void postUpdate() {
        if (!started) {
            return;
        }

        for (int i = 0; i < 3; ++i) {
            dirs[i].normalize();
        }
    }

    @Override
    protected void onStop() {}

    @Override
    public String getName() {
        return provider.getName();
    }
    
    // t is between 0 and 1
    private Location bezier(double t) {
        return locs[1].clone().add(locs[0].clone().subtract(locs[1]).multiply((1 - t) * (1 - t))).add(locs[2].clone().subtract(locs[1]).multiply(t * t));
    }
}
