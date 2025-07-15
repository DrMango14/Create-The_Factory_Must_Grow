package com.drmangotea.tfmg.datagen;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.worldgen.TFMGBiomeModifiers;
import com.drmangotea.tfmg.worldgen.TFMGConfiguredFeatures;
import com.drmangotea.tfmg.worldgen.TFMGPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TFMGGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, (RegistrySetBuilder.RegistryBootstrap) TFMGConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, TFMGPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TFMGBiomeModifiers::bootstrap)
            .add(Registries.DAMAGE_TYPE, TFMGDamageTypes::bootstrap);


    public TFMGGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TFMG.MOD_ID));

    }

    @Override
    public String getName() {
        return "TFMG's Generated Registry Entries";
    }
}
