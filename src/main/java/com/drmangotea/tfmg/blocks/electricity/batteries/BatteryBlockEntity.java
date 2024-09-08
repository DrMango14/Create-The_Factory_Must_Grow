package com.drmangotea.tfmg.blocks.electricity.batteries;



import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BatteryBlockEntity extends ElectricBlockEntity {




    public int capacity = 0;

    public BatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    public void setCapacity(ItemStack stack){

        energy.setEnergy(stack.getOrCreateTag().getInt("Capacity"));
    }



    @Override
    public boolean outputAllowed() {
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
        return energy.getEnergyStored() <= 10 ? 0 : 250;
    }
}
