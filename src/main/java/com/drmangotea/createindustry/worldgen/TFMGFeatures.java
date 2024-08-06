package com.drmangotea.createindustry.worldgen;



import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.worldgen.oil_deposit.OilDepositFeature;
import com.drmangotea.createindustry.worldgen.oil_deposit.OilWellFeature;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TFMGFeatures {

    public static final LazyRegistrar<Feature<?>> FEATURES = LazyRegistrar.create(Registry.FEATURE, CreateTFMG.MOD_ID);

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
    public static void register() {
        FEATURES.register();
    }
}
