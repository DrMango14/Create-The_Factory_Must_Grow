package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.DeployingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.goldSheet;

public class TFMGDeployingRecipeGen extends DeployingRecipeGen {

    GeneratedRecipe
            COATED_CIRCUIT_BOARD = create("coated_circuit_board", b -> b
            .require(TFMGItems.EMPTY_CIRCUIT_BOARD)
            .require(goldSheet())
            .output(TFMGItems.COATED_CIRCUIT_BOARD))

            ;

    public TFMGDeployingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, TFMG.MOD_ID);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }
}
