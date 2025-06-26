package com.drmangotea.tfmg.config;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class TFMGResistivity extends ConfigBase {
    // bump this version to reset configured values.
    private static final int VERSION = 2;

    // IDs need to be used since configs load before registration

    private static final Object2DoubleMap<ResourceLocation> DEFAULT_RESISTIVITIES = new Object2DoubleOpenHashMap<>();

    protected final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> resistivities = new HashMap<>();

    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        builder.comment(".", Comments.resistivity).push("resistivity");
        DEFAULT_RESISTIVITIES.forEach((id, value) -> this.resistivities.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    @Override
    public String getName() {
        return "resistivityValues.v" + VERSION;
    }

    @Nullable
    public DoubleSupplier getResistivity(CableType cableType) {
        ResourceLocation id = cableType.getKey();
        ForgeConfigSpec.ConfigValue<Double> value = this.resistivities.get(id);
        return value == null ? null : value::get;
    }

    public static <B extends CableType, P> NonNullUnaryOperator<CableTypeBuilder<B, P>> setNoResistivity() {
        return setResistivity(0);
    }

    public static <B extends CableType, P> NonNullUnaryOperator<CableTypeBuilder<B, P>> setResistivity(double value) {
        return builder -> {
            //assertFromCreate(builder);
            ResourceLocation id = TFMG.asResource(builder.getName());
            DEFAULT_RESISTIVITIES.put(id, value);
            return builder;
        };
    }

    private static class Comments {
        static String resistivity = "Configure the individual resistivity of cable types.";
    }
}
