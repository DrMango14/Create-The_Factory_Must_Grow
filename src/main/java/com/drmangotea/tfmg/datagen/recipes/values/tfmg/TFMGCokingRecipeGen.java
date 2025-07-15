package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.CokingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.log;

public class TFMGCokingRecipeGen extends CokingRecipeGen {

	GeneratedRecipe

	COAL_COKE = create(TFMG.asResource("coal"), b ->b
			.require(Items.COAL)
			.output(new FluidStack(TFMGFluids.CREOSOTE.get(), 1))
			.output(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get(), 30))
			.output(TFMGItems.COAL_COKE)
			.duration(20*60)),

	CHARCOAL = create(TFMG.asResource("charcoal"), b ->b
			.require(log())
			.output(new FluidStack(TFMGFluids.CREOSOTE.get(), 2))
			.output(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get(), 20))
			.output(Items.CHARCOAL)
			.duration(20*30));
;



	public TFMGCokingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}



}