package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.aluminumIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.aluminumSheetTFMG;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.castIronIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.castIronSheetTFMG;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.leadIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.leadSheetTFMG;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.nickelIngot;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.nickelSheetTFMG;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.rubber;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.syntheticLeather;

public class TFMGPressingRecipeGen extends TFMGProcessingRecipeGen {

    GeneratedRecipe

    CAST_IRON_SHEET = create("cast_iron_ingot", b -> b.require(castIronIngot())
            .output(castIronSheetTFMG()).duration(50)),

    ALUMINUM_SHEET = create("aluminum_ingot", b -> b.require(aluminumIngot())
            .output(aluminumSheetTFMG()).duration(50)),

    LEAD_SHEET = create("lead_ingot", b -> b.require(leadIngot())
            .output(leadSheetTFMG()).duration(50)),

    NICKEL_SHEET = create("nickel_ingot", b -> b.require(nickelIngot())
            .output(nickelSheetTFMG()).duration(50)),

    SYNTHETIC_LEATHER = create("synthetic_leather", b -> b
            .require(rubber())
            .output(syntheticLeather())
            .duration(200));

    public TFMGPressingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }

}