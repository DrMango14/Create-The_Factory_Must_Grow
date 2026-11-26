package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.DistillationRecipeGen;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;

@SuppressWarnings("unused")
public class TFMGDistillationRecipeGen extends DistillationRecipeGen {

	GeneratedRecipe

	CRUDE_OIL = create(TFMG.asResource("crude_oil"), b ->b
			.duration(1200)
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.CRUDE_OIL.tag,340))
			.output(heavyOil(), 120)
			.output(diesel(), 60)
			.output(kerosene(), 30)
			.output(naphtha(), 10)
			.output(gasoline(), 60)
			.output(lpg(), 60)),
	CRUDE_OIL_NO_NAPHTHA = create(TFMG.asResource("crude_oil_no_naphtha"), b -> b
			.duration(1000)
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.CRUDE_OIL.tag,330))
			.output(heavyOil(), 120)
			.output(diesel(), 60)
			.output(kerosene(), 30)
			.output(gasoline(), 60)
			.output(lpg(), 60)),
	CRUDE_OIL_LIGHT_DISTILLATION = create(TFMG.asResource("crude_oil_light_distillation"), b -> b
			.duration(600)
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.CRUDE_OIL.tag,200))
			.output(heavyOil(), 150)
			.output(diesel(), 45)
			.output(gasoline(), 5)),


	HEAVY_OIL = create(TFMG.asResource("heavy_oil"), b -> b
			.duration(1000)
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.HEAVY_OIL.tag,200))
			.output(heavyOil(), 100)
			.output(lubricationOil(), 25)
			.output(diesel(), 50)
			.output(kerosene(), 20)
			.output(naphtha(), 5)),

	HEAVY_OIL_NO_NAPHTHA = create(TFMG.asResource("heavy_oil_no_naphtha"), b -> b
			.duration(800)
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.HEAVY_OIL.tag,200))
			.output(heavyOil(), 100)
			.output(lubricationOil(), 30)
			.output(diesel(), 50)
			.output(kerosene(), 20)),

	HEAVY_OIL_LIGHT_DISTILLATION = create(TFMG.asResource("heavy_oil_light_distillation"), b -> b
			.duration(600)
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.HEAVY_OIL.tag,200))
			.output(heavyOil(), 100)
			.output(diesel(), 50)
			.output(lubricationOil(), 50));


;
	public TFMGDistillationRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries,TFMG.MOD_ID);
	}

	@Override
	protected TFMGRecipeTypes getRecipeType() {
		return TFMGRecipeTypes.DISTILLATION;
	}

}
