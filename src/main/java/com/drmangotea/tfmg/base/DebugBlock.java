package com.drmangotea.tfmg.base;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class DebugBlock extends Block {
    public DebugBlock(Properties p_49795_) {
        super(p_49795_);
    }
    public boolean shouldHide() {
       return true;
    }
}
