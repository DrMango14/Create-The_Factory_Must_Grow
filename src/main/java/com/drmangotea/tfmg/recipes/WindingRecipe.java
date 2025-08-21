package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.recipes.jei.WindingCategory;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class WindingRecipe extends ProcessingRecipe<RecipeWrapper>  implements IAssemblyRecipe {

	public WindingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.WINDING, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 2;
	}

	@Override
	protected int getMaxOutputCount() {
		return 1;
	}

	@Override
    protected boolean canSpecifyDuration() {
        return true;
    }


	public Ingredient getIngredient(){
		return getIngredients().get(0);
	}
	public Ingredient getSpool(){
		return getIngredients().get(1);
	}
	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
				.test(inv.getItem(0));
	}
//
	@Override
	public Component getDescriptionForAssembly() {
		return CreateLang.translateDirect("recipe.assembly.winding");
	}

	@Override
	public void addRequiredMachines(Set<ItemLike> list) {
		list.add(TFMGBlocks.WINDING_MACHINE.get());
	}

	@Override
	public void addAssemblyIngredients(List<Ingredient> list) {
		list.add(ingredients.get(1));
	}

	@Override
	public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
		return ()->WindingCategory.AssemblyWinding::new;
	}
}
