package com.projectkorra.core.game.firebending.flamingwall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;
import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.game.firebending.FireAbilityInstance;
import com.projectkorra.core.physics.Collider;
import com.projectkorra.core.util.Blocks;
import com.projectkorra.core.util.Effects;
import com.projectkorra.core.util.Vectors;
import com.projectkorra.core.util.Velocity;

public class FlamingWallInstance extends FireAbilityInstance implements Collidable {

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
    @Attribute(Attribute.DURATION)
    private double duration;
    @Attribute("raise_speed")
    private double raiseSpeed;
    @Attribute("stamina_cost")
    private double staminaCost;

    private Location loc, shoveStart = null;
    private Vector dir;
    private boolean shoved = false, targeted = false;
    private double currHeight = 0;
    private ArrayList<Location> locs = new ArrayList<>();
    private Set<Entity> entities = new HashSet<>();
    private Collider collider;

    public FlamingWallInstance(FlamingWall provider, AbilityUser user) {
        super(provider, user);
        this.damage = provider.damage;
        this.raiseRadius = provider.raiseRadius;
        this.width = provider.width;
        this.height = provider.height;
        this.shoveRange = provider.shoveRange;
        this.shoveSpeed = provider.shoveSpeed;
        this.shoveKnockback = provider.shoveKnockback;
        this.cooldown = provider.cooldown;
        this.duration = provider.duration;
        this.raiseSpeed = provider.raiseSpeed;
        this.staminaCost = provider.staminaDrain;
    }

    @Override
    protected void onStart() {
        this.locate();
        this.collider = new Collider(loc);
        this.user.getStamina().pauseRegen(this);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        if (!user.getStamina().consume(timeDelta * staminaCost)) {
            return false;
        }

        collider.clear();

        targeted = user.getDirection().angle(Vectors.direction(user.getLocation(), this.loc)) < (Math.PI / 4);
        if (shoved && targeted) {
            if (shoveStart.distanceSquared(loc) >= shoveRange * shoveRange) {
                return false;
            }

            this.loc.add(this.dir.clone().multiply(shoveSpeed * timeDelta));
            this.collider.shift(loc);
        }

        currHeight = Math.min(currHeight + timeDelta * raiseSpeed, height);

        if (this.timeLived() > duration) {
            return false;
        }
        
        Vector hori = Vectors.orthogonal(this.dir).get();
        for (double d = -width/2; d <= width/2; d += 1) {
            Location bot = loc.clone().add(hori.clone().multiply(d));
            Block top = Blocks.getTop(bot, height);
            if (top.isEmpty() || !top.getRelative(BlockFace.UP).isEmpty()) {
                continue;
            }

            bot.setY(top.getY() + 1);
            for (double k = 0; k <= currHeight; k += 1) {
                Location part = bot.clone().add(0, k, 0);
                if (!part.getBlock().isPassable() || part.getBlock().isLiquid()) {
                    break;
                }

                for (int i = 0; i < 2; ++i) {
                    part.getWorld().spawnParticle(getParticle(), part.getX() + (Math.random() - 0.5), part.getY() + (Math.random() - 0.5), part.getZ() + (Math.random() - 0.5), 0, 0.1 * (Math.random() - 0.5), 0.1, 0.1 * (Math.random() - 0.5));
                }

                Effects.forNearbyEntities(part, 0.5, (e) -> e instanceof LivingEntity && !e.getUniqueId().equals(user.getUniqueID()), this::affect);
                locs.add(part);
                double yaw = Math.toRadians(Vectors.getYaw(dir));
                double x = Math.cos(yaw) * 0.3 + 0.5;
                double z = Math.sin(yaw) * 0.3 + 0.5;
                collider.add(BoundingBox.of(part, x, 1, z));
            }
        }

        if (locs.isEmpty()) {
            return false;
        }

        locs.clear();
        entities.clear();

        return true;
    }

    private void affect(Entity entity) {
        Effects.damage((LivingEntity) entity, damage, this, false);
        if (shoved && targeted && !entities.contains(entity) && !Velocity.isAffected(entity)) {
            Velocity.apply(entity, this.dir.clone().setY(-0.1).multiply(shoveKnockback), this);
        }
        entities.add(entity);
    }

    private void locate() {
        this.loc = user.getLocation();
        this.dir = Vectors.direction(loc.getYaw(), 0);
        this.loc.add(this.dir.clone().multiply(raiseRadius));
    }

    @Override
    protected void postUpdate() {}

    @Override
    protected void onStop() {
        user.addCooldown(this.getProvider(), cooldown);
    }

    @Override
    public String getName() {
        return provider.getName();
    }
    
    public void shove() {
        if (shoved) {
            return;
        }

        if (shoveStart == null) {
            shoveStart = this.loc.clone();
        }
        shoved = true;
    }

    public void unshove() {
        if (!shoved) {
            return;
        }

        shoved = false;
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
        return loc.getWorld();
    }

    @Override
    public void remove() {}
}
