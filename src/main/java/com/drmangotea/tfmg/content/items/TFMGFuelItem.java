package com.drmangotea.tfmg.content.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class TFMGFuelItem extends BlockItem {

    private final int burnTicks;


    public static TFMGFuelItem fossilstone(Block block,Properties properties) {
        return new TFMGFuelItem(block,properties, 4000);
    }
        public static TFMGFuelItem coal_coke(Block block,Properties properties){
            return new TFMGFuelItem(block,properties,3200);
    }
    public TFMGFuelItem(Block p_40565_, Properties p_40566_,int burnTime) {
        super(p_40565_, p_40566_);
        this.burnTicks = burnTime;
    }



    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTicks;
    }
}
