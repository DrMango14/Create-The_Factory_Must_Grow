package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.air;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.carbonDioxide;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.hotAir;

public class HotBlastRecipeGen extends TFMGProcessingRecipeGen {

	GeneratedRecipe


	HOT_AIR = create("hot_air", b ->b
			.require(air(),25)
			.require(TFMGTags.TFMGFluidTags.BLAST_STOVE_FUEL.tag,5)
			.output(hotAir(), 25)
			.output(carbonDioxide(), 25)
			.duration(200));
	public HotBlastRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.HOT_BLAST;
	}

}