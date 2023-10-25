package com.drmangotea.tfmg.base.creative_mode_tabs;

import com.drmangotea.tfmg.registry.TFMGBlocks;

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
        return TFMGBlocks.CONCRETE.asStack();
    }
}
