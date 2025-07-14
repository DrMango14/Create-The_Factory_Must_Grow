package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class HotBlastRecipe extends StandardProcessingRecipe<RecipeWrapper> {

    public HotBlastRecipe(ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.HOT_BLAST, params);
    }
    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }

    public FluidStack getPrimaryResult(){
        return getFluidResults().get(0);
    }
    public FluidStack getSecondaryResult(){
        return getFluidResults().get(1);
    }

    public FluidIngredient getPrimaryIngredient(){
        return getFluidIngredients().get(0);
    }
    public FluidIngredient getSecondaryIngredient(){
        return getFluidIngredients().get(1);
    }


    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }



}
