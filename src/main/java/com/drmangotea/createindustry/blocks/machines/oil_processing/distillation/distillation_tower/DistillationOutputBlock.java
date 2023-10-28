package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillation_tower;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class DistillationOutputBlock extends Block implements IBE<DistillationOutputBlockEntity> {

    public DistillationOutputBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()));
    }


    @Override
    public Class<DistillationOutputBlockEntity> getBlockEntityClass() {
        return DistillationOutputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistillationOutputBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STEEL_DISTILLATION_OUTPUT.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

}