package com.drmangotea.tfmg.datagen.recipes.values.tfmg;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.IndustrialBlastingRecipeGen;
import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.AllItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.ironDust;


public class TFMGIndustrialBlastingRecipeGen extends IndustrialBlastingRecipeGen {


    GeneratedRecipe

    SILICON = create("silicon", b -> ((IndustrialBlastingRecipe.Builder<IndustrialBlastingRecipe>)b)
            .require(Items.QUARTZ)
            .output(TFMGFluids.LIQUID_SILICON.get(),40)
            .duration(5)
    ),

    STEEL = create("steel", b -> ((IndustrialBlastingRecipe.Builder<IndustrialBlastingRecipe>)b)
            .require(AllItems.CRUSHED_IRON)
            .require(TFMGTags.TFMGItemTags.FLUX.tag)
            .output(TFMGFluids.MOLTEN_STEEL.get(),144)
            .output(TFMGFluids.MOLTEN_SLAG.get(),144)
            .output(TFMGFluids.FURNACE_GAS.get(),200)
            .duration(20)
            .hotAirUsage(20)
    ),
    STEEL_DOUBLE = create("steel_from_raw_iron", b -> ((IndustrialBlastingRecipe.Builder<IndustrialBlastingRecipe>)b)
            .require(Items.RAW_IRON)
            .require(Ingredient.of(TFMGTags.TFMGItemTags.FLUX.tag))
            .output(TFMGFluids.MOLTEN_STEEL.get(),288)
            .output(TFMGFluids.MOLTEN_SLAG.get(),288)
            .output(TFMGFluids.FURNACE_GAS.get(),200)
            .duration(40)
            .hotAirUsage(40)
    ),


    STEEL_DUST = create("steel_from_dust", b -> ((IndustrialBlastingRecipe.Builder<IndustrialBlastingRecipe>)b)
            .require(ironDust())
            .require(TFMGTags.TFMGItemTags.FLUX.tag)
            .output(TFMGFluids.MOLTEN_STEEL.get(),144)
            .output(TFMGFluids.MOLTEN_SLAG.get(),144)
            .output(TFMGFluids.FURNACE_GAS.get(),20)
            .duration(20)
            .hotAirUsage(20)
    )



    ;


    public TFMGIndustrialBlastingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }




}