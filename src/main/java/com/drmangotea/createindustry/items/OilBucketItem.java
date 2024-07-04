package com.drmangotea.createindustry.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class OilBucketItem extends BucketItem {
    public OilBucketItem(Fluid p_40689_, Properties p_40690_) {
        super(p_40689_, p_40690_);
    }
    public OilBucketItem(java.util.function.Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier,builder);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 800;
    }
}
