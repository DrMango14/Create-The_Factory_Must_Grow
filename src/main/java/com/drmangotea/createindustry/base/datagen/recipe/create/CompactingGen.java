package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class CompactingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    bitumen = create(CreateTFMG.asResource("bitumen"), b -> b
            .require(F.heavyOil(), 10)
            .output(I.bitumen(), 1)),
    
    cinderflourblock = create(CreateTFMG.asResource("cinderflourblock"), b -> b
            .require(I.cinderFlour())
            .require(I.cinderFlour())
            .output(I.cinderflourBlock(), 1)),
    
    plasticMolding = create(CreateTFMG.asResource("plastic_molding"), b -> b
            .require(F.liquidPlastic(), 200)
            .output(I.plasticSheet(), 1)),
    
    steelBlock = create(CreateTFMG.asResource("steel_block"), b -> b
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .require(I.steelIngot())
            .output(I.steelBlock(), 1)),
    
    thermitePowder = create(CreateTFMG.asResource("thermite_powder"), b -> b
            .require(IT.aluminumIngot())
            .require(I.crimsite())
            .output(I.thermitePowder(), 1))
    
    ;
    public CompactingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}
