package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class CrushingGen extends TFMGProcessingRecipeGen {
    
    GeneratedRecipe
    
    bauxiteRecycling = create(CreateTFMG.asResource("bauxite_recycling"), b -> b
            .require(IT.bauxiteStoneType())
            .output(I.crushedRawAluminum(), 1)
            .output(0.25f, I.crushedRawAluminum(), 1)
            .output(0.15f, I.experienceNugget(), 1)
            .duration(250)),
    
    coalCokeDust = create(CreateTFMG.asResource("coal_coke_dust"), b -> b
            .require(I.coalCoke())
            .output(I.coalCokeDust(), 1)
            .duration(250)),
    
    copperSulfate = create(CreateTFMG.asResource("copper_sulfate"), b -> b
            .require(I.copperSulfate())
            .output(I.boneMeal(), 4)
            .output(0.5f, I.boneMeal(), 3)
            .output(0.5f, I.blueDye(), 1)
            .output(0.3f, I.cyanDye(), 1)
            .duration(100)),
    
    galenaRecycling = create(CreateTFMG.asResource("galena_recycling"), b -> b
            .require(IT.galenaStoneType())
            .output(I.crushedRawLead(), 1)
            .output(0.15f, I.experienceNugget(), 1)
            .duration(250)),
    
    lignite = create(CreateTFMG.asResource("lignite"), b -> b
            .require(I.lignite())
            .output(I.coal(), 1)
            .duration(250)),
    
    limesand = create(CreateTFMG.asResource("limesand"), b -> b
            .require(I.limestone())
            .output(I.limesand(), 1)
            .duration(100)),
    
    saltpeter = create(CreateTFMG.asResource("saltpeter"), b -> b
            .require(I.dirt())
            .output(0.2f, I.nitrateDust(), 1)
            .duration(350)),
    
    sulfur = create(CreateTFMG.asResource("sulfur"), b -> b
            .require(I.sulfur())
            .output(I.sulfurDust(), 1)
            .output(0.1f, I.sulfurDust(), 1)
            .duration(250))
            ;
    
    public CrushingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }
    
}
