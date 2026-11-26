package com.drmangotea.tfmg.base;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

public class MaterialSet {

    public BlockEntry<?> block;
    public BlockEntry<?> slab;
    public BlockEntry<?> stairs;
    public BlockEntry<?> wall;

    public MaterialSet() {}

    public BlockEntry<?> get() {
        return block;
    }

    public BlockEntry<?> getSlab() {
        return slab;
    }

    public BlockEntry<?> getStairs() {
        return stairs;
    }

    public BlockEntry<?> getWall() {
        return wall;
    }

    public boolean contains(Block block) {
        return get().is(block) || getSlab().is(block) || getStairs().is(block) || getWall().is(block);
    }
}
