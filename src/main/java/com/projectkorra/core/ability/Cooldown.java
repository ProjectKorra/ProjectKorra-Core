package com.projectkorra.core.ability;

public final class Cooldown {
    
    private String tag;
    private long start, duration = 0;

    Cooldown(String tag) {
        this.tag = tag;
        this.start = System.currentTimeMillis();
    }

    public String getTag() {
        return tag;
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
