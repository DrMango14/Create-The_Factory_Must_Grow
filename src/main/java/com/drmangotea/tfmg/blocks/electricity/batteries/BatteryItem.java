package com.drmangotea.tfmg.blocks.electricity.batteries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import static com.drmangotea.tfmg.blocks.electricity.batteries.GalvanicCellBlockEntity.GALVANIC_CELL_CAPACITY;

public class BatteryItem extends BlockItem {
    public BatteryItem(Block p_40565_, Properties p_40566_) {
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

        return (int) Math.min(((float) stack.getOrCreateTag().getInt("Capacity")/(float) GALVANIC_CELL_CAPACITY)*13,13f);
    }
}
