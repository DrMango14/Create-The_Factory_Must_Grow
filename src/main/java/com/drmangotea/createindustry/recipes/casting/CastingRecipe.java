package com.drmangotea.createindustry.recipes.casting;

import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import io.github.fabricators_of_create.porting_lib.transfer.item.RecipeWrapper;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CastingRecipe extends ProcessingRecipe<RecipeWrapper> {

	public CastingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.CASTING, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 0;
	}

	@Override
	protected int getMaxOutputCount() {
		return 3;
	}

	@Override
	protected int getMaxFluidInputCount() {
		return 1;
	}

	public boolean matches(CombinedTankWrapper inv, Level worldIn) {
		if (inv.parts.get(0).iterator().next().getAmount() ==0)
			return false;
		return fluidIngredients.get(0)
				.test(new FluidStack(inv.parts.get(0).iterator().next().getResource(), inv.parts.get(0).iterator().next().getAmount()));
	}
	public FluidIngredient getInputFluid(){
		return getFluidIngredients().get(0);
	}
	public ItemStack getIngotResult(){
		return results.get(0).getStack();
	}
	public ItemStack getBlockResult(){
		return results.get(1).getStack();
	}


	public static int getOutputCount(CastingRecipe recipe){
		return recipe.results.toArray().length;
	}


	@Override
	public boolean matches(RecipeWrapper pContainer, Level pLevel) {
		return false;
	}

}
