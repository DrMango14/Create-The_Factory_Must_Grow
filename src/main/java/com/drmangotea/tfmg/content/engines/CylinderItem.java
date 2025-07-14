package com.drmangotea.tfmg.content.engines;


import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import java.util.List;

public class CylinderItem extends Item {


    public CylinderItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        if(stack.get(TFMGDataComponents.FUELS)==null)
            return;

        CompoundTag fuels = stack.get(TFMGDataComponents.FUELS);

        if(fuels.isEmpty())
            return;
        tooltip.add(CreateLang.translateDirect("tooltip.cylinder")
                .withStyle(ChatFormatting.GRAY));

        for(String key : fuels.getAllKeys()) {


            MutableComponent component = CreateLang.text("- ").component()
                    .append(Component.translatable(fuels.getString(key)))
                    .withStyle(ChatFormatting.AQUA);
            tooltip.add(component);
        }

    }


}
