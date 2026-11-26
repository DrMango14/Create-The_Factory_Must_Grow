package com.drmangotea.tfmg.content.items.weapons.flamethrover;

import com.drmangotea.tfmg.TFMGRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public record FlamethrowerFuelType(HolderSet<Fluid> fluids, int spread, float speed, int amount, int color) {

    public static final Map<Fluid, Optional<Holder.Reference<FlamethrowerFuelType>>> typeCache = new HashMap<>();

    public static final Codec<FlamethrowerFuelType> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(FlamethrowerFuelType::fluids),
            Codec.INT.optionalFieldOf("spread", 15).forGetter(FlamethrowerFuelType::spread),
            Codec.FLOAT.optionalFieldOf("speed", 1f).forGetter(FlamethrowerFuelType::speed),
            Codec.INT.optionalFieldOf("amount", 4).forGetter(FlamethrowerFuelType::amount),
            Codec.INT.optionalFieldOf("color", 0xC4AA76). forGetter(FlamethrowerFuelType::color)
    ).apply(i, FlamethrowerFuelType::new));

    public static Optional<Holder.Reference<FlamethrowerFuelType>> getTypeForFluid(RegistryAccess registryAccess, Fluid fluid) {
        return typeCache.computeIfAbsent(fluid, f -> registryAccess.lookupOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE)
                .listElements()
                .filter(ref -> ref.value().fluids.contains(f.builtInRegistryHolder()))
                .findFirst());
    }

    public static class Builder {
        private final List<Holder<Fluid>> fluids = new ArrayList<>();
        private int spread = 15;
        private float speed = 1f;
        private int amount = 4;
        private int color = 0xC4AA76;

        public Builder spread(int spread) {
            this.spread = spread;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder addFluids(Fluid... fluids) {
            for (Fluid fluid : fluids)
                this.fluids.add(FluidHelper.convertToStill(fluid).builtInRegistryHolder());
            return this;
        }

        public FlamethrowerFuelType build() {
            return new FlamethrowerFuelType(HolderSet.direct(fluids), spread, speed, amount, color);
        }
    }
}
