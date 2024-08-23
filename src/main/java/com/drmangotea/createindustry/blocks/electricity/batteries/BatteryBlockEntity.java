package com.drmangotea.createindustry.blocks.electricity.batteries;


import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BatteryBlockEntity extends ElectricBlockEntity {


    public float powerOutput = 0;

    public int capacity = 0;

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    public void setCapacity(ItemStack stack){
        //capacity = stack.getOrCreateTag().getInt("Capacity");
        energy.receiveEnergy(stack.getOrCreateTag().getInt("Capacity"),false);
    }
    public void tick(){
        super.tick();


        if(capacity>0) {
            powerOutput = transferSpeed();

        }else powerOutput = 0;

    }
    @Override
    public int transferSpeed() {
        return 100;
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {




        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.putInt("Capacity", capacity);

        super.write(compound, clientPacket);

    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);


        capacity = compound.getInt("Capacity");

    }
    @Override
    public boolean hasElectricitySlot(Direction direction) {

        if(direction == Direction.UP)
            return true;

        return false;
    }

    @Override
    public int FECapacity() {
        return 30000;
    }




    @Override
    public int voltageGeneration() {
        return energy.getEnergyStored() <= 10 ? 0 : 200;
    }
}
