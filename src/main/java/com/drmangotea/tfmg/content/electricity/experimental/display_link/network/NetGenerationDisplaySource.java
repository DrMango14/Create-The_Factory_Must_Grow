package com.drmangotea.tfmg.content.electricity.experimental.display_link.network;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.IOldElectric;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.voltage_observer.ObservedElectricBehaviour;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NetGenerationDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof SmartBlockEntity smart))
            return EMPTY_LINE;
        if (!(smart instanceof IOldElectric electric))
            return EMPTY_LINE;

        int netGeneration = electric.getNetworkPowerGeneration();

        if (smart.getBehaviour(ObservedElectricBehaviour.TYPE) != null) {
            IOldElectric observed = smart.getBehaviour(ObservedElectricBehaviour.TYPE).getObservedElectric();
            if (observed != null) netGeneration = observed.getNetworkPowerGeneration();
        }

        return Component.literal(TFMGUtils.formatUnits(netGeneration, "W"));
    }

    @Override
    protected String getTranslationKey() {
        return "electricity.network_generation";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
