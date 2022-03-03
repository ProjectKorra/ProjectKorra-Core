package com.projectkorra.core.game.physique.wellbeing;

import com.projectkorra.core.ability.Ability;
import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.ability.attribute.Attribute;

import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;
import org.bukkit.attribute.AttributeInstance;

public class WellbeingInstance extends AbilityInstance {

    @Attribute("Health")
    private double health;

    private double original;

    public WellbeingInstance(Ability provider, AbilityUser user, double health) {
        super(provider, user);
        this.health = health;
    }

    @Override
    protected void onStart() {
        AttributeInstance attr = user.getEntity().getAttribute(GENERIC_MAX_HEALTH);
        original = attr.getBaseValue();
        attr.setBaseValue(health);
        user.getEntity().setHealth(health);
    }

    @Override
    protected boolean onUpdate(double timeDelta) {
        return true;
    }

    @Override
    protected void postUpdate() {}

    @Override
    protected void onStop() {
        user.getEntity().getAttribute(GENERIC_MAX_HEALTH).setBaseValue(original);
    }

    @Override
    public String getName() {
        return provider.getName();
    }
    
    
}
