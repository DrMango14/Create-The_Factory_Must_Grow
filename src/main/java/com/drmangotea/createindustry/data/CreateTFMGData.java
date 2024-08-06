package com.drmangotea.createindustry.data;

import com.drmangotea.createindustry.base.TFMGLangPartials;
import com.simibubi.create.foundation.data.LangMerger;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import static com.drmangotea.createindustry.CreateTFMG.*;

public class CreateTFMGData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(true, new LangMerger(generator, MOD_ID, NAME, TFMGLangPartials.values()));
    }
}
