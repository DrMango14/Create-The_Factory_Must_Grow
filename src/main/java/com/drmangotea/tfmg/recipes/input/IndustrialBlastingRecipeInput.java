package com.drmangotea.tfmg.recipes.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class IndustrialBlastingRecipeInput implements RecipeInput {
    public ItemStack input;
    public ItemStack flux;

    public IndustrialBlastingRecipeInput(ItemStack input, ItemStack flux) {
        this.input = input;
        this.flux = flux;
    }

    @Override
    public ItemStack getItem(int i) {
        return switch (i) {
            case 0 -> input;
            case 1 -> flux;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
