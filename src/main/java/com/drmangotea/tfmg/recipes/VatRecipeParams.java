package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.base.pressure.Pressure;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatTypeEntry;
import com.drmangotea.tfmg.registry.TFMGVatTypes;
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
    public static final List<VatTypeEntry> types = List.of(
            TFMGVatTypes.CAST_IRON, TFMGVatTypes.STEEL, TFMGVatTypes.FIREPROOF
    );

    public static List<VatType> allVatTypes() {
        return types.stream().map(VatTypeEntry::get).toList();
    }

    public static final MapCodec<VatRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(VatRecipeParams::new).forGetter(Function.identity()),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("min_size", 0).forGetter(VatRecipeParams::getMinSize),
            Codec.INT.optionalFieldOf("heat_level", 0).forGetter(VatRecipeParams::getHeatLevel),
            Pressure.CODEC.optionalFieldOf("pressure", Pressure.EMPTY).forGetter(VatRecipeParams::getPressure),
            VatOperation.CODEC.listOf().optionalFieldOf("machines", new ArrayList<>()).forGetter(VatRecipeParams::getMachines),
            VatType.CODEC.listOf().optionalFieldOf("allowed_vat_types", List.of()).forGetter(VatRecipeParams::getAllowedVatTypes)
    ).apply(instance, (params, min_size, heat_level,pressure, machines, allowed_vat_types) -> {
        params.machines = machines;
        params.min_size = min_size;
        params.heat_level = heat_level;
        params.pressure = pressure;
        params.allowedVatTypes = allowed_vat_types;
        return params;
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, VatRecipeParams> STREAM_CODEC = streamCodec(VatRecipeParams::new);

    public int min_size = 1;
    public int heat_level = 0;
    public Pressure pressure = Pressure.EMPTY;

    public List<VatOperation> machines = new ArrayList<>();
    public List<VatType> allowedVatTypes = new ArrayList<>();

    protected final int getHeatLevel() {
        return heat_level;
    }

    protected final Pressure getPressure() {
        return pressure;
    }

    protected final int getMinSize() {
        return min_size;
    }

    protected final List<VatOperation> getMachines() {
        return machines;
    }

    protected final List<VatType> getAllowedVatTypes() {
        return allowedVatTypes;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.INT.encode(buffer, min_size);
        ByteBufCodecs.INT.encode(buffer, heat_level);
        Pressure.STREAM_CODEC.encode(buffer, pressure);

        CatnipStreamCodecBuilders.list(VatOperation.STREAM_CODEC).encode(buffer, machines);
        CatnipStreamCodecBuilders.list(VatType.STREAM_CODEC).encode(buffer, allowedVatTypes);

    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        min_size = ByteBufCodecs.INT.decode(buffer);
        heat_level = ByteBufCodecs.INT.decode(buffer);
        pressure = Pressure.STREAM_CODEC.decode(buffer);

        machines = CatnipStreamCodecBuilders.list(VatOperation.STREAM_CODEC).decode(buffer);
        allowedVatTypes = CatnipStreamCodecBuilders.list(VatType.STREAM_CODEC).decode(buffer);
    }
}
