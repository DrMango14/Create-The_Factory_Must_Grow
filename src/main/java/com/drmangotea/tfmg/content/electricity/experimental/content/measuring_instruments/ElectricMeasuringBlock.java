package com.drmangotea.tfmg.content.electricity.experimental.content.measuring_instruments;

import com.drmangotea.tfmg.content.electricity.experimental.content.devices.DirectionalResistiveLoadBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ElectricMeasuringBlock extends DirectionalResistiveLoadBlock implements IBE<ElectricMeasuringBlockEntity> {
    public ElectricMeasuringBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public Class<ElectricMeasuringBlockEntity> getBlockEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends ElectricMeasuringBlockEntity> getBlockEntityType() {
        return null;
    }
}
