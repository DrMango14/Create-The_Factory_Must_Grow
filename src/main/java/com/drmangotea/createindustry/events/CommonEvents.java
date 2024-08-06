package com.drmangotea.createindustry.events;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.worldgen.TFMGConfiguredFeatures;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class CommonEvents {

    public static void serverStarting(MinecraftServer event)
    {
        CreateTFMG.LOGGER.info("YEEEHAAW");
    }

    public static void register() {
        final Holder<PlacedFeature> initializeOil = TFMGConfiguredFeatures.OIL_PLACED;
        final Holder<PlacedFeature> initializeSimulatedOil = TFMGConfiguredFeatures.OIL_DEPOSIT_PLACED;

        ServerLifecycleEvents.SERVER_STARTED.register(CommonEvents::serverStarting);
    }
}
