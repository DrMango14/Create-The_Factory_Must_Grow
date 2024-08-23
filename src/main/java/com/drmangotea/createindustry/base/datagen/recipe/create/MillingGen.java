package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MillingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    //charcoalDust = create(CreateTFMG.asResource("charcoal_dust"), b -> b
    //        .require(I.charcoal())
    //        .output(I.charcoalDust(), 1)
    //        .duration(130)),
    
    limesand = create(CreateTFMG.asResource("limesand"), b -> b
            .require(I.limestone())
            .output(I.limesand(), 1)
            .duration(130))
    
    ;
    public MillingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MILLING;
    }
}
