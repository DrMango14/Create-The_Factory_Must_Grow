package com.drmangotea.tfmg.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class VatRecipeParams extends ProcessingRecipeParams {

    public static List<String> types = new ArrayList<>();

    static {
        types.add("tfmg:steel_vat");
        types.add("tfmg:cast_iron_vat");
        types.add("tfmg:firebrick_lined_vat");
    }

    public static MapCodec<VatRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(VatRecipeParams::new).forGetter(Function.identity()),
            Codec.INT.optionalFieldOf("min_size", 0)
                    .forGetter(VatRecipeParams::getMinSize),
            Codec.INT.optionalFieldOf("heat_level", 0)
                    .forGetter(VatRecipeParams::getHeatLevel),
            Codec.INT.optionalFieldOf("pressure", 0)
                    .forGetter(VatRecipeParams::getPressure),
            Codec.STRING.listOf().optionalFieldOf("machines", new ArrayList<>()).forGetter(VatRecipeParams::getMachines),
            Codec.STRING.listOf().optionalFieldOf("allowed_vat_types", types).forGetter(VatRecipeParams::getAllowedVatTypes)
    ).apply(instance, (params, min_size, heat_level,pressure, machines, allowed_vat_types) -> {
        params.machines = machines;
        params.min_size = min_size;
        params.heat_level = heat_level;
        params.pressure = pressure;
        params.allowedVatTypes = allowed_vat_types;
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, VatRecipeParams> STREAM_CODEC = streamCodec(VatRecipeParams::new);

    public int min_size;

    public int heat_level;

    public int pressure;

    public List<String> machines;
    public List<String> allowedVatTypes;

    protected final int getHeatLevel() {
        return heat_level;
    }

    protected final int getPressure() {
        return pressure;
    }


    protected final int getMinSize() {
        return min_size;
    }

    protected final List<String> getMachines() {
        return machines;
    }

    protected final List<String> getAllowedVatTypes() {
        return allowedVatTypes;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.INT.encode(buffer, min_size);
        ByteBufCodecs.INT.encode(buffer, heat_level);
        ByteBufCodecs.INT.encode(buffer, pressure);


        CatnipStreamCodecBuilders.list(ByteBufCodecs.STRING_UTF8).encode(buffer, machines);
        CatnipStreamCodecBuilders.list(ByteBufCodecs.STRING_UTF8).encode(buffer, allowedVatTypes);

    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        min_size = ByteBufCodecs.INT.decode(buffer);
        heat_level = ByteBufCodecs.INT.decode(buffer);
        pressure = ByteBufCodecs.INT.decode(buffer);

        machines = CatnipStreamCodecBuilders.list(ByteBufCodecs.STRING_UTF8).decode(buffer);
        allowedVatTypes = CatnipStreamCodecBuilders.list(ByteBufCodecs.STRING_UTF8).decode(buffer);

    }
}