package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;


public class CokingRecipe extends StandardProcessingRecipe<RecipeWrapper> {

	public CokingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.COKING, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}
	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}
	@Override
	protected int getMaxOutputCount() {
		return 1;
	}
	@Override
	protected int getMaxFluidOutputCount() {
		return 2;
	}

	public FluidStack getPrimaryResult(){
		return getFluidResults().getFirst();
	}
	public FluidStack getSecondaryResult(){
		return getFluidResults().get(1);
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.getFirst().test(inv.getItem(0));
	}

}
