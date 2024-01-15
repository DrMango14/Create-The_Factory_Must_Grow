package com.drmangotea.createindustry.base;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class DebugBlock extends Block {
    public DebugBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {}
}
