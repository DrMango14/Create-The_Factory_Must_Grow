package com.drmangotea.tfmg.content.items.weapons.fire_extinguisher;

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

public record FireExtinguisherFuelType(HolderSet<Fluid> fluids, int spread, float speed, int color, int clearRadius) {

    public static final Map<Fluid, Optional<Holder.Reference<FireExtinguisherFuelType>>> typeCache = new HashMap<>();

    public static final Codec<FireExtinguisherFuelType> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(FireExtinguisherFuelType::fluids),
            Codec.INT.optionalFieldOf("spread", 10).forGetter(FireExtinguisherFuelType::spread),
            Codec.FLOAT.optionalFieldOf("speed", 0.5f).forGetter(FireExtinguisherFuelType::speed),
            Codec.INT.optionalFieldOf("color", 0x525252). forGetter(FireExtinguisherFuelType::color),
            Codec.INT.optionalFieldOf("clearRadius", 1).forGetter(FireExtinguisherFuelType::clearRadius)
    ).apply(i, FireExtinguisherFuelType::new));

    public static Optional<Holder.Reference<FireExtinguisherFuelType>> getTypeForFluid(RegistryAccess registryAccess, Fluid fluid) {
        return typeCache.computeIfAbsent(fluid, f -> registryAccess.lookupOrThrow(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE)
                .listElements()
                .filter(ref -> ref.value().fluids.contains(f.builtInRegistryHolder()))
                .findFirst());
    }

    public static class Builder {
        private final List<Holder<Fluid>> fluids = new ArrayList<>();
        private int spread = 10;
        private float speed = 0.5f;
        private int color = 0x525252;
        private int clearRadius = 1;

        public Builder spread(int spread) {
            this.spread = spread;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder clearRadius(int clearRadius) {
            this.clearRadius = clearRadius;
            return this;
        }

        public Builder addFluids(Fluid... fluids) {
            for (Fluid fluid : fluids)
                this.fluids.add(FluidHelper.convertToStill(fluid).builtInRegistryHolder());
            return this;
        }

        public FireExtinguisherFuelType build() {
            return new FireExtinguisherFuelType(HolderSet.direct(fluids), spread, speed, color, clearRadius);
        }
    }
}
