package com.projectkorra.core.collision.effect;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.data.Pairing;

public class CollisionSound extends CollisionEffect {

	public CollisionSound() {
		super("sound", Pairing.of(STRING, Sound.ENTITY_GENERIC_EXPLODE.name()), Pairing.of(FLOAT, 0.8f), Pairing.of(FLOAT, 0.2f));
	}

	@Override
	protected void accept(BendingCollisionEvent event, Object... args) {
		Sound sound;
		
		try {
			sound = Sound.valueOf((String)args[0]);
		} catch (Exception e) {
			sound = Sound.ENTITY_GENERIC_EXPLODE;
		}
		
		Location loc = event.getCenter();
		loc.getWorld().playSound(loc, sound, (float)args[1], (float)args[2]);
	}

}
