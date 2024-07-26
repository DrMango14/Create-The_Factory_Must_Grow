package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class FillingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    airTank = create(CreateTFMG.asResource("air_tank"), b -> b
            .require(I.bucket())
            .require(F.air(), 1000)
            .output(F.airTank(), 1)),
    
    bottleOfBatteryAcid = create(CreateTFMG.asResource("bottle_of_battery_acid"), b -> b
            .require(I.bottle())
            .require(F.sulfuricAcid(), 250)
            .output(I.bottleOfBatteryAcid(), 1)),
    
    bottleOfConcrete = create(CreateTFMG.asResource("bottle_of_concrete"), b -> b
            .require(I.bottle())
            .require(F.liquidConcrete(), 250)
            .output(I.bottleOfConcrete(), 1)),
    
    butaneTank = create(CreateTFMG.asResource("butane_tank"), b -> b
            .require(I.bucket())
            .require(F.butane(), 1000)
            .output(F.butaneTank(), 1)),
    
    carbonDioxideTank = create(CreateTFMG.asResource("carbon_dioxide_tank"), b -> b
            .require(I.bucket())
            .require(F.carbonDioxide(), 1000)
            .output(F.carbonDioxideTank(), 1)),
    
    ethyleneTank = create(CreateTFMG.asResource("ethylene_tank"), b -> b
            .require(I.bucket())
            .require(F.ethylene(), 1000)
            .output(F.ethyleneTank(), 1)),
    
    hardenedWoodCreosote = create(CreateTFMG.asResource("hardened_wood_creosote"), b -> b
            .require(IT.planks())
            .require(F.creosote(), 200)
            .output(I.hardenedPlanks(), 1)),
    
    lpgTank = create(CreateTFMG.asResource("lpg_tank"), b -> b
            .require(I.bucket())
            .require(F.lpg(), 1000)
            .output(F.lpgTank(), 1)),
    
    napalmPotato = create(CreateTFMG.asResource("napalm_potato"), b -> b
            .require(I.potato())
            .require(F.napalm(), 250)
            .output(I.napalmPotato(), 1)),
    
    neonTank = create(CreateTFMG.asResource("neon_tank"), b -> b
            .require(I.bucket())
            .require(F.neon(), 1000)
            .output(F.neonTank(), 1)),
    
    propaneTank = create(CreateTFMG.asResource("propane_tank"), b -> b
            .require(I.bucket())
            .require(F.propane(), 1000)
            .output(F.propaneTank(), 1)),
    
    propyleneTank = create(CreateTFMG.asResource("propylene_tank"), b -> b
            .require(I.bucket())
            .require(F.propylene(), 1000)
            .output(F.propyleneTank(), 1))
    
    ;
    
    public FillingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}
