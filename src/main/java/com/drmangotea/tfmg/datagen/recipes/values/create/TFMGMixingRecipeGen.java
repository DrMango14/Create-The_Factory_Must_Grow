package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.FalseCondition;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

@SuppressWarnings("unused")
public class TFMGMixingRecipeGen extends MixingRecipeGen {

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
			.require(Tags.Items.SANDS_COLORLESS)
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
			.require(Tags.Items.SANDS_COLORLESS)
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
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.SULFURIC_ACID.tag,500))
			.require(copperIngot())
			.output(copperSulfate())
	),

	LIQUID_CONCRETE = create("liquid_concrete", b -> b
			.require(SizedFluidIngredient.of(water(),250))
			.require(concreteMixture())
			.output(liquidConcrete(),1000)
	),

	LIQUID_ASPHALT = create("liquid_asphalt", b -> b
			.require(SizedFluidIngredient.of(water(),250))
			.require(asphaltMixture())
			.output(liquidAsphalt(),1000)
	),

	P_SEMICONDUCTOR = create("p_semiconductor", b -> b
			.require(aluminumIngot())
			.require(TFMGTags.Items.INGOTS_SILICON.tag)
			.output(TFMGItems.P_SEMICONDUCTOR)
	),

	N_SEMICONDUCTOR = create("n_semiconductor", b -> b
			.require(TFMGTags.Items.DUSTS_SULFUR.tag)
			.require(TFMGTags.Items.INGOTS_SILICON.tag)
			.output(TFMGItems.N_SEMICONDUCTOR)
	),

	GUNPOWDER = create("gunpowder", b -> b
			.require(TFMGTags.Items.DUSTS_SALTPETER.tag)
			.require(TFMGTags.Items.DUSTS_SALTPETER.tag)
			.require(TFMGTags.Items.DUSTS_SALTPETER.tag)
			.require(charcoal())
			.require(charcoal())
			.require(TFMGTags.Items.DUSTS_SULFUR.tag)
			.output(gunpowder(),3)
	),

	NAPALM = create("napalm", b -> b
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.GASOLINE.tag, 1000))
			.require(aluminumIngot())
			.output(napalm(),250)
	),

	COOLING_FLUID = create("cooling_fluid", b -> b
			.require(SizedFluidIngredient.of(water(),250))
			.require(SizedFluidIngredient.of(TFMGTags.Fluids.ETHYLENE.tag, 1000))
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

	//Overriding CBC recipes
	//TODO: find & override CBC cast iron recipe
	GeneratedRecipe
		ALLOY_NETHERSTEEL_CAST_IRON = overrideOther(cbcLoc("alloy_nethersteel_cast_iron")),
		ALLOY_NETHERSTEEL_STEEL = overrideOther(cbcLoc("alloy_nethersteel_steel")),
		ALLOY_STEEL = overrideOther(cbcLoc("alloy_steel"));
	
	private GeneratedRecipe overrideOther(ResourceLocation name) {
		return create(name, b -> b.withCondition(FalseCondition.INSTANCE));
	}

	public TFMGMixingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, registries, TFMG.MOD_ID);
	}

	private ResourceLocation cbcLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath("createbigcannons", path);
	}
}
