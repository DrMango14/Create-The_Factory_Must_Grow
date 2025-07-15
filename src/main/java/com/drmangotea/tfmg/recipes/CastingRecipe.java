package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class CastingRecipe extends StandardProcessingRecipe<RecipeInput> {

    public CastingRecipe(ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.CASTING, params);
    }
    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }
    public FluidIngredient getIngrenient(){
        return fluidIngredients.get(0);
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    public boolean matches(RecipeInput p_44002_, Level p_44003_) {
        return false;
    }
}
