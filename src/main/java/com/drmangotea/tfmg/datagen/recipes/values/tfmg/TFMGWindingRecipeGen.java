package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.WindingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.coil100Turns;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.resistor10Ohms;


public class TFMGWindingRecipeGen extends WindingRecipeGen {

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
	public TFMGWindingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries, TFMG.MOD_ID);
	}



}