package com.drmangotea.createindustry.registry;

import net.fabricmc.fabric.api.registry.FuelRegistry;

public class TFMGFuels {
    private FuelRegistry registry = FuelRegistry.INSTANCE;

    public void register() {
        registry.add(TFMGItems.COAL_COKE.get(), 3200);
        registry.add(TFMGItems.COAL_COKE.get(), 3200);
    }
}
