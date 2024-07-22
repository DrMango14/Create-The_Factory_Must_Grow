package com.drmangotea.createindustry.base.datagen.recipe.tfmg;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class IndustrialBlastingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    steel = create(CreateTFMG.asResource("steel"), b -> b
            .require(I.blastingMixture())
            .output(F.moltenSteel(), 111)
            .output(F.moltenSlag(), 75)
            .duration(200))
    
    ;
    
    
    public IndustrialBlastingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.INDUSTRIAL_BLASTING;
    }
}
