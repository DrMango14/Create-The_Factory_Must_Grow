package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.*;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGCompactingRecipeGen extends CompactingRecipeGen {

    GeneratedRecipe
            BITUMEN = create(TFMG.asResource("bitumen"), b -> b
            .require(SizedFluidIngredient.of(heavyOil(),1000))
            .output(bitumen(), 1)
            .requiresHeat(HeatCondition.HEATED)
            ),
            CINDERFLOURBLOCK = create(TFMG.asResource("cinderflourblock"), b -> b
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .output(TFMGItems.CINDERFLOURBLOCK)
            ),
            CAST_IRON = create(TFMG.asResource("cast_iron"), b -> b
                    .require(ironIngot())
                    .require(coal())
                    .output(TFMGItems.CAST_IRON_INGOT, 1)
                    .requiresHeat(HeatCondition.HEATED)
            );

    public TFMGCompactingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

}
