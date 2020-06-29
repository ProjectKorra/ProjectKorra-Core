package com.projectkorra.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class PathUtil {
	
	private PathUtil() {}

	private static BlockFace[] faces = { BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN };
	
	/**
	 * Attempts to construct a path from start to the goal using {@link SpatialUtil#PASSABLE_FILTER} and {@link #getDefaultHeuristic(Block)}
	 * @param start beginning of path
	 * @param goal ending of path
	 * @return null if no path found
	 */
	public static Iterator<Block> construct(Block start, Block goal) {
		return construct(start, goal, SpatialUtil.PASSABLE_FILTER, getDefaultHeuristic(goal));
	}
	
	/**
	 * Attempts to cons
	 * @param start beginning of path
	 * @param goal ending of path
	 * @param passable what blocks can exist in the path
	 * @return null if no path found
	 */
	public static Iterator<Block> construct(Block start, Block goal, Predicate<Block> passable) {
		return construct(start, goal, passable, getDefaultHeuristic(goal));
	}
	
	/**
	 * Attempts to construct a path from start to the goal according to the given Predicate and heuristic
	 * @param start beginning of the path
	 * @param goal ending of the path
	 * @param passable what blocks can exist in the path
	 * @param heuristic "cost" to reach the goal from a given block
	 * @return null if no path found
	 */
	public static Iterator<Block> construct(Block start, Block goal, Predicate<Block> passable, Function<Block, Double> heuristic) {
		PriorityQueue<Block> open = new PriorityQueue<>((block1, block2) -> MathUtil.distanceManhattan(block1, start) - MathUtil.distanceManhattan(block2, start));
		Map<Block, Block> origins = new HashMap<>();
		Map<Block, Double> gScore = new HashMap<>();
		Map<Block, Double> fScore = new HashMap<>();
		
		open.add(start);
		gScore.put(start, 0.0);
		fScore.put(start, heuristic.apply(start));
		
		Block current;
		
		while (!open.isEmpty()) {
			current = open.poll();
			
			if (current.equals(goal)) {
				LinkedList<Block> path = new LinkedList<>();
				path.add(current);
				
				while (origins.containsKey(current)) {
					current = origins.get(current);
					path.addFirst(current);
				}
				
				return path.iterator();
			}
			
			for (BlockFace face : faces) {
				Block neighbor = current.getRelative(face);
				
				if (!passable.test(neighbor)) {
					continue;
				}
				
				double tentative = gScore.get(current) + 1.0;
				
				if (!gScore.containsKey(neighbor) || gScore.get(neighbor) > tentative) {
					origins.put(neighbor, current);
					gScore.put(neighbor, tentative);
					fScore.put(neighbor, tentative + heuristic.apply(neighbor));
					
					if (!open.contains(neighbor)) {
						open.add(neighbor);
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get the default heuristic function, defining cost as the manhattan distance between the goal and a given block
	 * @param goal block working towards
	 * @return default heuristic function
	 */
	public static Function<Block, Double> getDefaultHeuristic(Block goal) {
		return (block) -> (double) MathUtil.distanceManhattan(block, goal);
	}
}
