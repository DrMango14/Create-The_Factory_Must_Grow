package com.drmangotea.tfmg.datagen.recipes.builder;

import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import com.drmangotea.tfmg.recipes.PolarizingRecipeParams;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public abstract class PolarizingRecipeGen extends ProcessingRecipeGen<PolarizingRecipeParams, PolarizingRecipe, PolarizingRecipe.Builder<PolarizingRecipe>> {
	public PolarizingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
		super(output, registries, defaultNamespace);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.POLARIZING;
	}

	@Override
	protected PolarizingRecipe.Builder<PolarizingRecipe> getBuilder(ResourceLocation id) {
		return new PolarizingRecipe.Builder<>(PolarizingRecipe::new, id);
	}
}