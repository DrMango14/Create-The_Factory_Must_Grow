package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.HotBlastRecipeGen;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.carbonDioxide;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.hotAir;

@SuppressWarnings("unused")
public class TFMGHotBlastRecipeGen extends HotBlastRecipeGen {

	GeneratedRecipe


	HOT_AIR = create(TFMG.asResource("hot_air"), b ->b
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.AIR.tag,50))
			.require(TFMGTags.Fluids.BLAST_STOVE_FUEL.tag,10)
			.output(hotAir(), 50)
			.output(carbonDioxide(), 50)
			.duration(200));
	public TFMGHotBlastRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}

}
