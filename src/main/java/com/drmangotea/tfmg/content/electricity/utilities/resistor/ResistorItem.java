package com.drmangotea.tfmg.content.electricity.utilities.resistor;


import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import java.util.List;

public class ResistorItem extends Item {
    public ResistorItem(Properties p_40566_) {
        super(p_40566_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(TFMGLang.translateDirect("tooltip.resistor", stack.get(TFMGDataComponents.RESISTANCE)==null?0:stack.get(TFMGDataComponents.RESISTANCE)).append("Ω")
                .withStyle(ChatFormatting.GREEN)
        );
        super.appendHoverText(stack, context, tooltip, flag);
    }


}
