package com.drmangotea.tfmg.blocks.electricity.base;


import com.drmangotea.tfmg.blocks.electricity.base.cables.CableConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricBlockEntity extends CableConnectorBlockEntity {



    public ElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(1);
    }


    @Override
    public void lazyTick() {
        super.lazyTick();
        //if(energy.getEnergyStored()!=0)
        //    getOrCreateElectricNetwork().transferEnergy(this);
    }








}
