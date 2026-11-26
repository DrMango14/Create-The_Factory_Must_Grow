package com.drmangotea.tfmg.remap;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Remap(String old, ResourceLocation current) {
    private static final Map<ResourceKey<? extends Registry<?>>, List<Remap>> REMAPS = new HashMap<>();

    @SafeVarargs
    public final Remap forTypes(ResourceKey<? extends Registry<?>>... registries) {
        for (ResourceKey<? extends Registry<?>> registry : registries) {
            REMAPS.computeIfAbsent(registry, k -> new ArrayList<>()).add(this);
        }
        return this;
    }

    public static Remap block(String old, ResourceLocation current) {
        return new Remap(old, current).forTypes(Registries.BLOCK, Registries.ITEM);
    }

    public static Remap item(String old, ResourceLocation current) {
        return new Remap(old, current).forTypes(Registries.ITEM);
    }

    public static Remap fluid(String old, ResourceLocation current) {
        return new Remap(old, current).forTypes(Registries.FLUID);
    }

    public static Remap recipe(String old, ResourceLocation current) {
        return new Remap(old, current).forTypes(Registries.RECIPE_TYPE, Registries.RECIPE_SERIALIZER);
    }

    public static void remap(Registry<?> registry) {
        List<Remap> remaps = REMAPS.remove(registry.key());
        if (remaps == null) {
            return;
        }

        for (Remap remap : remaps) {
            registry.addAlias(TFMG.asResource(remap.old()), remap.current());
            TFMG.LOGGER.debug("[TFMG Remapper ({})] Remapped {} to {}", registry.key().location(), TFMG.asResource(remap.old()), remap.current());
        }
    }
}
