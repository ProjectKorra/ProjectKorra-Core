package com.projectkorra.core.collision.effect;

import org.bukkit.Particle;

import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.Particles;
import com.projectkorra.core.util.data.Pairing;

public class CollisionParticles extends CollisionEffect {

	public CollisionParticles() {
		super("particles", Pairing.of(STRING, "barrier"), Pairing.of(INT, 1), Pairing.of(DOUBLE, 1.0));
	}

	@Override
	public void accept(BendingCollisionEvent event, Object... args) {
		Particle particle;
		try {
			particle = Particle.valueOf(((String) args[0]).toUpperCase());
		} catch (Exception e) {
			particle = Particle.BLOCK_MARKER;
		}
		
		Particles.spawn(particle, event.getCenter(), (int)args[1], (double)args[2], 0, null);
	}
}
