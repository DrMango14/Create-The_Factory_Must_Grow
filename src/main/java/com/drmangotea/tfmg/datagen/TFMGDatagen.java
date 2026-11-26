package com.drmangotea.tfmg.datagen;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.datagen.integration.*;
import com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider;
import com.drmangotea.tfmg.datagen.recipes.values.TFMGStandardRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.create.TFMGMechanicalCraftingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.create.TFMGSequencedAssemblyRecipeGen;
import com.drmangotea.tfmg.datagen.tags.TFMGEngineFuelTags;
import com.drmangotea.tfmg.datagen.tags.TFMGFlamethrowerFuelTags;
import com.drmangotea.tfmg.datagen.tags.TFMGRegistrateTags;
import com.drmangotea.tfmg.ponder.TFMGPonderPlugin;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.compat.Mods;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGDatagen {
    public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<EngineType>> ENGINE_TAGS = ProviderType.registerIntrinsicTag("tags/engine_type", "engine_type", TFMGRegistries.ENGINE_TYPE, engineType -> engineType.builtInRegistryHolder().getKey());
    public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<EngineUpgrade>> ENGINE_UPGRADE_TAGS = ProviderType.registerIntrinsicTag("tags/engine_upgrade", "engine_upgrade", TFMGRegistries.ENGINE_UPGRADE, engineUpgrade -> engineUpgrade.builtInRegistryHolder().getKey());

    //public static final ProviderType<RegistrateTagsProvider.Impl<EngineFuelType>> ENGINE_FUEL_TAGS = ProviderType.registerDynamicTag("tags/engine_fuel_type", "tfmg/fuel_type/engine", TFMGRegistries.ENGINE_FUEL_TYPE);

    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(TFMG.MOD_ID))
            addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

        TFMGGeneratedEntriesProvider generatedEntriesProvider = new TFMGGeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);

        generator.addProvider(event.includeServer(), new TFMGEngineFuelTags(output, lookupProvider, fileHelper));
        generator.addProvider(event.includeServer(), new TFMGFlamethrowerFuelTags(output, lookupProvider, fileHelper));

        generator.addProvider(event.includeServer(), new TFMGDataMapProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new TFMGStandardRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new TFMGMechanicalCraftingRecipeGen(output, lookupProvider));
        generator.addProvider(event.includeServer(), new TFMGSequencedAssemblyRecipeGen(output, lookupProvider));

        // Maybe use ModList instead?
        if (LoadingModList.get().getModFileById("createliquidfuel") != null) {
            generator.addProvider(event.includeServer(), new TFMGReburnedProvider(output));
        }
        if (LoadingModList.get().getModFileById("rutile") != null) {
            generator.addProvider(event.includeServer(), new TFMGRutileProvider.Item(output, lookupProvider));
            generator.addProvider(event.includeServer(), new TFMGRutileProvider.Fluid(output, lookupProvider));
        }
        if (LoadingModList.get().getModFileById("createbigcannons") != null) {
            generator.addProvider(event.includeServer(), new TFMGBigCannonsProvider.Melting(output, lookupProvider));
        }
		if (LoadingModList.get().getModFileById("sable") != null) {
			generator.addProvider(event.includeServer(), new TFMGSableProvider(output, lookupProvider));
		}
		if (Mods.CURIOS.isLoaded()) {
			generator.addProvider(event.includeServer(), new TFMGCuriosProvider(output, lookupProvider, fileHelper));
		}

        if (event.includeServer()) {
            TFMGRecipeProvider.registerAllProcessing(generator, output, lookupProvider);
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
