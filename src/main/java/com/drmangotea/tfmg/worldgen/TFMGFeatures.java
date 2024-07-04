package com.drmangotea.tfmg.worldgen;



import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.worldgen.oil_deposit.OilDepositFeature;
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

        public static final RegistryObject<OilDepositFeature> OIL_DEPOSIT = FEATURES.register("oil_deposit", () -> new OilDepositFeature(NoneFeatureConfiguration.CODEC));


}
