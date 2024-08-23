package com.drmangotea.createindustry.base.datagen.recipe.tfmg;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class CokingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    charcoal = create(CreateTFMG.asResource("charcoal"), b -> b
            .require(I.coal())
            .output(I.charcoal(), 1)
            .output(F.creosote(), 1)
            .duration(400)),
    
    coalCoke = create(CreateTFMG.asResource("coal_coke"), b -> b
            .require(I.coal())
            .output(I.coalCoke(), 1)
            .output(F.creosote(), 1)
            .duration(1000))
    
    ;
    
    public CokingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.COKING;
    }
}
