package com.projectkorra.core.util.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.projectkorra.core.ability.AbilityInstance;
import com.projectkorra.core.ability.AbilityUser;
import com.projectkorra.core.event.stamina.StaminaChangeEvent;
import com.projectkorra.core.util.Events;

public final class Stamina {

	private AbilityUser user;
	private double current = 1.0, regen;
	private Optional<BossBar> bar;
	private Set<AbilityInstance> paused = new HashSet<>();
	private Set<AbilityInstance> onRemove = new HashSet<>();

	public Stamina(AbilityUser user, double regenPercent) {
		this.user = user;
		this.regen = regenPercent;
		this.bar = Optional.of(Bukkit.createBossBar("Bending Stamina", BarColor.GREEN, BarStyle.SOLID));

		if (user.getEntity() instanceof Player) {
			this.updateBar(bar.get());
		}
	}

	public AbilityUser user() {
		return user;
	}

	public boolean consume(double percent) {
		StaminaChangeEvent event = Events.call(new StaminaChangeEvent(this, Math.abs(percent), true));
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

	public void restore(double percent) {
		StaminaChangeEvent event = Events.call(new StaminaChangeEvent(this, Math.abs(percent), false));
		if (event.isCancelled()) {
			return;
		}

		this.current = Math.min(1.0, this.current + Math.abs(event.getAmount()));
	}

	public void regen(double deltaTime) {
		Iterator<AbilityInstance> iter = onRemove.iterator();
		while (iter.hasNext()) {
			AbilityInstance next = iter.next();
			if (next.ticksLived() < 0) {
				iter.remove();
				paused.remove(next);
			}
		}

		if (paused.isEmpty() && user.getEntity().getRemainingAir() >= user.getEntity().getMaximumAir()) {
			this.current = Math.min(1.0, this.current + deltaTime * regen);
		}

		this.bar.ifPresent(this::updateBar);
	}

	public void pauseRegen(AbilityInstance instance) {
		this.pauseRegen(instance, true);
	}

	public void pauseRegen(AbilityInstance instance, boolean removeUnpauses) {
		this.paused.add(instance);
		if (removeUnpauses) {
			this.onRemove.add(instance);
		}
	}

	public void unpauseRegen(AbilityInstance instance) {
		this.paused.remove(instance);
		this.onRemove.remove(instance);
	}

	public void modifyRegen(double change) {
		this.regen = Math.max(this.regen + change, 0);
	}

	private void updateBar(BossBar bar) {
		bar.setProgress(current);
		if (current >= 0.5) {
			bar.setColor(BarColor.GREEN);
		} else if (current >= 0.1) {
			bar.setColor(BarColor.YELLOW);
		} else {
			bar.setColor(BarColor.RED);
		}

		bar.addPlayer((Player) user.getEntity());
	}
}
