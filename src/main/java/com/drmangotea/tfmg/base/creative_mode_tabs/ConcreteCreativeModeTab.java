package com.drmangotea.tfmg.base.creative_mode_tabs;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.infrastructure.item.CreateCreativeModeTab;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ConcreteCreativeModeTab extends TFMGCreativeModeTab {
    public ConcreteCreativeModeTab() {
        super("concrete");
    }

    @Override
    public void addItems(NonNullList<ItemStack> items, boolean specialItems) {
    }

    @Override
    public ItemStack makeIcon() {
        return TFMGItems.COPPER_GRENADE.asStack();
    }
}
