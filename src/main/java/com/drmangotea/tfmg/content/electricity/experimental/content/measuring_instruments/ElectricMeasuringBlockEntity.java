package com.drmangotea.tfmg.content.electricity.experimental.content.measuring_instruments;

import com.drmangotea.tfmg.content.electricity.utilities.resistor.ResistorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectricMeasuringBlockEntity extends ResistorBlockEntity {
    public ElectricMeasuringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
