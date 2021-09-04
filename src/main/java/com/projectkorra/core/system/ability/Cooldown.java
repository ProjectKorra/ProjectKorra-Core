package com.projectkorra.core.system.ability;

public final class Cooldown {
    
    private Ability ability;
    private long start, duration = 0;

    Cooldown(Ability ability) {
        this.ability = ability;
        this.start = System.currentTimeMillis();
    }

    public Ability getAbility() {
        return ability;
    }

    public long getStartTime() {
        return start;
    }

    public long getEndTime() {
        return start + duration;
    }

    public long getDuration() {
        return duration;
    }

    public void addDuration(long duration) {
        this.duration += duration;
    }

    public long getRemaining() {
        return getEndTime() - System.currentTimeMillis();
    }
}
