package com.drmangotea.createindustry.blocks.electricity.capacitor;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.List;

public class CapacitorBlockEntity extends ElectricBlockEntity {

    public int lastVoltage=0;

    public CapacitorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }


    @Override
    public void tick() {
        super.tick();

   // if(getVoltage()!=0&&energy.getEnergyStored()<10) {
   //     lastVoltage = getVoltage();
   //     voltage = 0;
   // }
//
   // if(energy.getEnergyStored()<10)
   //     lastVoltage = 0;
//
    }

    @Override
    public float maxVoltage() {
        return 1000;
    }

    @Override
     public void manageVoltage(){

         if(voltageGeneration()>0) {
             voltage = voltageGeneration();
             distanceFromSource = 0;
         }



         current = energy.getEnergyStored()/(voltage+1);

         if(voltage == 0)
             setDistanceFromSource(Integer.MAX_VALUE);



         if(voltage>maxVoltage()){
             explode();
             voltage = 0;
         }



     }

     @Override
     public int voltageGeneration() {



         //if(energy.getEnergyStored()<10) {
         //    lastVoltage = 0;
         //    voltage = 0;
         //    return 0;
         //}
//
//
//
         //if(lastVoltage!=0) {
         //    setDistanceFromSource(0);
         //    return lastVoltage;
//
         //}




         if(energy.getEnergyStored()==0) {
             voltage= 0;
             return 0;
         }

       // if(voltage>0)
       //     voltage = 0;

         return voltage;
     }

    @Override
    public int FECapacity() {
        return 30000;
    }
    @Override
    public int transferSpeed() {
        return 1000;
    }


    @Override
    public boolean isStorage() {
        return true;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis().isVertical();
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putInt("LastVoltage",lastVoltage);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        lastVoltage = compound.getInt("LastVoltage");

    }
}
