package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.controller;

import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.blocks.tanks.SteelTankBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class DistillationControllerBlock extends TFMGHorizontalDirectionalBlock implements IBE<DistillationControllerBlockEntity>, IWrenchable {
    public DistillationControllerBlock(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        SteelTankBlock.updateTowerState(pLevel, pPos.relative(getFacing(pState).getOpposite()),true,false);

    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity()))
            world.removeBlockEntity(pos);
        SteelTankBlock.updateTowerState(world, pos.relative(getFacing(state).getOpposite()),false,false);

    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if(!SteelTankBlock.updateTowerState((Level) pLevel, pPos.relative(getFacing(pState).getOpposite()),true,true))
            return false;


        return super.canSurvive(pState, pLevel, pPos);
    }



    public static Direction getFacing(BlockState state){
        return state.getValue(FACING);
    }

    @Override
    public Class<DistillationControllerBlockEntity> getBlockEntityClass() {
        return DistillationControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistillationControllerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STEEL_DISTILLATION_CONTROLLER.get();
    }
}
