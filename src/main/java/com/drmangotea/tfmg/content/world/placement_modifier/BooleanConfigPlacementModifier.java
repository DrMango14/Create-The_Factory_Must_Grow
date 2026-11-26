package com.drmangotea.tfmg.content.world.placement_modifier;

import com.drmangotea.tfmg.registry.TFMGPlacementModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class BooleanConfigPlacementModifier extends PlacementFilter {
    private final ResourceLocation option;
    private final boolean enabledWhen;

    public static final Map<ResourceLocation, BooleanSupplier> OPTIONS = new HashMap<>();

    public BooleanConfigPlacementModifier(ResourceLocation option, boolean enabledWhen) {
        this.option = option;
        this.enabledWhen = enabledWhen;
    }

    public static final MapCodec<BooleanConfigPlacementModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("option").forGetter(BooleanConfigPlacementModifier::option),
            Codec.BOOL.fieldOf("enabled_when").forGetter(BooleanConfigPlacementModifier::enabledWhen)
    ).apply(instance, BooleanConfigPlacementModifier::new));

    public ResourceLocation option() {
        return option;
    }

    public boolean enabledWhen() {
        return enabledWhen;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        BooleanSupplier supplier = OPTIONS.get(option);
        return supplier != null && supplier.getAsBoolean() == enabledWhen;
    }

    @Override
    public PlacementModifierType<?> type() {
        return TFMGPlacementModifiers.CONFIG_BOOLEAN.get();
    }
}
