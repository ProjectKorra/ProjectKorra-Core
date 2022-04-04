package com.projectkorra.core.util.data;

import java.util.Optional;

import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.event.stamina.StaminaChangeEvent;
import com.projectkorra.core.util.Events;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public final class Stamina {
    
    private AbilityUser user;
    private double current, max, regen;
    private Optional<BossBar> bar;

    public Stamina(AbilityUser user, int max, int regen) {
        this.user = user;
        this.current = this.max = max;
        this.regen = regen;
        this.bar = Optional.of(Bukkit.createBossBar("Bending Stamina", BarColor.GREEN, BarStyle.SOLID));

        if (user.getEntity() instanceof Player) {
            this.updateBar(bar.get());
        }
    }

    public AbilityUser user() {
        return user;
    }

    public boolean consume(double amount) {
        StaminaChangeEvent event = Events.call(new StaminaChangeEvent(this, Math.abs(amount), true));
        if (event.isCancelled()) {
            return false;
        }

        double diff = this.current - Math.abs(event.getAmount());
        if (diff < 0) {
            return false;
        }

        this.current = diff;
        return true;
    }

    public void restore(double amount) {
        StaminaChangeEvent event = Events.call(new StaminaChangeEvent(this, Math.abs(amount), false));
        if (event.isCancelled()) {
            return;
        }

        this.current = Math.min(this.max, this.current + Math.abs(event.getAmount()));
    }

    public void regen(double deltaTime) {
        this.current = Math.min(this.max, this.current + deltaTime * regen);
        this.bar.ifPresent(this::updateBar);
    }

    public void modifyRegen(double change) {
        this.regen = Math.max(this.regen + change, 0);
    }

    private void updateBar(BossBar bar) {
        double progress = this.current / this.max;
        bar.setProgress(progress);
        if (progress >= 0.5) {
            bar.setColor(BarColor.GREEN);
        } else if (progress >= 0.1) {
            bar.setColor(BarColor.YELLOW);
        } else {
            bar.setColor(BarColor.RED);
        }
        
        bar.addPlayer((Player) user.getEntity());
    }
}
