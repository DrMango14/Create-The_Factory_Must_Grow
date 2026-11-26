package com.drmangotea.tfmg.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class IndustrialBlastingRecipeParams extends ProcessingRecipeParams {
    public static final MapCodec<IndustrialBlastingRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(IndustrialBlastingRecipeParams::new).forGetter(Function.identity()),
            Codec.INT.optionalFieldOf("hot_air_usage", 0).forGetter(IndustrialBlastingRecipeParams::hotAirUsage)
    ).apply(instance, (params, hotAirUsage) -> {
        params.hotAirUsage = hotAirUsage;
        return params;
    }));
    public static final StreamCodec<RegistryFriendlyByteBuf, IndustrialBlastingRecipeParams> STREAM_CODEC = streamCodec(IndustrialBlastingRecipeParams::new);

    protected int hotAirUsage = 0;

    protected final int hotAirUsage() {
        return hotAirUsage;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.INT.encode(buffer, hotAirUsage);
    }

   @Override
   protected void decode(RegistryFriendlyByteBuf buffer) {
       super.decode(buffer);
       hotAirUsage = ByteBufCodecs.INT.decode(buffer);
   }
}