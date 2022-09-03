package com.projectkorra.core;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.PlayerUser;
import com.projectkorra.core.ability.User;

public class FireAbilityInstance extends Ability {
    private Location loc;

    public FireAbilityInstance(User user, int priority) {
        super(user, priority);

        loc = user.getEntity().getLocation();
    }

    @Override
    public void onStart() {

        System.out.println("I AM START WOOHOOO" + this);
    }

    @Override
    public void progress() {
        Entity e = user.getEntity();
        Vector dir;

        if (user instanceof PlayerUser) {
            Player p = (Player) e;
            dir = p.getEyeLocation().getDirection().clone();
        } else {
            dir = e.getLocation().getDirection().clone();
        }

        e.getWorld().spawnParticle(Particle.CLOUD, loc.add(dir), 5);

        if (System.currentTimeMillis() - this.getStartTime() > 5000) {
            this.remove();
        }
    }

    @Override
    public void onRemove() {

        System.out.println("I AM STOP SAD DAY" + this);

    }

}
