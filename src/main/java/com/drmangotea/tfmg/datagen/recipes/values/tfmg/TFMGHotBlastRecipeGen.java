package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.HotBlastRecipeGen;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;

public class TFMGHotBlastRecipeGen extends HotBlastRecipeGen {

	GeneratedRecipe


	HOT_AIR = create(TFMG.asResource("hot_air"), b ->b
			.require(SizedFluidIngredient.of(air(),25))
			.require(TFMGTags.TFMGFluidTags.BLAST_STOVE_FUEL.tag,5)
			.output(hotAir(), 25)
			.output(carbonDioxide(), 25)
			.duration(200));
	public TFMGHotBlastRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}

}