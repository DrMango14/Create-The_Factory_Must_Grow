package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class LargeElectromagneticCoilItem extends BlockItem {

    public LargeElectromagneticCoilItem(LargeCoilBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int turns = 0;
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Turns"))
            turns = tag.getInt("Turns");
        tooltip.add(Component.literal("Turns: " + turns)
                .withStyle(ChatFormatting.GREEN));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
