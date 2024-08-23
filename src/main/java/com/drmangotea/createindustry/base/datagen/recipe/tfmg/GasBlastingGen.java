package com.drmangotea.createindustry.base.datagen.recipe.tfmg;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class GasBlastingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    heatedAir = create(CreateTFMG.asResource("heated_air"), b -> b
            .require(F.air(), 1000)
            .require(F.blastFurnaceGas(), 250)
            .output(F.heatedAir(), 1000)
            .output(F.carbonDioxide(), 750)
            .duration(400))
    
    ;
    
    
    public GasBlastingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.GAS_BLASTING;
    }
}
