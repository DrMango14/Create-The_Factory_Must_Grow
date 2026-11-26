package com.drmangotea.tfmg.datagen.recipes.builder;

import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.recipes.VatRecipeParams;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public abstract class VatRecipeGen extends ProcessingRecipeGen<VatRecipeParams, VatMachineRecipe, VatMachineRecipe.Builder<VatMachineRecipe>> {
	public VatRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String namespace) {
		super(output, registries, namespace);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.VAT;
	}

	@Override
	protected VatMachineRecipe.Builder<VatMachineRecipe> getBuilder(ResourceLocation id) {
		return new VatMachineRecipe.Builder<>(VatMachineRecipe::new, id);
	}
}