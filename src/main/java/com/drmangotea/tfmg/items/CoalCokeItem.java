package com.drmangotea.tfmg.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.property.Properties;
import org.jetbrains.annotations.Nullable;

public class CoalCokeItem extends Item {




    public CoalCokeItem(Properties p_40566_) {
        super( p_40566_);

    }



    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 3200;
    }
}
