package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGPressingRecipeGen extends PressingRecipeGen {

    GeneratedRecipe

            CAST_IRON_SHEET = create(TFMG.asResource("cast_iron_ingot"), b -> b.require(castIronIngot())
            .output(castIronSheetTFMG())),

    ALUMINUM_SHEET = create(TFMG.asResource("aluminum_ingot"), b -> b.require(aluminumIngot())
            .output(aluminumSheetTFMG())),

    LEAD_SHEET = create(TFMG.asResource("lead_ingot"), b -> b.require(leadIngot())
            .output(leadSheetTFMG())),

    NICKEL_SHEET = create(TFMG.asResource("nickel_ingot"), b -> b.require(nickelIngot())
            .output(nickelSheetTFMG())),

    SYNTHETIC_LEATHER = create(TFMG.asResource("synthetic_leather"), b -> b
            .require(rubber())
            .output(syntheticLeather()));

    public TFMGPressingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries,TFMG.MOD_ID);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }

}