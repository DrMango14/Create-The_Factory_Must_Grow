package com.drmangotea.tfmg.recipes.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public class DistillationRecipeInput implements RecipeInput {
    public final FluidStack fluid;
    public final int outputs;

    public DistillationRecipeInput(FluidStack fluid, int outputs) {
        this.fluid = fluid;
        this.outputs = outputs;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 0;
    }
}
