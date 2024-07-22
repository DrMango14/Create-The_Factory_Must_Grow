package com.drmangotea.createindustry.base.datagen.recipe.tfmg;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class DistillationGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    crudeOil = create(CreateTFMG.asResource("crude_oil"), b -> b
            .require(F.crudeOil(), 360)
            .output(F.heavyOil(), 80)
            .output(F.diesel(), 60)
            .output(F.kerosene(), 40)
            .output(F.naphtha(), 40)
            .output(F.gasoline(), 80)
            .output(F.lpg(), 60)),
    
    crudeOilNoNaphtha = create(CreateTFMG.asResource("crude_oil_no_naphtha"), b -> b
            .require(F.crudeOil(), 340)
            .output(F.heavyOil(), 80)
            .output(F.diesel(), 60)
            .output(F.kerosene(), 40)
            .output(F.gasoline(), 80)
            .output(F.lpg(), 60)),
    
    heavyOil = create(CreateTFMG.asResource("heavy_oil"), b -> b
            .require(F.heavyOil(), 150)
            .output(F.diesel(), 100)
            .output(F.lubricationOil(), 50)),
    
    naphtha = create(CreateTFMG.asResource("naphtha"), b -> b
            .require(F.naphtha(), 100)
            .output(F.ethylene(), 50)
            .output(F.propylene(), 50))
    ;
    
    public DistillationGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.DISTILLATION;
    }
}
