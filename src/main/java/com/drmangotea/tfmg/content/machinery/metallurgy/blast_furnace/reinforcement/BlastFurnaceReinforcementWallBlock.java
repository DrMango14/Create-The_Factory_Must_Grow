package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlastFurnaceReinforcementWallBlock extends TFMGHorizontalDirectionalBlock {
    public BlastFurnaceReinforcementWallBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return TFMGShapes.BLAST_FURNACE_REINFORCEMENT_WALL.get(p_60555_.getValue(FACING));
    }
}
