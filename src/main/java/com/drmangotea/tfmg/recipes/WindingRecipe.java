package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.integration.jei.category.WindingCategory;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class WindingRecipe extends StandardProcessingRecipe<RecipeWrapper> implements IAssemblyRecipe {
	public WindingRecipe(ProcessingRecipeParams params) {
		super(TFMGRecipeTypes.WINDING, params);
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	@Override
	protected int getMaxInputCount() {
		return 2;
	}

	@Override
	protected int getMaxOutputCount() {
		return 1;
	}

	public Ingredient getIngredient(){
		return getIngredients().getFirst();
	}
	public Ingredient getSpool(){
		return getIngredients().get(1);
	}
	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		return !inv.isEmpty() && ingredients.getFirst().test(inv.getItem(0));
	}

	@Override
	public Component getDescriptionForAssembly() {
		ItemStack[] matchingStacks = getSpool().getItems();
		if (matchingStacks.length == 0) {
			return Component.literal("Invalid");
		}
		return TFMGLang.translateDirect("recipe.assembly.winding.count", Component.translatable(matchingStacks[0].getDescriptionId()).getString());
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
		return () -> WindingCategory.AssemblyWinding::new;
	}
}
