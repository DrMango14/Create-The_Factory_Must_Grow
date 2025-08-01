package com.drmangotea.tfmg.content.items.weapons.flamethrover;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.registry.TFMGFlamethrowerFuelTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record FlamethrowerFuel(@Nullable ResourceKey<FlamethrowerFuelType> fuelType, int amount, int color) {

    public static final Codec<FlamethrowerFuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).fieldOf("fuel_type").forGetter(FlamethrowerFuel::fuelType),
            Codec.INT.fieldOf("amount").forGetter(FlamethrowerFuel::amount),
            Codec.INT.optionalFieldOf("color", 0xFFFFFF).forGetter(fuel -> fuel.color == 0 ? 0xFFFFFF : fuel.color)
    ).apply(instance, FlamethrowerFuel::new));

    public static final StreamCodec<ByteBuf, FlamethrowerFuel> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(TFMGRegistries.FLAMETHROWER_FUEL_TYPE),
            FlamethrowerFuel::fuelType,
            ByteBufCodecs.INT,
            FlamethrowerFuel::amount,
            ByteBufCodecs.INT,
            FlamethrowerFuel::color,
            FlamethrowerFuel::new
    );

    public static final FlamethrowerFuel EMPTY = new FlamethrowerFuel(TFMGFlamethrowerFuelTypes.FALLBACK, 0, 0xFFFFFF);

    public FlamethrowerFuel decrement(int amount) {
        if (this.amount <= amount || fuelType == TFMGFlamethrowerFuelTypes.FALLBACK) {
            return EMPTY;
        }
        return new FlamethrowerFuel(fuelType, this.amount - amount, color);
    }

    public FlamethrowerFuel increment(int amount, int capacity) {
        if (fuelType == TFMGFlamethrowerFuelTypes.FALLBACK) {
            return EMPTY;
        }
        if (this.amount + amount > capacity) {
            return new FlamethrowerFuel(fuelType, capacity, color);
        }
        return new FlamethrowerFuel(fuelType, this.amount + amount, color);
    }

    public static FlamethrowerFuel createForType(RegistryAccess registryAccess, Fluid fluid, int amount) {
        Optional<Holder.Reference<FlamethrowerFuelType>> type = FlamethrowerFuelType.getTypeForFluid(registryAccess, fluid);
        return type.map(typeReference -> new FlamethrowerFuel(typeReference.getKey(), amount, typeReference.value().color())).orElse(EMPTY);
    }

    public static FlamethrowerFuel createForType(RegistryAccess registryAccess, FluidStack stack) {
        return createForType(registryAccess, stack.getFluid(), stack.getAmount());
    }

    public boolean isEmpty() {
        if (fuelType == TFMGFlamethrowerFuelTypes.FALLBACK) {
            return true;
        }
        return this.amount <= 0;
    }

    public boolean hasFuel() {
        return fuelType != TFMGFlamethrowerFuelTypes.FALLBACK;
    }

    public Optional<FlamethrowerFuelType> getFuelType(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).getOptional(fuelType);
    }

    public FlamethrowerFuelType getFuelTypeOrThrow(RegistryAccess registryAccess) {
        return getFuelType(registryAccess).orElseThrow(() -> new IllegalStateException("No fuel type found for " + fuelType));
    }
}
