package com.projectkorra.core.game.physique.landing;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;

import org.bukkit.event.entity.EntityDamageEvent;

public class LandingInstance extends AbilityInstance {

    @Attribute("damage_reduction")
    private double reduction;

    public LandingInstance(Ability provider, AbilityUser user, double reduction) {
        super(provider, user);
        this.reduction = reduction;
    }

    @Override
    protected void onStart() {}

    @Override
    protected boolean onUpdate(double timeDelta) {
        return true;
    }

    @Override
    protected void postUpdate() {}

    @Override
    protected void onStop() {}

    @Override
    public String getName() {
        return provider.getName();
    }
    
    public void reduceDamage(EntityDamageEvent event) {
        if (event.getDamage() - reduction <= 0) {
            event.setCancelled(true);
        } else {
            event.setDamage(event.getDamage() - reduction);
        }
    }
}
