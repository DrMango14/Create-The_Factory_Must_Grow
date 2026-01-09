package com.drmangotea.tfmg.datagen.recipes.values.tfmg;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.WindingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;


public class TFMGWindingRecipeGen extends WindingRecipeGen {

    GeneratedRecipe


            ELECTROMAGNETIC_COIL = create("electromagnetic_coil", b -> b
            .require(TFMGItems.UNFINISHED_ELECTROMAGNETIC_COIL)
            .require(TFMGItems.COPPER_SPOOL)
            .output(coil100Turns())
            .duration(100)),
            LARGE_COIL = create("large_coil", b -> b
                    .require(TFMGBlocks.LAMINATED_MAGNETIC_ALLOY_BLOCK)
                    .require(TFMGItems.COPPER_SPOOL)
                    .output(largeCoil100Turns())
                    .duration(100)),
            RESISTOR = create("resistor", b -> b
                    .require(TFMGItems.UNFINISHED_RESISTOR)
                    .require(TFMGItems.CONSTANTAN_SPOOL)
                    .output(resistor10Ohms())
                    .duration(50));

    public TFMGWindingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }


}