package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.experimental.packets.RealNetworkUpdatePacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DebugResistorBlock extends TFMGDirectionalBlock implements IBE<DebugResistorBlockEntity> {
    public DebugResistorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.getBlockEntity(pos) instanceof DebugResistorBlockEntity be){
            be.updateNetwork(pos);
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pos), new RealNetworkUpdatePacket(BlockPos.of(pos.asLong())));
            be.sendData();

        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
    }

    @Override
    public Class<DebugResistorBlockEntity> getBlockEntityClass() {
        return DebugResistorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DebugResistorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DEBUG_RESISTOR.get();
    }
}
