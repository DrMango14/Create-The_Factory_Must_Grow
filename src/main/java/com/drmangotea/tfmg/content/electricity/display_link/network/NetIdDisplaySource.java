package com.drmangotea.tfmg.content.electricity.display_link.network;

import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.ObservedElectricBehaviour;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NetIdDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof SmartBlockEntity smart))
            return EMPTY_LINE;
        if (!(smart instanceof IElectric electric))
            return EMPTY_LINE;

        double id = electric.getOrCreateElectricNetwork().id;

        if (smart.getBehaviour(ObservedElectricBehaviour.TYPE) != null) {
            IElectric observed = smart.getBehaviour(ObservedElectricBehaviour.TYPE).getObservedElectric();
            if (observed != null) id = observed.getOrCreateElectricNetwork().id;
        }

        if (id == -1) return EMPTY_LINE;

        return CreateLang.number(id).component();
    }

    @Override
    protected String getTranslationKey() {
        return "electricity.network_id";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
