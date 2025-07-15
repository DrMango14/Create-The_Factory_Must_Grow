package com.drmangotea.tfmg.content.electricity.utilities.segmented_display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.SingleLineDisplayTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

public class SegmentedDisplayTarget extends SingleLineDisplayTarget {

    @Override
    protected void acceptLine(MutableComponent text, DisplayLinkContext context) {
        String tagElement = Component.Serializer.toJson(text, context.level().registryAccess());
        SegmentedDisplayBlock.walkParts(context.level(), context.getTargetPos(), (currentPos, rowPosition) -> {
            BlockEntity blockEntity = context.level()
                    .getBlockEntity(currentPos);
            if (blockEntity instanceof SegmentedDisplayBlockEntity display)
                display.displayCustomText(tagElement, rowPosition);
        });
    }

    @Override
    protected int getWidth(DisplayLinkContext context) {
        MutableInt count = new MutableInt(0);
        SegmentedDisplayBlock.walkParts(context.level(), context.getTargetPos(), (currentPos, rowPosition) -> count.add(2));
        return count.intValue();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getMultiblockBounds(LevelAccessor level, BlockPos pos) {
        MutableObject<BlockPos> start = new MutableObject<>(null);
        MutableObject<BlockPos> end = new MutableObject<>(null);
        SegmentedDisplayBlock.walkParts(level, pos, (currentPos, rowPosition) -> {
            end.setValue(currentPos);
            if (start.getValue() == null)
                start.setValue(currentPos);
        });

        BlockPos diffToCurrent = start.getValue()
                .subtract(pos);
        BlockPos diff = end.getValue()
                .subtract(start.getValue());

        return super.getMultiblockBounds(level, pos).move(diffToCurrent)
                .expandTowards(Vec3.atLowerCornerOf(diff));
    }
}
