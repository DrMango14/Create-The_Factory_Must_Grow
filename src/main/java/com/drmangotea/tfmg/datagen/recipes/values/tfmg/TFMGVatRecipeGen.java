package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.pressure.Pressure;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.datagen.recipes.builder.VatRecipeGen;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import rbasamoyai.createbigcannons.index.CBCFluids;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.crushedRawIron;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.rubber;

@SuppressWarnings("unused")
public class TFMGVatRecipeGen extends VatRecipeGen {
    public TFMGVatRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }


    GeneratedRecipe CONCRETE = create("concrete", b -> b
            .require(Tags.Items.SANDS_COLORLESS)
            .require(Blocks.GRAVEL.asItem())
            .require(TFMGItems.LIMESAND)
            .require(Fluids.WATER, 250)
            .output(TFMGFluids.LIQUID_CONCRETE.get(), 32000)
            .allowAllVatTypes()
            .mixing()
    );
	
	GeneratedRecipe[] CONCRETE_DYEING =
		Arrays.stream(DyeColor.values())
		.map(colour -> create("dyeing_" + colour.getSerializedName() + "_concrete", b -> b
			.require(colour.getTag())
			.require(TFMGFluids.LIQUID_CONCRETE.getSource(), 8000)
			.output(TFMGFluids.COLOURED_CONCRETE.getSource(colour), 8000)
			.mixing()
			.duration(100)
			.allowAllVatTypes()
		)).toArray(GeneratedRecipe[]::new);
	
	GeneratedRecipe
    ARC_FURNACE_STEEL = create("arc_furnace_steel", b -> b
            .require(crushedRawIron())
            .require(TFMGTags.Items.FLUX.tag)
            .require(TFMGTags.Items.DUSTS_COAL_COKE.tag)
            .output(0.9f, TFMGItems.COAL_COKE_DUST)
            .output(TFMGFluids.MOLTEN_STEEL.get(), 90)
            .output(TFMGFluids.MOLTEN_SLAG.get(), 160)
            .duration(20)
            .allowFireproof()
            .minSize(9)
            .arcBlasting()
    ),

    NEON = create("neon", b -> b
            .require(TFMGTags.Fluids.AIR.tag, 1000)
            .output(TFMGFluids.NEON.get(), 1)
            .duration(10)
            .centrifuge()
            .allowAllVatTypes()
    ),

    SULFURIC_ACID = create("sulfuric_acid", b -> b
            .require(SizedFluidIngredient.of(water(), 1000))
            .require(TFMGTags.Items.DUSTS_SULFUR.tag)
            .require(TFMGTags.Items.DUSTS_SULFUR.tag)
            .require(TFMGTags.Items.DUSTS_SULFUR.tag)
            .require(TFMGTags.Items.DUSTS_SALTPETER.tag)
            .output(sulfuricAcid(), 500)
            .mixing()
            .duration(5)
            .allowAllVatTypes()
    ),

    RUBBER = create("rubber", b -> b
            .require(SizedFluidIngredient.of(TFMGTags.Fluids.HEAVY_OIL.tag, 250))
            .require(TFMGTags.Items.DUSTS_SULFUR.tag)
            .output(rubber())
            .mixing()
            .allowAllVatTypes()
            .duration(40)
            .heatLevel(2)
    ),

    NAPHTHA = create("naphtha", b -> b
            .require(SizedFluidIngredient.of(TFMGTags.Fluids.NAPHTHA.tag, 500))
            .output(ethylene(), 250)
            .output(propylene(), 250)
            .mixing()
            .duration(20)
            .allowAllVatTypes()
            .heatLevel(2)
    ),

    PLASTIC_FROM_ETHYLENE = create("plastic_from_ethylene", b -> b
            .require(SizedFluidIngredient.of(TFMGTags.Fluids.ETHYLENE.tag, 500))
            .output(liquidPlastic(), 500)
            .mixing()
            .allowAllVatTypes()
            .duration(30)
            .heatLevel(2)
    ),

    PLASTIC_FROM_PROPYLENE = create("plastic_from_propylene", b -> b
            .require(SizedFluidIngredient.of(TFMGTags.Fluids.PROPYLENE.tag, 500))
            .output(liquidPlastic(), 500)
            .mixing()
            .duration(30)
            .allowAllVatTypes()
            .heatLevel(2)
    ),

    ETCHED_CIRCUIT_BOARD = create("etched_circuit_board", b -> b
            .require(TFMGItems.COATED_CIRCUIT_BOARD)
            .require(TFMGFluids.SULFURIC_ACID.getSource(), 250)
            .output(TFMGItems.ETCHED_CIRCUIT_BOARD)
            .duration(100)
            .mixing()
            .allowAllVatTypes()
    ),

    ALUMINUM = create("aluminum", b -> b
            .require(TFMGTags.Items.DUSTS_BAUXITE.tag)
            .require(TFMGTags.Items.DUSTS_BAUXITE.tag)
            .require(TFMGTags.Items.DUSTS_BAUXITE.tag)
            .require(TFMGTags.Items.DUSTS_BAUXITE.tag)
            .output(TFMGItems.ALUMINUM_INGOT)
            .output(.5f, TFMGItems.ALUMINUM_NUGGET, 4)
            .output(.25f, TFMGItems.ALUMINUM_NUGGET, 2)
            .output(TFMGFluids.CARBON_DIOXIDE.get(), 500)
            .duration(100)
            .electrolysis()
            .allowNonCastIron()
            .heatLevel(2)
    ),

    //CBC
    Nethersteel = create("nethersteel", b -> b.whenModLoaded("createbigcannons")
            .require(Items.NETHERITE_SCRAP)
            .require(TFMGTags.Fluids.MOLTEN_STEEL.tag, 360)
            .output(CBCFluids.MOLTEN_NETHERSTEEL.get(), 360)
            .arcBlasting()
            .allowFireproof()
            .heatLevel(2)
    );

    public static class VatRecipeValues {
        public List<VatOperation> machines;
        public int minSize;
        public int heat;
        public Pressure pressure;
        public List<VatType> allowedVatTypes;

        public VatRecipeValues() {
            machines = new ArrayList<>();
            minSize = 1;
            heat = 0;
            pressure = Pressure.EMPTY;
            allowedVatTypes = new ArrayList<>();
            allowedVatTypes.add(new VatType(TFMG.asResource("steel")));
            allowedVatTypes.add(new VatType(TFMG.asResource("cast_iron")));
            allowedVatTypes.add(new VatType(TFMG.asResource("fireproof")));
        }

        public VatRecipeValues heat(int heat) {
            this.heat = heat;
            return this;
        }

        public VatRecipeValues pressure(int kpa) {
            this.pressure = Pressure.of(kpa);
            return this;
        }

    }

    @Override @Nonnull
    public String getName() {
        return "TFMG'S Vat Recipes";
    }
}
