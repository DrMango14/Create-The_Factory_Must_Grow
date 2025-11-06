package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.coil100Turns;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.resistor10Ohms;


public class WindingRecipeGen extends TFMGProcessingRecipeGen {

	GeneratedRecipe


	ELECTROMAGNETIC_COIL = create("electromagnetic_coil", b ->b
			.require(TFMGItems.UNFINISHED_ELECTROMAGNETIC_COIL)
			.require(TFMGItems.COPPER_SPOOL)
			.output(coil100Turns())
			.duration(100)),
	RESISTOR = create("resistor", b ->b
			.require(TFMGItems.UNFINISHED_RESISTOR)
			.require(TFMGItems.CONSTANTAN_SPOOL)
			.output(resistor10Ohms())
			.duration(50))
;
	public WindingRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.WINDING;
	}

}