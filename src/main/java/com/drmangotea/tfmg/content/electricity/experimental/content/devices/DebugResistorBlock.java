package com.drmangotea.tfmg.content.electricity.experimental.content.devices;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DebugResistorBlock extends DirectionalResistiveLoadBlock implements IBE<DebugResistorBlockEntity> {
    public DebugResistorBlock(Properties properties) {
        super(properties);
    }


    @Override
    public Class<DebugResistorBlockEntity> getBlockEntityClass() {
        return DebugResistorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DebugResistorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DEBUG_RESISTOR.get();
    }
}
