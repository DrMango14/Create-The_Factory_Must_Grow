package com.drmangotea.tfmg.content.electricity.utilities.resistor;


import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ResistorItem extends Item {
    public ResistorItem(Properties p_40566_) {
        super(p_40566_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(CreateLang.translateDirect("tooltip.resistor", stack.getOrCreateTag().getInt("Resistance")).append("Ω")
                .withStyle(ChatFormatting.GREEN)
        );
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
