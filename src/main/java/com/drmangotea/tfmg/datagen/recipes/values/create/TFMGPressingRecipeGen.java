package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGPressingRecipeGen extends TFMGProcessingRecipeGen {

    GeneratedRecipe

    CAST_IRON_SHEET = create("cast_iron_ingot", b -> b.require(castIronIngot())
            .output(castIronSheetTFMG())),

    ALUMINUM_SHEET = create("aluminum_ingot", b -> b.require(aluminumIngot())
            .output(aluminumSheetTFMG())),

    LEAD_SHEET = create("lead_ingot", b -> b.require(leadIngot())
            .output(leadSheetTFMG())),

    NICKEL_SHEET = create("nickel_ingot", b -> b.require(nickelIngot())
            .output(nickelSheetTFMG())),

    SYNTHETIC_LEATHER = create("synthetic_leather", b -> b
            .require(rubber())
            .output(syntheticLeather()));

    public TFMGPressingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }

}