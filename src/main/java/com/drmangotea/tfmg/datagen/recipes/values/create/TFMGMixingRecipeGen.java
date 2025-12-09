package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.coolingFluid;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.ethylene;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.gasoline;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.liquidAsphalt;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.liquidConcrete;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.napalm;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.sulfuricAcid;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.water;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.aluminumIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.asphaltMixture;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.bitumen;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.cement;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.charcoal;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.clayBall;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.concreteMixture;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.copperIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.copperSulfate;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.gravel;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.gunpowder;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.limesand;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.magneticIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.nickelIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.nitrateDust;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.sand;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.slag;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.steelIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.sulfurDust;
public class TFMGMixingRecipeGen extends TFMGProcessingRecipeGen {

	GeneratedRecipe

	THERMITE = create("thermite", b -> b
			.require(AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get())
			.require(AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get())
			.require(aluminumIngot())
			.require(aluminumIngot())
			.output(TFMGItems.THERMITE_POWDER)
	),

	CEMENT = create("cement", b -> b
			.require(clayBall())
			.require(limesand())
			.output(cement(),4)
	),

	CONSTANTAN = create("constantan", b -> b
			.require(copperIngot())
			.require(nickelIngot())
			.output(TFMGItems.CONSTANTAN_INGOT,2)
			.requiresHeat(HeatCondition.HEATED)
	),

	ASPHALT_MIXTURE = create("asphalt_mixture", b -> b
			.require(sand())
			.require(bitumen())
			.require(gravel())
			.output(asphaltMixture(),16)
	),

	ASPHALT_MIXTURE_FROM_SLAG = create("asphalt_mixture_from_slag", b -> b
			.require(slag())
			.require(bitumen())
			.require(gravel())
			.output(asphaltMixture(),32)
	),

	CONCRETE_MIXTURE = create("concrete_mixture", b -> b
			.require(sand())
			.require(cement())
			.require(gravel())
			.output(concreteMixture(),16)
	),

	CONCRETE_MIXTURE_FROM_SLAG = create("concrete_mixture_from_slag", b -> b
			.require(slag())
			.require(cement())
			.require(gravel())
			.output(concreteMixture(),32)
	),

	COPPER_SULFATE = create("copper_sulfate", b -> b
			.require(sulfuricAcid(),500)
			.require(copperIngot())
			.output(copperSulfate())
	),

	LIQUID_CONCRETE = create("liquid_concrete", b -> b
			.require(water(),250)
			.require(concreteMixture())
			.output(liquidConcrete(),1000)
	),

	LIQUID_ASPHALT = create("liquid_asphalt", b -> b
			.require(water(),250)
			.require(asphaltMixture())
			.output(liquidAsphalt(),1000)
	),

	P_SEMICONDUCTOR = create("p_semiconductor", b -> b
			.require(aluminumIngot())
			.require(TFMGItems.SILICON_INGOT)
			.output(TFMGItems.P_SEMICONDUCTOR)
	),

	N_SEMICONDUCTOR = create("n_semiconductor", b -> b
			.require(sulfurDust())
			.require(TFMGItems.SILICON_INGOT)
			.output(TFMGItems.N_SEMICONDUCTOR)
	),

	GUNPOWDER = create("gunpowder", b -> b
			.require(nitrateDust())
			.require(nitrateDust())
			.require(nitrateDust())
			.require(charcoal())
			.require(charcoal())
			.require(sulfurDust())
			.output(gunpowder(),3)
	),

	NAPALM = create("napalm", b -> b
			.require(gasoline(),1000)
			.require(aluminumIngot())
				.output(napalm(),250)
	),

	COOLING_FLUID = create("cooling_fluid", b -> b
			.require(water(),250)
			.require(ethylene(),1000)
			.output(coolingFluid(),250)
	),

	MAGNETIC_ALLOY = create("magnetic_alloy", b -> b
			.require(nickelIngot())
			.require(nickelIngot())
			.require(TFMGItems.SILICON_INGOT)
			.require(steelIngot())
			.require(steelIngot())
			.output(magneticIngot(),2)
			.duration(300)
			.requiresHeat(HeatCondition.HEATED)
	);



	public TFMGMixingRecipeGen(PackOutput output) {
		super(output);
	}

	@Override
	protected AllRecipeTypes getRecipeType() {
		return AllRecipeTypes.MIXING;
	}

}