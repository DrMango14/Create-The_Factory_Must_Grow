package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MixingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    blastingMixture = create(CreateTFMG.asResource("blasting_mixture"), b -> b
            .require(I.limesand())
            .require(I.crushedRawIron())
            .require(I.crushedRawIron())
            .require(I.crushedRawIron())
            .output(I.blastingMixture(), 3)),
    
    castIronIngot = create(CreateTFMG.asResource("cast_iron_ingot"), b -> b
            .require(I.ironIngot())
            .require(I.coal())
            .output(I.castIronIngot(), 1)
            .requiresHeat(HeatCondition.HEATED)
            .duration(100)),
    
    cement = create(CreateTFMG.asResource("cement"), b -> b
            .require(I.limesand())
            .require(I.clayBall())
            .output(I.cement(), 4)),
    
    concreteMixture = create(CreateTFMG.asResource("concrete_mixture"), b -> b
            .require(I.sand())
            .require(I.gravel())
            .require(I.cement())
            .output(I.concreteMixture(), 16)),
    
    concreteMixtureFromSlag = create(CreateTFMG.asResource("concrete_mixture_from_slag"), b -> b
            .require(I.slag())
            .require(I.gravel())
            .require(I.cement())
            .output(I.concreteMixture(), 32)),
    
    coolingFluid = create(CreateTFMG.asResource("cooling_fluid"), b -> b
            .require(F.ethylene(), 250)
            .require(F.water(), 250)
            .output(F.coolingFluid(), 500)),
    
    copperSulfate = create(CreateTFMG.asResource("copper_sulfate"), b -> b
            .require(F.sulfuricAcid(), 500)
            .require(IT.copperIngot())
            .output(I.copperSulfate(), 1)),
    
    gunPowder = create(CreateTFMG.asResource("gun_powder"), b -> b
            .require(I.nitrateDust())
            .require(I.nitrateDust())
            .require(I.nitrateDust())
            .require(I.charcoal())
            .require(I.charcoal())
            .require(I.sulfurDust())
            .output(I.gunpowder(), 6)),
    
    liquidAsphalt = create(CreateTFMG.asResource("liquid_asphalt"), b -> b
            .require(I.bitumen())
            .require(I.sand())
            .require(I.gravel())
            .require(F.water(), 500)
            .output(F.liquidAsphalt(), 1200)),
    
    liquidConcrete = create(CreateTFMG.asResource("liquid_concrete"), b -> b
            .require(I.concreteMixture())
            .require(F.water(), 250)
            .output(F.liquidConcrete(), 1000)),
    
    liquidPlasticFromEthylene = create(CreateTFMG.asResource("liquid_plastic_from_ethylene"), b -> b
            .require(F.ethylene(), 500)
            .output(F.liquidPlastic(), 500)),
    
    liquidPlasticFromPropylene = create(CreateTFMG.asResource("liquid_plastic_from_propylene"), b -> b
            .require(F.propylene(), 500)
            .output(F.liquidPlastic(), 500)),
    
    napalm = create(CreateTFMG.asResource("napalm"), b -> b
            .require(IT.aluminumIngot())
            .require(F.gasoline(), 1000)
            .output(F.napalm(), 1000)
            .duration(1000)),
    
    neon = create(CreateTFMG.asResource("neon"), b -> b
            .require(F.air(), 250)
            .output(F.neon(), 1)),
    
    slag = create(CreateTFMG.asResource("slag"), b -> b
            .require(F.moltenSlag(), 1000)
            .output(I.slag(), 9)),
    
    sulfuricAcid = create(CreateTFMG.asResource("sulfuric_acid"), b -> b
            .require(I.sulfurDust())
            .require(I.nitrateDust())
            .require(F.water(), 500)
            .output(F.sulfuricAcid(), 500)),
    
    zincSulfate = create(CreateTFMG.asResource("zinc_sulfate"), b -> b
            .require(F.sulfuricAcid(), 500)
            .require(IT.zincIngot())
            .output(I.zincSulfate(), 1)),
    
    heatedAirTank = create(CreateTFMG.asResource("heated_air_tank"), b -> b
            .require(F.airTank())
            .output(F.heatedAirTank(), 1)
            .requiresHeat(HeatCondition.SUPERHEATED)
            .duration(1000))
    ;
    
    public MixingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
