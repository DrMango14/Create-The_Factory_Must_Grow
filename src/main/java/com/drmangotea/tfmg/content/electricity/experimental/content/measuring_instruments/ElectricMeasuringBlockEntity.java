package com.drmangotea.tfmg.content.electricity.experimental.content.measuring_instruments;

import com.drmangotea.tfmg.content.electricity.experimental.content.devices.base.ResistiveLoadBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricMeasuringBlockEntity extends ResistiveLoadBlockEntity {

    public LerpedFloat angle = LerpedFloat.angular();



    public ElectricMeasuringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        angle.chase(voltage/maxValue()*180,0.1, LerpedFloat.Chaser.EXP);
        angle.tickChaser();

    }

    public int maxValue(){
        return 500;
    }

    public double measuredValue(){
        return voltage;
    }
}
