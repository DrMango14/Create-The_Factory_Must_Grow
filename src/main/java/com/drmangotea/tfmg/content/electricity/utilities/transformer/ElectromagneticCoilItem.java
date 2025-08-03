package com.drmangotea.tfmg.content.electricity.utilities.transformer;


import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import java.util.List;

public class ElectromagneticCoilItem extends Item {
    public ElectromagneticCoilItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(TFMGLang.translateDirect("tooltip.coils", stack.get(TFMGDataComponents.COIL_TURNS)==null?0:stack.get(TFMGDataComponents.COIL_TURNS))
                .withStyle(ChatFormatting.GREEN)
        );
        super.appendHoverText(stack, context, tooltip, flag);
    }

}
