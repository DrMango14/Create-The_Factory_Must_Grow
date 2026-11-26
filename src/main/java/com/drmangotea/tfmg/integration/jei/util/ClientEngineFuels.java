package com.drmangotea.tfmg.integration.jei.util;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.registry.TFMGEngineFuelTypes;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientEngineFuels {

    public static final ClientEngineFuels INSTANCE = new ClientEngineFuels();

    protected final Map<ResourceLocation, EngineFuelType> FUELS = new HashMap<>();

    public Map<ResourceLocation, EngineFuelType> getFuels() {
        return new HashMap<>(FUELS);
    }

    public void clear() {
        FUELS.clear();
    }

    public void populate(List<Holder<EngineFuelType>> fuels) {
        TFMG.LOGGER.info("Synchronizing Engine Fuels...");
        TFMG.LOGGER.debug("Loaded Fuels:");
        for (Holder<EngineFuelType> fuel : fuels) {
            ResourceLocation key = fuel.unwrapKey().orElseThrow().location();
            if (key == TFMGEngineFuelTypes.FALLBACK.location()) {
                continue;
            }
            FUELS.put(key, fuel.value());
            TFMG.LOGGER.debug("- {}", key);
        }
        String exclamation = FUELS.isEmpty() ? ":C" : "!";
        TFMG.LOGGER.info("Synchronized {} Engine Fuels{}", FUELS.size(), exclamation);
    }
}
