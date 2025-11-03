package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.CastingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class TFMGCastingRecipeGen extends CastingRecipeGen {

	BaseRecipeProvider.GeneratedRecipe

	STEEL_INGOT = create("steel", b ->b
			.require(TFMGFluids.MOLTEN_STEEL.get(),144)
			.output(TFMGItems.STEEL_INGOT)
			.duration(200)),


	PLASTIC_SHEET = create("plastic_sheet", b ->b
			.require(TFMGFluids.MOLTEN_PLASTIC.get(),200)
			.output(TFMGItems.PLASTIC_SHEET)
			.duration(100)),

	SLAG_BLOCK = create("slag_block", b ->b
			.require(TFMGFluids.MOLTEN_SLAG.get(),20)
			.output(TFMGBlocks.SLAG_BLOCK)
			.duration(50)),

	CINDERBLOCK = create("cinderblock", b ->b
			.require(TFMGFluids.LIQUID_CONCRETE.get(),144)
			.output(TFMGItems.CINDERBLOCK)
			.duration(50)),

	SILICON = create("silicon", b ->b
			.require(TFMGFluids.LIQUID_SILICON.get(),144)
			.output(TFMGItems.SILICON_INGOT)
			.duration(200));

;
	public TFMGCastingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}


}