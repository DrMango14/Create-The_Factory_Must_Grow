package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CokingRecipe extends ProcessingRecipe<RecipeWrapper> {

	public CokingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.COKING, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 1;
	}

	@Override
	protected int getMaxFluidOutputCount() {
		return 2;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	public FluidStack getPrimaryResult() {
		return getFluidResults().get(0);
	}

	public FluidStack getSecondaryResult() {
		return getFluidResults().get(1);
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
				.test(inv.getItem(0));
	}

}
