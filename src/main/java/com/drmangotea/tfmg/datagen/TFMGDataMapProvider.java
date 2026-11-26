package com.drmangotea.tfmg.datagen;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class TFMGDataMapProvider extends DataMapProvider {
    protected TFMGDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    public void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(TFMG.asResource("fossilstone"), new FurnaceFuel(4000), false)
                .add(TFMG.asResource("coal_coke_block"), new FurnaceFuel(28800), false)
                .add(TFMGItems.COAL_COKE, new FurnaceFuel(3200), false)
                .add(TFMGItems.COAL_COKE_DUST, new FurnaceFuel(3200), false);
    }
}
