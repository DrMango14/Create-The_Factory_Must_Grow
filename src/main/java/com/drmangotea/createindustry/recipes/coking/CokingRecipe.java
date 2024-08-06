package com.drmangotea.createindustry.recipes.coking;

import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import io.github.fabricators_of_create.porting_lib.transfer.item.RecipeWrapper;
import net.minecraft.world.level.Level;

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
		return 1;
	}


	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
				.test(inv.getItem(0));
	}

}
