package com.projectkorra.core.physics;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class PhysicsBody {

	private double mass;
	private Vector3 pos, vel = new Vector3(), acc = new Vector3();
	private boolean hasGravity = false;

	public PhysicsBody(double mass, double x, double y, double z, boolean hasGravity) {
		this.mass = mass;
		this.pos = new Vector3(x, y, z);
		this.hasGravity = hasGravity;
	}

	public PhysicsBody(double mass, Location position, boolean hasGravity) {
		this(mass, position.getX(), position.getY(), position.getZ(), hasGravity);
	}

	public void update(double timeDelta) {
		if (hasGravity) {
			this.acc.add(0, -31.36, 0);
		}

		this.vel.add(acc.getX() * timeDelta, acc.getY() * timeDelta, acc.getZ() * timeDelta);
		this.pos.add(vel.getX() * timeDelta, vel.getY() * timeDelta, vel.getZ() * timeDelta);
		this.acc.setX(0).setY(0).setZ(0);
	}

	public void applyForce(Vector force) {
		this.acc.add(force.getX() / mass, force.getY() / mass, force.getZ() / mass);
	}

	public Vector getPosition() {
		return pos.clone();
	}

	public Vector getVelocity() {
		return vel.clone();
	}

	public Vector getAcceleration() {
		return acc.clone();
	}

	public Location getLocation(World world) {
		return pos.toLocation(world);
	}

	public static class Vector3 extends Vector {

		public Vector3(double x, double y, double z) {
			super(x, y, z);
		}

		public Vector3() {
			super(0, 0, 0);
		}

		public void add(double x, double y, double z) {
			super.x += x;
			super.y += y;
			super.z += z;
		}
	}
}
