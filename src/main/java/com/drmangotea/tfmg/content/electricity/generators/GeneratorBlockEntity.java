package com.drmangotea.tfmg.content.electricity.generators;

import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GeneratorBlockEntity extends KineticElectricBlockEntity {

    public GeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int voltageGeneration() {
        return (int) Math.min(255, generation());
    }

    @Override
    public int powerGeneration() {
        return generation() * 40;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide()) {
            float speed = Math.abs(getSpeed());
            float minSpeed = TFMGConfigs.common().machines.largeGeneratorMinSpeed.getF();
            if (speed > minSpeed) {
                float maxSpeed = 255f;
                float normalizedSpeed = Math.min(1.0f, (speed - minSpeed) / (maxSpeed - minSpeed));
                float volume = 0.1f + (0.4f * normalizedSpeed);
                float pitch = 0.8f + (0.4f * normalizedSpeed);
                TFMGSoundEvents.GENERATOR_HUM.playAt(level, worldPosition, volume, pitch, false);
            }
        }
    }

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

    public void updateStress() {
        // Re-add to network so stress calculations pick up the new network state
        getOrCreateElectricNetwork().getMembers().remove(this);
        getOrCreateElectricNetwork().add(this);
    }

    public int generation() {
        float modifier = TFMGConfigs.common().machines.generatorModifier.getF();
        float maxSpeed = TFMGConfigs.common().machines.generatorMinSpeed.getF();
        return (int) Math.max(0, (Math.abs(getSpeed()) - maxSpeed) * modifier);
    }
}
