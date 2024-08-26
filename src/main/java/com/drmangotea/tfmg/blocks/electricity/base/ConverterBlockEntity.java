package com.drmangotea.tfmg.blocks.electricity.base;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ConverterBlockEntity extends ElectricBlockEntity {


    public ConverterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }





    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.CONVERTER.get());
    }


     @Override
     public int voltageGeneration() {



         if(energy.getEnergyStored()==0) {
            return 0;
         }



         return 255;
     }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis().isVertical();
    }

    @Override
    public int FECapacity() {
        return 10000;
    }



}
