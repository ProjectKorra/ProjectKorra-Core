package com.projectkorra.core.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class Blocks {
    
    private Blocks() {}

    public static Block getTop(Location loc, double range) {
        Block curr = loc.getBlock();
        BlockFace v = !curr.isEmpty() ? BlockFace.UP : BlockFace.DOWN;
        int i = 0;
        while (i <= range) {
            if (v == BlockFace.UP && curr.getRelative(BlockFace.UP).isEmpty()) {
                break;
            }

            curr = curr.getRelative(v);
            ++i;

            if (v == BlockFace.DOWN && !curr.isEmpty()) {
                break;
            }
        }

        return curr;
    }
}
