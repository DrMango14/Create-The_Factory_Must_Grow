package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.goldSheet;

public class TFMGDeployingRecipeGen extends TFMGProcessingRecipeGen {

    GeneratedRecipe
            COATED_CIRCUIT_BOARD = create("coated_circuit_board", b -> b
            .require(TFMGItems.EMPTY_CIRCUIT_BOARD)
            .require(goldSheet())
            .output(TFMGItems.COATED_CIRCUIT_BOARD))

            ;

    public TFMGDeployingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }
}
