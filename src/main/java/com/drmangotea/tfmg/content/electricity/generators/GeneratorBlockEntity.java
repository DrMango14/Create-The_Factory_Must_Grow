package com.drmangotea.tfmg.content.electricity.generators;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
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

        if (level.isClientSide()) {
            float speed = Math.abs(getSpeed());
            float minSpeed = TFMGConfigs.common().machines.largeGeneratorMinSpeed.getF();

            // Only play sound if above minimum speed
            if (speed > minSpeed) {
                float maxSpeed = 255f; // Max expected speed
                // Normalize speed between 0-1 range (clamped)
                float normalizedSpeed = Math.min(1.0f, (speed - minSpeed) / (maxSpeed - minSpeed));

                // Volume scales from 0.1 to 0.5 with speed
                float volume = 0.1f + (0.4f * normalizedSpeed);

                // Pitch scales from 0.8 to 1.2 with speed (Java clamps below 0.5)
                float pitch = 0.8f + (0.4f * normalizedSpeed);

                TFMGSoundEvents.GENERATOR_HUM.playAt(level, worldPosition, volume, pitch, false);
            }
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
