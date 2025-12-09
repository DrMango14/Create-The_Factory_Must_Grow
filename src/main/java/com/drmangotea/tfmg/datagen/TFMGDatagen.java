package com.drmangotea.tfmg.datagen;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrateTags;
import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider;
import com.drmangotea.tfmg.datagen.recipes.values.TFMGStandardRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.create.TFMGMechanicalCraftingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.create.TFMGSequencedAssemblyRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.CastingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.IndustrialBlastingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.VatRecipeGen;
import com.drmangotea.tfmg.ponder.TFMGPonderPlugin;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGDatagen {
    protected static final List<TFMGRecipeProvider> RECIPE_GENERATORS = new ArrayList<>();
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            TFMGGeneratedEntriesProvider generatedEntriesProvider = new TFMGGeneratedEntriesProvider(output, lookupProvider);
            //lookupProvider = generatedEntriesProvider.getRegistryProvider();
            generator.addProvider(true, generatedEntriesProvider);

          //  generator.addProvider(true, new DamageTypeTagGen(output, lookupProvider, existingFileHelper));

            RECIPE_GENERATORS.add(new IndustrialBlastingRecipeGen(output));
            RECIPE_GENERATORS.add(new CastingRecipeGen(output));
            RECIPE_GENERATORS.add(new VatRecipeGen(output));
            RECIPE_GENERATORS.add(new TFMGStandardRecipeGen(output));
            RECIPE_GENERATORS.add(new TFMGMechanicalCraftingRecipeGen(output));
            //generator.addProvider(event.includeServer(), new TFMGMechanicalCraftingRecipeGen(output));
            generator.addProvider(true, new TFMGSequencedAssemblyRecipeGen(output));

            generator.addProvider(true, new DataProvider() {

                @Override
                public String getName() {
                    return "TFMG's Recipes";
                }

                @Override
                public CompletableFuture<?> run(CachedOutput dc) {
                    return CompletableFuture.allOf(RECIPE_GENERATORS.stream()
                            .map(gen -> gen.run(dc))
                            .toArray(CompletableFuture[]::new));
                }
            });

            //generator.addProvider(true, new IndustrialBlastingRecipeGen(output));
            //generator.addProvider(true, new CastingRecipeGen(output));
            //generator.addProvider(true, new VatRecipeGen(output));
            //generator.addProvider(true, new TFMGStandardRecipeGen(output));
            //generator.addProvider(true, new TFMGSequencedAssemblyRecipeGen(output));
            TFMGProcessingRecipeGen.registerAll(generator, output);
        }

    }

    private static void addExtraRegistrateData() {
        TFMGRegistrateTags.addGenerators();

        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);

            providePonderLang(langConsumer);
        });
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new TFMGPonderPlugin());

        PonderIndex.getLangAccess().provideLang(TFMG.MOD_ID, consumer);
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/tfmg/lang/default/" + fileName + ".json";
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
