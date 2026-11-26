package com.drmangotea.tfmg.recipes.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public class CastingRecipeInput implements RecipeInput {
    public FluidStack fluid;

    public CastingRecipeInput(FluidStack fluid) {
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
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
