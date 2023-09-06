package com.drmangotea.tfmg.content.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class CoalCokeBlockItem extends BlockItem {




    public CoalCokeBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);

    }



    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 28800;
    }
}
