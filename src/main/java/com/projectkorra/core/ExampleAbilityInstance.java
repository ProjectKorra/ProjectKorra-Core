package com.projectkorra.core;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.User;

public class ExampleAbilityInstance extends Ability {
    private Location loc;

    public ExampleAbilityInstance(User user, int priority) {
        super(user, priority);

        loc = user.getEntity().getLocation();
    }

    @Override
    public void onStart() {

        System.out.println("I AM START WOOHOOO " + this);
    }

    @Override
    public void progress() {
        Entity e = user.getEntity();
        Vector dir = e.getLocation().getDirection().clone();

        if (loc.add(dir).getBlock().isPassable()) {
            e.getWorld().spawnParticle(Particle.CLOUD, loc.add(dir), 5);
        } else {
            this.remove();
        }

        if (System.currentTimeMillis() - this.getStartTime() > 5000) {
            this.remove();
        }
    }

    @Override
    public void onRemove() {

        System.out.println("I AM STOP SAD DAY " + this);

    }

}
