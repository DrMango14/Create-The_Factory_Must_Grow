package com.drmangotea.tfmg.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class PolarizingRecipeParams extends ProcessingRecipeParams {
    public int energy = 2000;

    public static final MapCodec<PolarizingRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(PolarizingRecipeParams::new).forGetter(Function.identity()),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("energy", 2000).forGetter(PolarizingRecipeParams::getEnergy)
    ).apply(instance, (params, energy) -> {
        params.energy = energy;
        return params;
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, PolarizingRecipeParams> STREAM_CODEC = streamCodec(PolarizingRecipeParams::new);

    public int getEnergy() {
        return energy;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.INT.encode(buffer, energy);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        energy = ByteBufCodecs.INT.decode(buffer);
    }
}
