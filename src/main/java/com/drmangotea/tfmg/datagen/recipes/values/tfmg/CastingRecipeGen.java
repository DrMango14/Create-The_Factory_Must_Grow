package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.*;
import net.minecraft.data.PackOutput;

public class CastingRecipeGen extends TFMGProcessingRecipeGen {

	GeneratedRecipe

	STEEL_INGOT = create("steel", b ->b
			.require(TFMGTags.TFMGFluidTags.MOLTEN_STEEL.tag, 144)
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
	public CastingRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.CASTING;
	}

}