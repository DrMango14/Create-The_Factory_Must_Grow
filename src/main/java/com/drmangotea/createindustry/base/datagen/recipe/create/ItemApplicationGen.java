package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class ItemApplicationGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    heavyMachineryCasing = create(CreateTFMG.asResource("heavy_machinery_casing"), b -> b
            .require(I.steelCasing())
            .require(I.heavyPlate())
            .output(I.heavyMachineryCasing(), 1)),
    
    steelCasing = create(CreateTFMG.asResource("steel_casing"), b -> b
            .require(I.hardenedPlanks())
            .require(IT.steelIngot())
            .output(I.steelCasing(), 1))
    
    ;
    
    public ItemApplicationGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
    
    
}
