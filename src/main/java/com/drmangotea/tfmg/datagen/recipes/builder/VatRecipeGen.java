package com.drmangotea.tfmg.datagen.recipes.builder;

import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipeParams;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.recipes.VatRecipeParams;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class VatRecipeGen<P extends ProcessingRecipeParams, R extends ProcessingRecipe<?, P>, B extends ProcessingRecipeBuilder<P, R, B>> extends ProcessingRecipeGen<VatRecipeParams, VatMachineRecipe, VatMachineRecipe.Builder<VatMachineRecipe>> {


	public VatRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String namespace) {
		super(output, registries, namespace);
	}



	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.VAT_MACHINE_RECIPE;
	}

	@Override
	protected VatMachineRecipe.Builder<VatMachineRecipe> getBuilder(ResourceLocation id) {
		return new VatMachineRecipe.Builder<>(VatMachineRecipe::new, id);
	}
}