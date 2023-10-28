package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillery;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class DistilleryControllerBlock extends Block implements IBE<DistilleryControllerBlockEntity>, IWrenchable {


    public DistilleryControllerBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }


    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }


    @Override
    public Class<DistilleryControllerBlockEntity> getBlockEntityClass() {
        return DistilleryControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistilleryControllerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CAST_IRON_DISTILLATION_CONTROLLER.get();
    }


    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

}
