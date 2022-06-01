package com.projectkorra.core.event;

import org.bukkit.Location;

import com.projectkorra.core.collision.Collidable;
import com.projectkorra.core.collision.CollisionOperator;

public class BendingCollisionEvent extends CancellableBendingEvent {

	private Collidable first, second;
	private CollisionOperator op;

	public BendingCollisionEvent(Collidable first, Collidable second, CollisionOperator op) {
		this.first = first;
		this.second = second;
		this.op = op;
	}

	public Collidable getFirst() {
		return first;
	}

	public Collidable getSecond() {
		return second;
	}

	public boolean isFirstBeingRemoved() {
		return op.equals(CollisionOperator.FIRST_CANCELED) || op.equals(CollisionOperator.BOTH_CANCELED);
	}

	public boolean isSecondBeingRemoved() {
		return op.equals(CollisionOperator.SECOND_CANCELED) || op.equals(CollisionOperator.BOTH_CANCELED);
	}

	/**
	 * Set whether to remove the first collidable or not
	 * 
	 * @param remove true to remove first collidable
	 */
	public void setFirstToRemove(boolean remove) {
		if (remove) {
			if (op.equals(CollisionOperator.SECOND_CANCELED)) {
				op = CollisionOperator.BOTH_CANCELED;
			} else if (op.equals(CollisionOperator.NEITHER_CANCELED)) {
				op = CollisionOperator.FIRST_CANCELED;
			}
		} else {
			if (op.equals(CollisionOperator.BOTH_CANCELED)) {
				op = CollisionOperator.SECOND_CANCELED;
			} else if (op.equals(CollisionOperator.FIRST_CANCELED)) {
				op = CollisionOperator.NEITHER_CANCELED;
			}
		}
	}

	/**
	 * Set whether to remove the second collidable or not
	 * 
	 * @param remove true to remove second collidable
	 */
	public void setSecondToRemove(boolean remove) {
		if (remove) {
			if (op.equals(CollisionOperator.FIRST_CANCELED)) {
				op = CollisionOperator.BOTH_CANCELED;
			} else if (op.equals(CollisionOperator.NEITHER_CANCELED)) {
				op = CollisionOperator.SECOND_CANCELED;
			}
		} else {
			if (op.equals(CollisionOperator.BOTH_CANCELED)) {
				op = CollisionOperator.FIRST_CANCELED;
			} else if (op.equals(CollisionOperator.SECOND_CANCELED)) {
				op = CollisionOperator.NEITHER_CANCELED;
			}
		}
	}

	/**
	 * Calculates the approximate center of the collision using the positions of the
	 * collidables and their volumes to find a weighted average location
	 * 
	 * @return approximate center location of the collision
	 */
	public Location getCenter() {
		double denom = first.getHitbox().getVolume() + second.getHitbox().getVolume();
		double xnum = first.getHitbox().getVolume() * first.getHitbox().getCenterX() + second.getHitbox().getVolume() * second.getHitbox().getCenterX();
		double ynum = first.getHitbox().getVolume() * first.getHitbox().getCenterY() + second.getHitbox().getVolume() * second.getHitbox().getCenterY();
		double znum = first.getHitbox().getVolume() * first.getHitbox().getCenterZ() + second.getHitbox().getVolume() * second.getHitbox().getCenterZ();
		return new Location(first.getWorld(), xnum / denom, ynum / denom, znum / denom);
	}
}
