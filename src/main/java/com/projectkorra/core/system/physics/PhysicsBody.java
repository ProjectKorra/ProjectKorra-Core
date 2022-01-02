package com.projectkorra.core.system.physics;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class PhysicsBody {
    
    private double mass;
    private Vector3 pos, vel = new Vector3(), acc = new Vector3();

    PhysicsBody(double mass, double x, double y, double z) {
        this.mass = mass;
        this.pos = new Vector3(x, y, z);
    }

    protected void update(double timeDelta) {
        this.vel.add(acc.getX() * timeDelta, acc.getY() * timeDelta, acc.getZ() * timeDelta);
        this.pos.add(vel.getX() * timeDelta, vel.getY() * timeDelta, vel.getZ() * timeDelta);
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
