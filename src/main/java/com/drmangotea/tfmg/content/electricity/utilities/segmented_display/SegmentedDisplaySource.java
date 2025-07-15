package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SegmentedDisplaySource extends SingleLineDisplaySource {

    @Override
    protected String getTranslationKey() {
        return "segmented_display";
    }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof SegmentedDisplayBlockEntity nbe))
            return EMPTY_LINE;

        MutableComponent text = nbe.getFullText();

        try {
            String line = text.getString();
            Integer.valueOf(line);
            context.flapDisplayContext = Boolean.TRUE;
        } catch (NumberFormatException e) {
        }

        return text;
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return !(context.blockEntity().activeTarget instanceof SegmentedDisplayTarget);
    }

    @Override
    protected String getFlapDisplayLayoutName(DisplayLinkContext context) {
        if (isNumeric(context))
            return "Number";
        return super.getFlapDisplayLayoutName(context);
    }

    @Override
    protected FlapDisplaySection createSectionForValue(DisplayLinkContext context, int size) {
        if (isNumeric(context))
            return new FlapDisplaySection(size * FlapDisplaySection.MONOSPACE, "numeric", false, false);
        return super.createSectionForValue(context, size);
    }

    protected boolean isNumeric(DisplayLinkContext context) {
        return context.flapDisplayContext == Boolean.TRUE;
    }

}
