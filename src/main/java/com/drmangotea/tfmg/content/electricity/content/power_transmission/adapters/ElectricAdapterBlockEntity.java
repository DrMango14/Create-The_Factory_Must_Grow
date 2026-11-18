package com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ElectricAdapterBlockEntity extends SmartBlockEntity implements IElectric {

    ElectricalProperties p;

    public ElectricAdapterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if(state.getBlock() instanceof ElectricAdapterBlock block){
            p = block.slotCount == 2 ? new TwoSlotAdapterProperties(pos,state.getValue(TFMGDirectionalBlock.FACING)) : new FourSlotAdapterProperties(pos,state.getValue(TFMGDirectionalBlock.FACING));
        }

    }



    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public Level getWorld() {
        return getLevel();
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }
}
