package com.drmangotea.createindustry.blocks.machines.simple.welding_machine;

import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.compat.recipeViewerCommon.SequencedAssemblySubCategoryType;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.transfer.item.RecipeWrapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public class WeldingRecipe extends ProcessingRecipe<RecipeWrapper> implements IAssemblyRecipe {

	public WeldingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.WELDING, params);
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
			.test(inv.getItem(0));
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 2;
	}

	@Override
	public void addAssemblyIngredients(List<Ingredient> list) {}

	@Override
	@Environment(EnvType.CLIENT)
	public Component getDescriptionForAssembly() {
		return Lang.translateDirect("recipe.assembly.welding");
	}
	
	@Override
	public void addRequiredMachines(Set<ItemLike> list) {
		//list.add(TFMGBlocks.WELDING_MACHINE.get());
	}
	
	@Override
	public SequencedAssemblySubCategoryType getJEISubCategory() {
		return SequencedAssemblySubCategoryType.PRESSING;
	}

}
