package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.cable_hubs;

import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.CableBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.packets.AddElectricalComponentPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableHubBlock extends Block implements IBE<CableHubBlockEntity> {
    public CableHubBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.getBlockEntity(pos) instanceof IRealisticElectric be){
            be.onPlace();
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pos), new AddElectricalComponentPacket(BlockPos.of(pos.asLong())));


        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
    }

    @Override
    public Class<CableHubBlockEntity> getBlockEntityClass() {
        return CableHubBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableHubBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DEBUG_CABLE_HUB.get();
    }
}
