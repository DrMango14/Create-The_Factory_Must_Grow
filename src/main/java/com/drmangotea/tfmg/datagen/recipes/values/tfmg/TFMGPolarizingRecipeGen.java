package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.PolarizingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class TFMGPolarizingRecipeGen extends PolarizingRecipeGen {

	GeneratedRecipe

	MAGNET = create(TFMG.asResource("magnet"), b ->b
			.require(TFMGItems.MAGNETIC_ALLOY_INGOT)
			.output(TFMGItems.MAGNET));
;
	public TFMGPolarizingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}

}