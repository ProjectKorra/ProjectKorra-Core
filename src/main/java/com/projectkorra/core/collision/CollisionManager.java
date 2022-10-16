package com.projectkorra.core.collision;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import com.projectkorra.core.ProjectKorra;
import com.projectkorra.core.collision.CollisionTree.TreeQueryResult;
import com.projectkorra.core.collision.effect.CollisionEffect;
import com.projectkorra.core.event.BendingCollisionEvent;
import com.projectkorra.core.util.CollisionUtil;
import com.projectkorra.core.util.data.Pair;
import com.projectkorra.core.util.data.Pairing;

public final class CollisionManager {

	private CollisionManager() {
	}

	private static final ProjectKorra PLUGIN = JavaPlugin.getPlugin(ProjectKorra.class);

	// these numbers are for the max world size, check
	// https://minecraft.gamepedia.com/Server.properties for more info
	private static final int BOUNDING_MIN = -29999984;
	private static final int BOUNDING_MAX = 29999984;
	private static final CollisionFile COLLISIONS = new CollisionFile(new File(PLUGIN.getDataFolder(), "collisions.txt"));

	private static CollisionTree tree = new CollisionTree(new BoundingBox(BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MIN, BOUNDING_MAX, BOUNDING_MAX, BOUNDING_MAX), 5);
	private static Set<Collidable> instances = new HashSet<>(), handled = new HashSet<>();
	private static Set<Pair<Collidable, Pair<BoundingBox, Location>>> collided = new HashSet<>();
	private static Map<Pair<String, String>, CollisionData> valids = new HashMap<>();
	private static boolean init = false;

	public static void init(ProjectKorra plugin) {
		if (init) {
			return;
		}

		init = true;
		COLLISIONS.readAnd((cd) -> valids.put(Pairing.of(cd.getLeft().toLowerCase(), cd.getRight().toLowerCase()), cd));
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, CollisionManager::tick, 1, 1);
	}

	public static void tick() {
		for (Collidable obj : instances) {
			handled.add(obj);

			for (TreeQueryResult result : tree.query(obj.getHitbox(), (c) -> !handled.contains(c))) {
				if (!obj.getWorld().equals(result.getCollidable().getWorld())) {
					continue;
				}

				Collidable other = result.getCollidable();
				if (validCollisionExists(obj, other)) {
					CollisionData data = valids.get(CollisionUtil.pairTags(obj, other));
					Collidable first, second;
					BoundingBox fb, sb;

					if (obj.getTag().equalsIgnoreCase(data.getLeft())) {
						first = obj;
						fb = result.getCollidingBounds().getLeft();
						second = other;
						sb = result.getCollidingBounds().getRight();
					} else {
						first = other;
						fb = result.getCollidingBounds().getRight();
						second = obj;
						sb = result.getCollidingBounds().getLeft();
					}

					BendingCollisionEvent event = new BendingCollisionEvent(first, second, data.getOperator());
					PLUGIN.getServer().getPluginManager().callEvent(event);

					if (event.isCancelled()) {
						continue;
					}

					for (int i = 0; i < data.getEffectAmount(); i++) {
						String[] args = data.getArgs(i);
						CollisionEffect.ofLabel(data.getEffect(i)).ifPresent((c) -> c.accept(event, args));
					}

					if (event.isFirstBeingRemoved()) {
						collided.add(Pairing.of(first, Pairing.of(fb, event.getCenter())));
					}

					if (event.isSecondBeingRemoved()) {
						collided.add(Pairing.of(second, Pairing.of(sb, event.getCenter())));
					}
				}
			}
		}

		for (Pair<Collidable, Pair<BoundingBox, Location>> collider : collided) {
			collider.getLeft().onCollide(collider.getRight().getLeft(), collider.getRight().getRight());
		}

		tree.reset();
		handled.clear();
		instances.clear();
		collided.clear();
	}

	// call this after the progress method for Collidable abilities
	public static void addCollidable(Collidable obj) {
		if (tree.insert(obj)) {
			instances.add(obj);
		}
	}

	public static void reload() {
		tree.reset();
		instances.clear();
	}

	/**
	 * Checks for a valid collision between two collidables
	 * 
	 * @param first  a collidable
	 * @param second another collidable
	 * @return true if valid collision found
	 */
	public static boolean validCollisionExists(Collidable first, Collidable second) {
		return first != null && second != null && valids.containsKey(CollisionUtil.pairTags(first, second));
	}

	public static boolean addValidCollision(Collidable first, Collidable second, CollisionOperator op) {
		return addValidCollision(first, second, op, null, null);
	}

	/**
	 * Adds a valid collision between two collidables but will not overwrite any
	 * existing valid collision between them. Also writes the new data into the
	 * collision file, which can then be configured by the owner.
	 * 
	 * @param first  lefthand collidable
	 * @param second righthand collidable
	 * @param op     interaction between the two collidables
	 * @param effect label of additional effect to happen
	 * @param args   effect parameters
	 * @return false if valid collision exists
	 */
	public static boolean addValidCollision(Collidable first, Collidable second, CollisionOperator op, String effect[], String[][] args) {
		if (first == null || second == null || validCollisionExists(first, second)) {
			return false;
		}

		CollisionData data = new CollisionData(first.getTag().toLowerCase(), second.getTag().toLowerCase(), op, effect, args);

		valids.put(CollisionUtil.pairTags(first, second), data);
		COLLISIONS.write(data);
		return true;
	}
	
	public static void removeValidCollision(Collidable first, Collidable second) {
		valids.remove(CollisionUtil.pairTags(first, second));
	}
}
