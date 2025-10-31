package com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DistillationControllerBlock extends TFMGHorizontalDirectionalBlock implements IBE<DistillationControllerBlockEntity>, IWrenchable {
    public DistillationControllerBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState,level,pPos,pOldState,pIsMoving);
        SteelTankBlock.updateTowerState(level, pPos.relative(getFacing(pState).getOpposite()),true,false);
        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pPos),new DistillationTowerPacket(pPos,pPos.relative(getFacing(pState).getOpposite()),true));

    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean pIsMoving) {

        super.onRemove(state,world,pos,newState,pIsMoving);
        IBE.onRemove(state,world,pos,newState);


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
        return TFMGBlockEntities.DISTILLATION_CONTROLLER.get();
    }
}
