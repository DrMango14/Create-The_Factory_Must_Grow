package com.drmangotea.createindustry.worldgen;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.DynamicDataProvider;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class TFMGOreConfigEntries {


    public static final OreFeatureConfigEntry LEAD_ORE =
            create("lead_ore", 10, 5, -63, 60)
                    .standardDatagenExt()
                    .withBlocks(Couple.create(TFMGBlocks.LEAD_ORE, TFMGBlocks.DEEPSLATE_LEAD_ORE))
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final OreFeatureConfigEntry NICKEL_ORE =
            create("nickel_ore", 8, 4, -63, 20)
                    .standardDatagenExt()
                    .withBlocks(Couple.create(TFMGBlocks.NICKEL_ORE, TFMGBlocks.DEEPSLATE_NICKEL_ORE))
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();


    public static final OreFeatureConfigEntry LITHIUM_ORE =
            create("lithium_ore", 12, 2, -63, 0)
                    .standardDatagenExt()
                    .withBlocks(Couple.create(TFMGBlocks.LITHIUM_ORE, TFMGBlocks.DEEPSLATE_LITHIUM_ORE))
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();


    ////
    private static OreFeatureConfigEntry create(String name, int clusterSize, float frequency,
                                                int minHeight, int maxHeight) {
        ResourceLocation id = Create.asResource(name);
        OreFeatureConfigEntry configDrivenFeatureEntry = new OreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
        return configDrivenFeatureEntry;
    }


    public static void init() {}

}
