package com.projectkorra.core.collision;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.collision.effect.CollisionEffect;
import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.CollisionUtil;
import com.projectkorra.core.util.data.MutablePair;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.Pairing;

public class CollisionManager {
	
	private static final ProjectKorra PLUGIN = JavaPlugin.getPlugin(ProjectKorra.class);
	
	// these numbers are for the max world size, check https://minecraft.gamepedia.com/Server.properties for more info
	private static final int BOUNDING_MIN = -29999984;
	private static final int BOUNDING_MAX = 29999984;
	private static final CollisionFile COLLISIONS = new CollisionFile(new File(PLUGIN.getDataFolder(), "collisions.txt"));

	private CollisionTree tree;
	private Set<Collidable> instances, removal;
	private Set<Pair<Collidable, Collidable>> collided;
	private Map<Pair<String, String>, CollisionData> valids;
	private MutablePair<Collidable, Collidable> colliding = Pairing.ofMutable(null, null);
	
	public CollisionManager() {
		tree = new CollisionTree(new BoundingBox(BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MAX, BOUNDING_MAX, BOUNDING_MAX), 5); //create a tree with the max world size
		instances = new HashSet<>();
		collided = new HashSet<>();
		valids = new HashMap<>();
		
		COLLISIONS.readAnd((cd) -> valids.put(Pairing.of(cd.getLeft().toLowerCase(), cd.getSecond().toLowerCase()), cd));
	}
	
	public void tick() {
		for (Collidable obj : instances) {
			Set<Collidable> found = tree.query(obj.getHitbox());
			found.remove(obj);
			colliding.setLeft(obj);
			
			for (Collidable other : found) {
				colliding.setRight(other);
				if (collided.contains(colliding)) {
					continue;
				}
				
				if (doesValidCollisionExist(obj, other)) {
					CollisionData data = valids.get(CollisionUtil.pairTags(obj, other));
					Collidable first, second;
					
					if (obj.getTag().equalsIgnoreCase(data.getLeft())) {
						first = obj;
						second = other;
					} else {
						first = other;
						second = obj;
					}
					
					BendingCollisionEvent event = new BendingCollisionEvent(first, second, data.getOperator());
					
					for (int i = 0; i < data.getEffectAmount(); i++) {
						String[] args = data.getArgs(i);
						CollisionEffect.ofLabel(data.getEffect(i)).ifPresent((c) -> c.accept(event, args));	
					}
					PLUGIN.getServer().getPluginManager().callEvent(event);
					
					if (event.isCancelled()) {
						continue;
					}
					
					if (event.isFirstBeingRemoved()) {
						removal.add(first);
					}
					
					if (event.isSecondBeingRemoved()) {
						removal.add(second);
					}
					
					collided.add(colliding);
				}
			}
		}
		
		//do removal things with removal set
		
		colliding.set(null, null);
		tree.reset();
		instances.clear();
		collided.clear();
		removal.clear();
	}

	//call this after the progress method for Collidable abilities
	public void addCollidable(Collidable obj) {
		if (tree.insert(obj)) {
			instances.add(obj);
		}
	}
	
	public void reload() {
		tree.reset();
		instances.clear();
	}
	
	/**
	 * Checks for a valid collision between two collidables
	 * @param first a collidable
	 * @param second another collidable
	 * @return true if valid collision found
	 */
	public boolean doesValidCollisionExist(Collidable first, Collidable second) {
		return first != null && second != null && valids.containsKey(CollisionUtil.pairTags(first, second));
	}
	
	public boolean addValidCollision(Collidable first, Collidable second, CollisionOperator op) {
		return addValidCollision(first, second, op, null, null);
	}
	
	/**
	 * Adds a valid collision between two collidables but will not overwrite
	 * any existing valid collision between them. Also writes the new data
	 * into the collision file, which can then be configured by the owner.
	 * @param first lefthand collidable 
	 * @param second righthand collidable
	 * @param op interaction between the two collidables
	 * @param effect label of additional effect to happen
	 * @param args effect parameters
	 * @return false if valid collision exists
	 */
	public boolean addValidCollision(Collidable first, Collidable second, CollisionOperator op, String effect[], String[][] args) {
		if (first == null || second == null || doesValidCollisionExist(first, second)) {
			return false;
		}
		
		CollisionData data = new CollisionData(first.getTag().toLowerCase(), second.getTag().toLowerCase(), op, effect, args);
		
		valids.put(CollisionUtil.pairTags(first, second), data);
		COLLISIONS.write(data);
		return true;
	}
}
