package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceReinforcementBlockItem extends BlockItem {
    public BlastFurnaceReinforcementBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Level level = context.getLevel();
        Direction face = context.getClickedFace();

        BlockState originalState = super.getPlacementState(context);
        if(originalState == null||originalState.isAir())
            return super.getPlacementState(context);

        BlockState state = originalState.is(TFMGBlocks.BLAST_FURNACE_REINFORCEMENT.get()) ?  TFMGBlocks.BLAST_FURNACE_REINFORCEMENT_WALL.getDefaultState() : TFMGBlocks.RUSTED_BLAST_FURNACE_REINFORCEMENT_WALL.getDefaultState();



        if(face.getAxis().isHorizontal())
            if(level.getBlockState(context.getClickedPos().relative(face.getOpposite())).is(TFMGBlocks.FIREPROOF_BRICKS.get())){
                return state.setValue(TFMGHorizontalDirectionalBlock.FACING, face);
            }

        return super.getPlacementState(context);
    }
}
