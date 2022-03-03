package com.projectkorra.core.game.lightningbending.bolt;

import java.util.concurrent.ThreadLocalRandom;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityManager;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;
import com.projectkorra.core.util.math.AngleType;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BoltInstance extends AbilityInstance {

    @Attribute(Attribute.CHARGE_DURATION + "_max")
    private long maxChargeTime;
    @Attribute(Attribute.CHARGE_DURATION + "_min")
    private long minChargeTime;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.SPEED)
    private double speed;
    @Attribute(Attribute.RANGE)
    private double range;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute("subarc_chance")
    private double subarcChance;
    
    private boolean shot = false;
    private Location loc;
    private double maxRange;

    public BoltInstance(BoltAbility provider, AbilityUser user, boolean subArc) {
        super(provider, user);
        this.damage = subArc ? provider.subarcDamage : provider.damage;
        this.speed = subArc ? provider.subarcSpeed : provider.speed;
        this.range = subArc ? provider.subarcRange : provider.range;
        this.cooldown = subArc ? 0 : provider.cooldown;
        this.minChargeTime = subArc ? 0 : provider.minChargeTime;
        this.maxChargeTime = subArc ? 0 : provider.maxChargeTime;
        this.subarcChance = subArc ? 0 : provider.subarcChance;
        this.maxRange = this.range;
    }

    @Override
    protected void onStart() {}

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (!shot && this.timeLived() <= maxChargeTime) {
            if (!user.getBoundAbility().isPresent() || user.getBoundAbility().get() != provider) {
                return false;
            }

            if (this.timeLived() >= minChargeTime) {
                double percent = ((double) this.timeLived() - minChargeTime) / ((double) maxChargeTime - minChargeTime);
                for (int i = 0; i < 2; ++i) {
                    Vector ortho = Vectors.orthogonal(user.getDirection(), AngleType.RADIANS, Math.random() * Math.PI * 2).get().multiply(0.9 * (1 - percent));
                    Particles.lightning(user.getEyeLocation().add(user.getDirection()).add(ortho), 1, 0, 0, 0);
                }
            } else {
                double percent = ((double) this.timeLived()) / (double) minChargeTime;
                Location display = user.getLocation().add(0, 0.5 + percent * 0.6, 0);
                display.setPitch(0);
                double angle = percent * 60;
                float yaw = display.getYaw();
                display.setYaw((float) (yaw + 90 - angle));
                Particles.lightning(display.add(display.getDirection().multiply(0.5)), 1, 0, 0, 0);
                display.subtract(display.getDirection().multiply(0.5));
                display.setYaw((float) (yaw - 90 + angle));
                Particles.lightning(display.add(display.getDirection().multiply(0.5)), 1, 0, 0, 0);
            }

            Effects.playSound(user.getEyeLocation(), Sound.ENTITY_CREEPER_HURT, 1f, 0f);
        } else if (!shot) {
            Effects.damage(user.getEntity(), damage, this, true);
            return false;
        } else {
            if ((range -= speed * timeDelta) <= 0) {
                return false; 
            }

            double zag = 0.2 + Math.random() * 0.3;
            Vector ortho = Vectors.orthogonal(loc.getDirection(), AngleType.RADIANS, Math.random() * Math.PI * 2).get().multiply(zag);
            Vector out = loc.getDirection().multiply(speed * timeDelta).add(ortho).multiply(0.1);
            Vector in = loc.getDirection().multiply(speed * timeDelta).subtract(ortho).multiply(0.1);

            if (this.ticksLived() % 3 == 0) {
                Effects.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 2.0f);
            }

            double size = 0.1 * range / maxRange;
            double len = speed * timeDelta / 2 / Math.cos(loc.getDirection().angle(out));
            for (double d = 0; d < len; d += 0.1) {
                loc.add(out);
                Particles.lightning(loc, 5, size, size, size);
                if (ray(timeDelta)) {
                    Effects.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 0.4f);
                    return false;
                }
            }

            if (ThreadLocalRandom.current().nextInt(100) < subarcChance * 100) {
                BoltInstance subArc = new BoltInstance((BoltAbility) provider, user, true);
                subArc.shot = true;
                subArc.loc = loc.clone().setDirection(out.add(ortho.multiply(0.2)));
                AbilityManager.start(subArc);    
            }

            for (double d = 0; d <= len; d += 0.1) {
                loc.add(in);
                Particles.lightning(loc, 5, size, size, size);
                if (ray(timeDelta)) {
                    Effects.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 0.4f);
                    return false;
                }
            }

            if (ThreadLocalRandom.current().nextInt(100) < subarcChance * 100) {
                BoltInstance subArc = new BoltInstance((BoltAbility) provider, user, true);
                subArc.shot = true;
                subArc.loc = loc.clone().setDirection(in.add(ortho.multiply(0.2)));
                AbilityManager.start(subArc);    
            }
        }

        return true;
    }

    @Override
    protected void postUpdate() {
        if (!shot) {
            return;
        }
    }

    @Override
    protected void onStop() {}

    @Override
    public String getName() {
        return provider.getName();
    }

    private boolean ray(double timeDelta) {
        RayTraceResult ray = loc.getWorld().rayTrace(loc, loc.getDirection(), speed / 2 * timeDelta, FluidCollisionMode.ALWAYS, true, 0.5, null);
        if (ray != null) {
            if (ray.getHitEntity() != null && ray.getHitEntity() instanceof LivingEntity  && !ray.getHitEntity().getUniqueId().equals(user.getUniqueID())) {
                Particles.spawn(Particle.EXPLOSION_LARGE, loc);
                Particles.lightning(loc, 30, 0.9, 0.9, 0.9);
                Effects.damage((LivingEntity) ray.getHitEntity(), damage, this, true);
                Velocity.apply(ray.getHitEntity(), loc.getDirection().setY(0), this);
                return true;
            } else if (ray.getHitBlock() != null) {
                Particles.spawn(Particle.EXPLOSION_LARGE, loc);
                Particles.lightning(loc, 30, 0.9, 0.9, 0.9);
                return true;
            }
        }

        return false;
    }
    
    private void shoot() {
        if (shot) return;
        this.shot = true;
        this.loc = user.getEyeLocation();
        this.user.addCooldown(provider, cooldown);
        Effects.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1.5f);
    }

    public void releaseSneak() {
        if (!shot && this.timeLived() >= minChargeTime) {
            this.shoot();
        } else {
            AbilityManager.remove(this);
        }
    }

    public void charge() {
        if (!shot) {
            this.minChargeTime = 0;
        }
    }

    public boolean canRedirect() {
        return this.timeLived() < minChargeTime;
    }
}
