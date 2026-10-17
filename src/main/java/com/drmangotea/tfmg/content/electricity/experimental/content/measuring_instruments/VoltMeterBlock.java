package com.drmangotea.tfmg.content.electricity.experimental.content.measuring_instruments;

import com.drmangotea.tfmg.content.electricity.experimental.content.devices.base.DirectionalResistiveLoadBlock;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.base.HorizontalResistiveLoadBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class VoltMeterBlock extends DirectionalResistiveLoadBlock implements IBE<VoltMeterBlockEntity> {

    public final boolean voltmeter;

    public VoltMeterBlock(Properties p_49795_,boolean voltmeter) {
        super(p_49795_);
        this.voltmeter = voltmeter;
    }

    @Override
    public Class<VoltMeterBlockEntity> getBlockEntityClass() {
        return VoltMeterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VoltMeterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.VOLTMETER.get();
    }
}
