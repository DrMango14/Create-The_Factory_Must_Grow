package com.drmangotea.tfmg.content.electricity.storage;

import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;



public class AccumulatorItem extends BlockItem {
    public AccumulatorItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return 0x51DBD4;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if(stack.get(TFMGDataComponents.ACCUMULATOR_STORAGE)==null)
            return 0;

        return (int) Math.min(((float) stack.get(TFMGDataComponents.ACCUMULATOR_STORAGE)/(float) TFMGConfigs.common().machines.accumulatorStorage.get())*13,13f);
    }
}
