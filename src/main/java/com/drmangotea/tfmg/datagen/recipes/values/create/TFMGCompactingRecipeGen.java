package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.data.PackOutput;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGCompactingRecipeGen extends TFMGProcessingRecipeGen {

    GeneratedRecipe
            BITUMEN = create("bitumen", b -> b
            .require(heavyOil(),1000)
            .output(bitumen(), 1)
            .requiresHeat(HeatCondition.HEATED)
            ),
            CINDERFLOURBLOCK = create("cinderflourblock", b -> b
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .output(TFMGItems.CINDERFLOURBLOCK)
            ),
            CAST_IRON = create("cast_iron", b -> b
                    .require(ironIngot())
                    .require(coal())
                    .output(TFMGItems.CAST_IRON_INGOT, 1)
                    .requiresHeat(HeatCondition.HEATED)
            );

    public TFMGCompactingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

}
