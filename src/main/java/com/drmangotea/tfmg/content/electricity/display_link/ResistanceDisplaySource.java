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

public class ResistanceDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof SmartBlockEntity smart))
            return EMPTY_LINE;
        if (!(smart instanceof IElectric electric))
            return EMPTY_LINE;

        float value = electric.voltageGeneration() > 0 ? electric.getGeneratorResistance() : electric.resistance();

        if (smart.getBehaviour(ObservedElectricBehaviour.TYPE) != null) {
            IElectric observed = smart.getBehaviour(ObservedElectricBehaviour.TYPE).getObservedElectric();
            if (observed != null) value = observed.voltageGeneration() > 0 ? observed.getGeneratorResistance() : observed.resistance();
        }

        return Component.literal(TFMGUtils.formatUnits(value, "Ω"));
    }

    @Override
    protected String getTranslationKey() {
        return "electricity.resistance";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
