package com.drmangotea.tfmg.blocks.electricity.storage;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CapacitorBlockEntity extends ElectricBlockEntity {

    public int lastVoltage=0;

    public CapacitorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }




    @Override
    public void lazyTick() {
        super.lazyTick();

        getOrCreateElectricNetwork().requestEnergy(this);
    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.CAPACITOR.get());
    }



     @Override
     public int voltageGeneration() {


         if(energy.getEnergyStored()==0) {
            // setVoltage(0);
             return 0;
         }

       // if(voltage>0)
       //     voltage = 0;

         return getVoltage();
     }

    @Override
    public int FECapacity() {
        return 75000;
    }

    @Override
    public int FETransferSpeed() {
        return 1000;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction==getBlockState().getValue(DirectionalBlock.FACING)||direction==getBlockState().getValue(DirectionalBlock.FACING).getOpposite();
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
