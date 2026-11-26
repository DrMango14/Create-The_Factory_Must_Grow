package com.drmangotea.tfmg.content.world.height_provider;

import com.drmangotea.tfmg.registry.TFMGHeightProviders;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;

public class WorldBottomHeight extends HeightProvider {

    public static final MapCodec<WorldBottomHeight> CODEC = MapCodec.unit(new WorldBottomHeight());

    @Override
    public int sample(RandomSource rn, WorldGenerationContext ctx) {
        return ctx.getMinGenY();
    }

    @Override
    public HeightProviderType<?> getType() {
        return TFMGHeightProviders.BOTTOM.get();
    }
}
