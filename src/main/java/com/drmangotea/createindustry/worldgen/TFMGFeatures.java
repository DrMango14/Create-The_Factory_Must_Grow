package com.drmangotea.createindustry.worldgen;



import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.worldgen.oil_deposit.OilDepositFeature;
import com.drmangotea.createindustry.worldgen.oil_deposit.OilWellFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
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



   //public static final RegistryObject<Feature<OreConfiguration>> SIMULATED_OIL = FEATURES.register("simulated_oil", () ->
   //        new OreFeature(OreConfiguration.CODEC));


    public static final RegistryObject<Feature<NoneFeatureConfiguration>> OIL_DEPOSIT = FEATURES.register("oil_deposit", () ->
            new OilDepositFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> OIL_WELL = FEATURES.register("oil_well", () ->
            new OilWellFeature(NoneFeatureConfiguration.CODEC));


    //-------------------------------------------------------------------------------------------------//
    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
