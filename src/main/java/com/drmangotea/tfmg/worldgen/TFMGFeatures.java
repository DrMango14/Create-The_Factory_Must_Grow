package com.drmangotea.tfmg.worldgen;



import com.drmangotea.tfmg.CreateTFMG;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TFMGFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CreateTFMG.MOD_ID);

    //-------------------------------------------------------------------------------------------------//

    public static final RegistryObject<Feature<GeodeConfiguration>> OIL = FEATURES.register("oil", () ->
            new OilFeature(GeodeConfiguration.CODEC));



    public static final RegistryObject<Feature<OreConfiguration>> SIMULATED_OIL = FEATURES.register("simulated_oil", () ->
            new OreFeature(OreConfiguration.CODEC));


    //-------------------------------------------------------------------------------------------------//
    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
