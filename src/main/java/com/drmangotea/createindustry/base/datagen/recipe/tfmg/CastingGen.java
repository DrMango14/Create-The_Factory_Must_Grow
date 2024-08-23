package com.drmangotea.createindustry.base.datagen.recipe.tfmg;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class CastingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    steel = create(CreateTFMG.asResource("steel"), b -> b
            .require(F.moltenSteel(), 1)
            .output(I.steelIngot(), 1)
            .output(I.steelBlock(), 1)
            .duration(300))
    
    ;
    
    public CastingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.CASTING;
    }
}
