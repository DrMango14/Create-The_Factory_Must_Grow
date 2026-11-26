package com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large;


import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class LargeElectromagneticCoilItem extends BlockItem {
    public LargeElectromagneticCoilItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String text = TFMGLang.translateDirect("tooltip.coils").getString();

        tooltip.add(TFMGLang.text(text+stack.getOrDefault(TFMGDataComponents.COIL_TURNS,0)).component().withStyle(ChatFormatting.GREEN)

        );
        super.appendHoverText(stack, context, tooltip, flag);
    }

}
