package com.projectkorra.core.temporary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Consumer;

import com.projectkorra.core.ProjectKorra;

public class TempBlock {

	private static final Map<Block, TempBlock> CACHE = new HashMap<>();
	private static boolean init = false;

	private Block block;
	private BlockState original;
	private LinkedList<TempData> stack = new LinkedList<>();

	private TempBlock(Block block) {
		this.block = block;
		this.original = block.getState();
	}

	public Block getBlock() {
		return block;
	}
	
	public BlockData getCurrentData() {
		return stack.peek().data;
	}
	
	public boolean currentlyHasPhysics() {
		return stack.peek().physics;
	}

	public TempData setData(BlockData data) {
		return this.setData(data, -1, false);
	}
	
	public TempData setData(BlockData data, long duration) {
		return this.setData(data, duration, false);
	}
	
	public TempData setData(BlockData data, boolean physics) {
		return this.setData(data, -1, physics);
	}

	public TempData setData(BlockData data, long duration, boolean physics) {
		this.block.setBlockData(data, false);
		TempData td = new TempData(data, duration, physics);
		this.stack.addFirst(td);
		return td;
	}

	public void revertData(TempData data) {
		if (data == null) {
			return;
		} else if (!stack.contains(data)) {
			return;
		}

		if (stack.peek() == data) {
			stack.poll();

			TempData td;
			while ((td = stack.peek()) != null) {
				if (!td.isDone()) {
					this.block.setBlockData(td.data, false);
					break;
				}

				stack.poll();
			}
		} else {
			stack.remove(data);
		}
		
		data.onRevert.accept(this);
		if (stack.isEmpty()) {
			this.revert();
		}
	}

	public void revert() {
		CACHE.remove(this.block);
		for (TempData td : this.stack) {
			td.onRevert.accept(this);
		}
		this.stack.clear();
		this.block.setBlockData(original.getBlockData());
	}
	
	public void destroy() {
		CACHE.remove(this.block);
		for (TempData td : this.stack) {
			td.onDestroy.accept(this);
		}
		this.stack.clear();
	}

	private void progressDurations() {
		if (stack.isEmpty()) {
			return;
		}

		TempData td = stack.peek();

		if (td.isDone()) {
			revertData(td);
		}
	}

	public static boolean exists(Block block) {
		return CACHE.containsKey(block);
	}

	public static TempBlock from(Block block) {
		Validate.notNull(block);
		return CACHE.computeIfAbsent(block, TempBlock::new);
	}

	public static void init(ProjectKorra plugin) {
		if (init) {
			return;
		}

		init = true;
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, TempBlock::tick, 1, 1);
	}

	public static void clear() {
		for (TempBlock tb : new HashSet<>(CACHE.values())) {
			tb.revert();
		}
	}

	private static void tick() {
		for (TempBlock tb : new HashSet<>(CACHE.values())) {
			tb.progressDurations();
		}
	}
	
	public static class TempData {
		private BlockData data;
		private long created = System.currentTimeMillis(), duration;
		private boolean physics;
		private Consumer<TempBlock> onDestroy = (b) -> {}, onRevert = (b) -> {};

		private TempData(BlockData data, long duration, boolean physics) {
			this.data = data;
			this.duration = duration;
			this.physics = physics;
		}

		public long lifetime() {
			return System.currentTimeMillis() - created;
		}

		public boolean isDone() {
			return duration > 0 && lifetime() >= duration;
		}
		
		public boolean hasPhysics() {
			return physics;
		}
		
		public TempData setDestroyEffect(Consumer<TempBlock> onDestroy) {
			if (onDestroy == null) {
				onDestroy = (b) -> {};
			}
			
			this.onDestroy = onDestroy;
			return this;
		}
		
		public TempData setRevertEffect(Consumer<TempBlock> onRevert) {
			if (onRevert == null) {
				onRevert = (b) -> {};
			}
			
			this.onRevert = onRevert;
			return this;
		}
	}
}
