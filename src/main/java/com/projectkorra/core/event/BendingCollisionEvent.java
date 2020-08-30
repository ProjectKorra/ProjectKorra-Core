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
	
	public Location getCenterOfMass() {
		return first.getLocation().clone().add(second.getLocation()).multiply(0.5);
	}
}
