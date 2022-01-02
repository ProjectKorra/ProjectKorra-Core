package com.projectkorra.core.system.ability;

import org.bukkit.Location;

public abstract class SourceInstance {
    
    protected final AbilityUser user;

    public SourceInstance(AbilityUser user) {
        this.user = user;
    }

    /**
     * Attempts to use the source. If usable, returns the location to source from, otherwise should return null.
     * @return sourced location if usable
     */
    public abstract Location use();

    /**
     * Called to display something to the user to know that they have a source selected. Typically particles on
     * the source location or around the user
     */
    public abstract void display();

    public final AbilityUser getUser() {
        return user;
    }
}
