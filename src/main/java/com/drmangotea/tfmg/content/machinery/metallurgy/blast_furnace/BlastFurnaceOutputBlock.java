package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceOutputBlock extends TFMGHorizontalDirectionalBlock implements IBE<BlastFurnaceOutputBlockEntity> {
    public BlastFurnaceOutputBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public Class<BlastFurnaceOutputBlockEntity> getBlockEntityClass() {
        return BlastFurnaceOutputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BlastFurnaceOutputBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.BLAST_FURNACE_OUTPUT.get();
    }
}
