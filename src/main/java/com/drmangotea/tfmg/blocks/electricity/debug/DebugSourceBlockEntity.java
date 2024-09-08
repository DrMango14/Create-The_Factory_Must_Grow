package com.drmangotea.tfmg.blocks.electricity.debug;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DebugSourceBlockEntity extends DebugElectricBlockEntity {



    public DebugSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


   // @Override
   // public int voltageGeneration() {
   //     return getBlockPos().getX();
   // }

    @Override
    public int voltageGeneration() {
       return 100;
    }

    @Override
    public boolean outputAllowed() {
        return true;
    }

    @Override
    public int FEProduction() {
        return 100;
    }
}
