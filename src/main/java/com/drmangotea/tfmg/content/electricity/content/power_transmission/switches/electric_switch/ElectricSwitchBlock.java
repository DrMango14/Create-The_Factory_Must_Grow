package com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.electric_switch;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.content.devices.base.DirectionalResistiveLoadBlock;
import com.drmangotea.tfmg.content.electricity.packets.AddElectricalComponentPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricSwitchBlock extends DirectionalResistiveLoadBlock implements IBE<ElectricSwitchBlockEntity> {
    public ElectricSwitchBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.getBlockEntity(pos) instanceof IElectric be){
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
    public Class<ElectricSwitchBlockEntity> getBlockEntityClass() {
        return ElectricSwitchBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElectricSwitchBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ELECTRIC_SWITCH.get();
    }
}
