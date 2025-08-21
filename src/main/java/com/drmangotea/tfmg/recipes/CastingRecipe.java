package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CastingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public CastingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.CASTING, params);
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
	protected boolean canSpecifyDuration() {
		return true;
	}

    @Override
    public boolean matches(RecipeWrapper p_44002_, Level p_44003_) {
        return false;
    }
}
