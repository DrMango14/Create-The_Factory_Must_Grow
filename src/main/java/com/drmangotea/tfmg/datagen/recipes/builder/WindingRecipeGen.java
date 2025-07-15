package com.drmangotea.tfmg.datagen.recipes.builder;

import com.drmangotea.tfmg.recipes.CastingRecipe;
import com.drmangotea.tfmg.recipes.WindingRecipe;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public abstract class WindingRecipeGen extends StandardProcessingRecipeGen<WindingRecipe> {

	public WindingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
		super(output, registries, defaultNamespace);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.WINDING;
	}

}