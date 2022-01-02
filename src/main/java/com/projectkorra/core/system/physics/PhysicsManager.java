package com.projectkorra.core.system.physics;

import java.util.HashSet;
import java.util.Set;

import com.projectkorra.core.util.Threads;

public final class PhysicsManager {

    private PhysicsManager() {}

    private static class Timer {

        private long prevTime = System.currentTimeMillis();

        private long delta() {
            long delta = System.currentTimeMillis() - prevTime;
            prevTime = System.currentTimeMillis();
            return delta;
        }

        private double deltaSeconds() {
            return delta() / 1000D;
        }
    }
    
    private static final Timer TIMER;
    private static final Set<PhysicsBody> BODIES = new HashSet<>();

    static {
        TIMER = new Timer();
        Threads.runRepeatingTask(PhysicsManager::run);
    }

    public static PhysicsBody createBody(double mass, double x, double y, double z) {
        PhysicsBody body = new PhysicsBody(mass, x, y, z);
        BODIES.add(body);
        return body;
    }

    private static void run() {
        BODIES.forEach((pb) -> pb.update(TIMER.deltaSeconds()));
    }
}
