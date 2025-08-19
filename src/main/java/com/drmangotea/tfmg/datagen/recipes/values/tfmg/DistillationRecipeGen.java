package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;

public class DistillationRecipeGen extends TFMGProcessingRecipeGen {

	GeneratedRecipe

	CRUDE_OIL = create("crude_oil", b ->b
			.require(TFMGTags.TFMGFluidTags.CRUDE_OIL.tag,340)
			.output(heavyOil(), 120)
			.output(diesel(), 60)
			.output(kerosene(), 30)
			.output(naphtha(), 10)
			.output(gasoline(), 60)
			.output(lpg(), 60)),
	CRUDE_OIL_NO_NAPHTHA = create("crude_oil_no_naphtha", b ->b
			.require(TFMGTags.TFMGFluidTags.CRUDE_OIL.tag,330)
			.output(heavyOil(), 120)
			.output(diesel(), 60)
			.output(kerosene(), 30)
			.output(gasoline(), 60)
			.output(lpg(), 60)),
	CRUDE_OIL_LIGHT_DISTILLATION = create("crude_oil_light_distillation", b ->b
			.require(TFMGTags.TFMGFluidTags.CRUDE_OIL.tag,200)
			.output(heavyOil(), 150)
			.output(diesel(), 45)
			.output(gasoline(), 5)),


	HEAVY_OIL = create("heavy_oil", b ->b
			.require(heavyOil(),200)
			.output(heavyOil(), 100)
			.output(lubricationOil(), 25)
			.output(diesel(), 50)
			.output(kerosene(), 20)
			.output(naphtha(), 5)),

	HEAVY_OIL_NO_NAPHTHA = create("heavy_oil_no_naphtha", b ->b
			.require(heavyOil(),200)
			.output(heavyOil(), 100)
			.output(lubricationOil(), 30)
			.output(diesel(), 50)
			.output(kerosene(), 20)),

	HEAVY_OIL_LIGHT_DISTILLATION = create("heavy_oil_light_distillation", b ->b
			.require(heavyOil(),200)
			.output(heavyOil(), 100)
			.output(diesel(), 50)
			.output(lubricationOil(), 50));


;
	public DistillationRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.DISTILLATION;
	}

}