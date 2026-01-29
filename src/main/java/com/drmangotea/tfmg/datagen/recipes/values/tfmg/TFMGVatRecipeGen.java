package com.drmangotea.tfmg.datagen.recipes.values.tfmg;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.VatRecipeGen;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGVatRecipeGen extends VatRecipeGen {
    public TFMGVatRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }


    GeneratedRecipe
            CONCRETE = create("concrete", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
            .require(Blocks.SAND.asItem())
            .require(Blocks.GRAVEL.asItem())
            .require(TFMGItems.LIMESAND)
            .require(Fluids.WATER, 250)
            .output(TFMGFluids.LIQUID_CONCRETE.get(), 32000)
            .values(mixing(true))
    ),
            ARC_FURNACE_STEEL = create("arc_furnace_steel", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    .require(crushedRawIron())
                    .require(TFMGTags.TFMGItemTags.FLUX.tag)
                    .require(TFMGItems.COAL_COKE_DUST)
                    .output(0.9f, TFMGItems.COAL_COKE_DUST)
                    .output(TFMGFluids.MOLTEN_STEEL.get(), 144)
                    .output(TFMGFluids.MOLTEN_SLAG.get(), 288)
                    .duration(20)
                    .values(arcBlasting())),
            NEON = create("neon", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    .require(TFMGFluids.AIR.get(), 1000)
                    .output(TFMGFluids.NEON.get(), 1)
                    .duration(10)
                    .values(centrifuge())),
            SULFURIC_ACID = create("sulfuric_acid", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    .require(SizedFluidIngredient.of(water(), 1000))
                    .require(sulfurDust())
                    .require(sulfurDust())
                    .require(sulfurDust())
                    .require(nitrateDust())
                    .output(sulfuricAcid(), 500)
                    .values(mixing(true))),

    RUBBER = create("rubber", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
            .require(SizedFluidIngredient.of(heavyOil(), 250))
            .require(sulfurDust())
            .output(rubber())
            .values(heatedmixing(true))),

    NAPHTHA = create("naphtha", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
            .require(SizedFluidIngredient.of(naphtha(), 500))
            .output(ethylene(), 250)
            .output(propylene(), 250)
            .values(heatedmixing(true))),

    PLASTIC_FROM_ETHYLENE = create("plastic_from_ethylene", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
            .require(SizedFluidIngredient.of(ethylene(), 500))
            .output(liquidPlastic(), 500)
            .values(heatedmixing(true))),

            PLASTIC_FROM_PROPYLENE = create("plastic_from_propylene", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    .require(SizedFluidIngredient.of(propylene(), 500))
                    .output(liquidPlastic(), 500)
                    .values(heatedmixing(true))),
            ETCHED_CIRCUIT_BOARD = create("etched_circuit_board", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    .require(TFMGItems.COATED_CIRCUIT_BOARD)
                    .require(TFMGFluids.SULFURIC_ACID.getSource(), 250)
                    .output(TFMGItems.ETCHED_CIRCUIT_BOARD)
                    .duration(100)
                    .values(noMachines())),
            ALUMINUM = create("aluminum", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    .require(TFMGItems.BAUXITE_POWDER)
                    .require(TFMGItems.BAUXITE_POWDER)
                    .require(TFMGItems.BAUXITE_POWDER)
                    .require(TFMGItems.BAUXITE_POWDER)
                    .output(TFMGItems.ALUMINUM_INGOT)
                    .output(.5f, TFMGItems.ALUMINUM_NUGGET, 4)
                    .output(.25f, TFMGItems.ALUMINUM_NUGGET, 2)
                    .output(TFMGFluids.CARBON_DIOXIDE.get(), 500)
                    .duration(100)
                    .values(electrolysis()))


                    //DEBUG = createVatRecipe("debug_5", b -> ((VatMachineRecipe.Builder<VatMachineRecipe>) b)
                    //                .require(Blocks.GOLD_BLOCK.asItem())
                    //                .require(Blocks.DIAMOND_BLOCK.asItem())
                    //                .require(Blocks.IRON_BLOCK.asItem())
                    //                .require(Blocks.COAL_BLOCK.asItem())
                    //                .require(TFMGFluids.LIQUID_CONCRETE.getSource(), 1)
                    //                .require(TFMGFluids.HEAVY_OIL.getSource(), 1)
                    //                .require(TFMGFluids.COOLING_FLUID.getSource(), 1)
                    //                .require(TFMGFluids.CRUDE_OIL.getSource(), 1)
                    //                .output(TFMGFluids.LIQUID_CONCRETE.get(), 1)
                    //                .output(TFMGFluids.HEAVY_OIL.get(), 1)
                    //                .output(TFMGFluids.COOLING_FLUID.get(), 1)
                    //                .output(TFMGFluids.CRUDE_OIL.get(), 1)
                    //                .output(Items.EGG)
                    //                .output(Items.ARROW)
                    //                .output(Items.DIAMOND)
                    //                .output(Items.STRING)
                    //        , mixing());
                    ;

    /// ////
    public VatRecipeValues electrolysis() {
        VatRecipeValues params = new VatRecipeValues();
        params.machines.add("tfmg:electrode");
        params.machines.add("tfmg:electrode");
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        params.heat = 2;
        return params;
    }

    public VatRecipeValues mixing() {
        return mixing(false);
    }

    public VatRecipeValues mixing(boolean allowsCastIronVat) {
        VatRecipeValues params = new VatRecipeValues();
        params.machines.add("tfmg:mixing");
        params.allowedVatTypes = new ArrayList<>();
        if (allowsCastIronVat)
            params.allowedVatTypes.add("tfmg:cast_iron_vat");
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public VatRecipeValues heatedmixing(boolean allowsCastIronVat) {
        VatRecipeValues params = new VatRecipeValues();
        params.machines.add("tfmg:mixing");
        params.allowedVatTypes = new ArrayList<>();
        if (allowsCastIronVat)
            params.allowedVatTypes.add("tfmg:cast_iron_vat");
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        params.heat = 2;
        return params;
    }

    public VatRecipeValues centrifuge() {
        VatRecipeValues params = new VatRecipeValues();
        params.machines.add("tfmg:centrifuge");
        return params;
    }

    public VatRecipeValues noMachines() {
        VatRecipeValues params = new VatRecipeValues();
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:cast_iron_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        params.minSize = 1;
        return params;
    }

    public VatRecipeValues freezing() {
        VatRecipeValues params = new VatRecipeValues();

        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:cast_iron_vat");
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        params.heat = 5;
        return params;
    }

    public VatRecipeValues intenseFreezing() {
        VatRecipeValues params = new VatRecipeValues();
        params.machines.add("tfmg:freezing");
        params.machines.add("tfmg:freezing");
        params.machines.add("tfmg:freezing");
        return params;
    }

    public VatRecipeValues arcBlasting() {
        VatRecipeValues params = new VatRecipeValues();
        params.machines.add("tfmg:graphite_electrode");
        params.machines.add("tfmg:graphite_electrode");
        params.machines.add("tfmg:graphite_electrode");
        params.minSize = 9;
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public static class VatRecipeValues {

        public List<String> machines;
        public int minSize;
        public int heat;
        public int pressure;
        public List<String> allowedVatTypes;


        public VatRecipeValues() {
            machines = new ArrayList<>();
            minSize = 1;
            heat = 0;
            pressure = 0;
            allowedVatTypes = new ArrayList<>();
            allowedVatTypes.add("tfmg:steel_vat");
            allowedVatTypes.add("tfmg:cast_iron_vat");
            allowedVatTypes.add("tfmg:firebrick_lined_vat");

        }

        public VatRecipeValues heat(int heat) {
            VatRecipeValues values = this;
            values.heat = heat;
            return values;
        }

    }
}
