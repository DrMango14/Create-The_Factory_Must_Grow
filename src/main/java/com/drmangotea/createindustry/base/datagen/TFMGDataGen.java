package com.drmangotea.createindustry.base.datagen;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGProcessingRecipeGen;
import com.drmangotea.createindustry.base.datagen.recipe.create.MechanicalCraftingGen;
import com.drmangotea.createindustry.base.datagen.recipe.create.SequencedAssemblyGen;
import com.drmangotea.createindustry.base.datagen.recipe.vanilla.TFMGStandardRecipeGen;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class TFMGDataGen {
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();
        
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        
        boolean client = event.includeClient();
        boolean server = event.includeServer();
        
        if (server) {
            //generator.addProvider(true, new MStandardRecipeGen(generator));
            TFMGProcessingRecipeGen.registerAll(generator);
            generator.addProvider(true, new SequencedAssemblyGen(generator));
            generator.addProvider(true, new MechanicalCraftingGen(generator));
            generator.addProvider(true, new TFMGStandardRecipeGen(generator));
        }
    }
    
    
    private static void addExtraRegistrateData() {
        TFMGRegistrateTags.addGenerators();
        
        CreateTFMG.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            
            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("ponders", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
        });
    }
    
    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/createindustry/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
}
