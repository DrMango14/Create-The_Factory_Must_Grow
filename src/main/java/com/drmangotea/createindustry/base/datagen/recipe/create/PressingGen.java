package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGRecipeProvider;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class PressingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    syntheticLeather = create(CreateTFMG.asResource("synthetic_leather"), b -> b
            .require(I.plasticSheet())
            .output(I.syntheticLeather(), 1))
    
    ;
    
    
    public PressingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
