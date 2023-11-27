package com.drmangotea.tfmg.recipes.industrial_blasting;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class IndustrialBlastingRecipe extends ProcessingRecipe<RecipeWrapper> {

	public IndustrialBlastingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.INDUSTRIAL_BLASTING, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
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
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
				.test(inv.getItem(0));
	}

}
