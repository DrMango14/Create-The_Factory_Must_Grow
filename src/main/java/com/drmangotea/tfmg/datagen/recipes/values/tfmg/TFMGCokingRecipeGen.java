package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.CokingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.carbonDioxide;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.creosote;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

@SuppressWarnings("unused")
public class TFMGCokingRecipeGen extends CokingRecipeGen {

	GeneratedRecipe

	COAL_COKE = create(TFMG.asResource("coal"), b ->b
			.require(coal())
			.output(creosote(), 1)
			.output(carbonDioxide(), 30)
			.output(coalCoke())
			.duration(20*60)),

	CHARCOAL = create(TFMG.asResource("charcoal"), b ->b
			.require(log())
			.output(creosote(), 2)
			.output(carbonDioxide(), 20)
			.output(charcoal())
			.duration(20*30));

	public TFMGCokingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}
}