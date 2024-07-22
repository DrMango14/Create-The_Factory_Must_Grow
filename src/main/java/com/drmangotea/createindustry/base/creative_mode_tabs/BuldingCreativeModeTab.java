package com.drmangotea.createindustry.base.creative_mode_tabs;


import com.drmangotea.createindustry.base.TFMGColoredBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class BuldingCreativeModeTab extends TFMGCreativeModeTab {
    public BuldingCreativeModeTab() {
        super("building");
    }

    @Override
    public void addItems(NonNullList<ItemStack> items, boolean specialItems) {
    }

    @Override
    public ItemStack makeIcon() {
        return TFMGColoredBlocks.MAGENTA_CONCRETE.asStack();
    }
}
