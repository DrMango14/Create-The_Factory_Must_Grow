package com.drmangotea.createindustry.registry;

import net.fabricmc.fabric.api.registry.FuelRegistry;

public class TFMGFuels {
    private FuelRegistry registry = FuelRegistry.INSTANCE;

    public void register() {
        registry.add(TFMGItems.COAL_COKE.get(), 3200);
        registry.add(TFMGItems.COAL_COKE.get(), 3200);
        registry.add(TFMGBlocks.COAL_COKE_BLOCK.get().asItem(), 28800);
        registry.add(TFMGBlocks.FOSSILSTONE.get().asItem(), 4000);
    }
}
