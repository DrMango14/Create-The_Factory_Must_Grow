package com.drmangotea.tfmg.content.electricity.generators;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GeneratorBlockEntity extends KineticElectricBlockEntity  {


    public GeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public int voltageGeneration() {
        return (int) Math.min(255,generation());
    }

    @Override
    public int powerGeneration() {
        return generation()*40;
    }

    @Override
    public void tick() {
        super.tick();
        if(data.updateNextTick){
            updateNetwork();
            data.updateNextTick = false;
        }
    }

    @Override
    public void updateNetwork() {
        super.updateNetwork();
    }
//
   // @Override
   // public float calculateStressApplied() {
   //     if(getData().voltageSupply == 0)
   //         return super.calculateStressApplied();
//
   //     if(getNetworkResistance() ==0)
   //         return super.calculateStressApplied();
//
   //     return (int)(Math.min(super.calculateStressApplied()+(getGeneratorLoad() * 0.01f), 1000));
   // }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        updateNextTick();
    }

    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        updateStress();
        sendStuff();
    }

    public void updateStress(){
        if(getOrCreateNetwork() != null) {
            getOrCreateNetwork().remove(this);
            getOrCreateNetwork().add(this);
        }
    }

    public int generation() {
        float modifier = TFMGConfigs.common().machines.generatorModifier.getF();
        float maxSpeed = TFMGConfigs.common().machines.generatorMinSpeed.getF();
        return (int) Math.max(0,((Math.abs(getSpeed())-maxSpeed)* modifier));
    }




}
