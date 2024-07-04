package com.drmangotea.createindustry.blocks.engines.radial.large;

import com.drmangotea.createindustry.blocks.engines.radial.RadialEngineBlockEntity;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class LargeRadialEngineBlockEntity extends RadialEngineBlockEntity {
    public LargeRadialEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public float getGeneratedSpeed() {

        int signal = Math.max(this.signal,inputSingal);

        if(!level.isClientSide){


            calculateEfficiency();
            fuelConsumption = (int)((speed/(efficiency/10)/5)+1);
            if(fuelConsumption<1)
                fuelConsumption=0;
            if(!tankInventory.isEmpty()) {

                if(consumptionTimer>=45) {
                    if(signal!=0)
                        tankInventory.drain(fuelConsumption, IFluidHandler.FluidAction.EXECUTE);
                    consumptionTimer=0;
                }
                consumptionTimer++;


                return ((signal*signal)*0.8f)*powerModifier;

            }}
        return 0;


    }
    public TagKey<Fluid> validFuel(){
        return TFMGTags.TFMGFluidTags.KEROSENE.tag;
    };
}
