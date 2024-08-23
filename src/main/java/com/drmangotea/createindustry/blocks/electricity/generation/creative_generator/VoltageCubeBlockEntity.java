package com.drmangotea.createindustry.blocks.electricity.generation.creative_generator;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VoltageCubeBlockEntity extends ElectricBlockEntity {
    public VoltageCubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public int voltageGeneration() {
        return 10;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return true;
    }
}
