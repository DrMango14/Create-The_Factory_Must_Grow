package com.drmangotea.tfmg.content.electricity.display_link;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.ObservedElectricBehaviour;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public class VoltageGenerationDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof SmartBlockEntity smart))
            return EMPTY_LINE;
        if (!(smart instanceof IElectric electric))
            return EMPTY_LINE;

        int voltageGen = electric.voltageGeneration();

        if (smart.getBehaviour(ObservedElectricBehaviour.TYPE) != null) {
            IElectric observed = smart.getBehaviour(ObservedElectricBehaviour.TYPE).getObservedElectric();
            if (observed != null) voltageGen = observed.voltageGeneration();
        }

        return Component.literal(TFMGUtils.formatUnits(voltageGen, "V"));
    }

    @Override
    protected String getTranslationKey() {
        return "electricity.voltage_generation";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
