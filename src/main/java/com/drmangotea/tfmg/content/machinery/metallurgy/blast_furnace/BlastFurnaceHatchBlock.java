package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceHatchBlock extends Block implements IBE<BlastFurnaceHatchBlockEntity> {

    public BlastFurnaceHatchBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Class<BlastFurnaceHatchBlockEntity> getBlockEntityClass() {
        return BlastFurnaceHatchBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BlastFurnaceHatchBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.BLAST_FURNACE_HATCH.get();
    }
}
