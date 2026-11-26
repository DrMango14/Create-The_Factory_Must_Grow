package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.CastingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import rbasamoyai.createbigcannons.index.CBCItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class TFMGCastingRecipeGen extends CastingRecipeGen {

	BaseRecipeProvider.GeneratedRecipe

	STEEL_INGOT = create("steel", b ->b
			.require(TFMGTags.Fluids.MOLTEN_STEEL.tag, 90)
			.output(TFMGItems.STEEL_INGOT)
			.duration(200)),


	PLASTIC_SHEET = create("plastic_sheet", b ->b
			.require(TFMGFluids.MOLTEN_PLASTIC.get(), 90)
			.output(TFMGItems.PLASTIC_SHEET)
			.duration(100)),

	SLAG_BLOCK = create("slag_block", b ->b
			.require(TFMGFluids.MOLTEN_SLAG.get(), 20)
			.output(TFMGBlocks.SLAG_BLOCK)
			.duration(50)),

	CINDERBLOCK = create("cinderblock", b ->b
			.require(TFMGFluids.LIQUID_CONCRETE.get(), 90)
			.output(TFMGItems.CINDERBLOCK)
			.duration(50)),

	SILICON = create("silicon", b ->b
			.require(TFMGFluids.LIQUID_SILICON.get(), 90)
			.output(TFMGItems.SILICON_INGOT)
			.duration(200));

;
	//CBC
	BaseRecipeProvider.GeneratedRecipe
			CAST_IRON_CBC = create("cast_iron", b -> b.whenModLoaded("createbigcannons")
					.require(TFMGTags.Fluids.MOLTEN_CAST_IRON.tag, 90)
					.output(TFMGItems.CAST_IRON_INGOT)
					.duration(200)),
			BRONZE_CBC = create("bronze", b -> b.whenModLoaded("createbigcannons")
					.require(TFMGTags.Fluids.MOLTEN_BRONZE.tag, 90)
					.output(CBCItems.BRONZE_INGOT)
					.duration(200)),
			NETHERSTEEL_CBC = create("nethersteel", b -> b.whenModLoaded("createbigcannons")
					.require(TFMGTags.Fluids.MOLTEN_NETHERSTEEL.tag, 90)
					.output(CBCItems.NETHERSTEEL_INGOT)
					.duration(200));
	
	public TFMGCastingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}


}
