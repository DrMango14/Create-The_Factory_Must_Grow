package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.items.weapons.fire_extinguisher.FireExtinguisherFuelType;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class TFMGFireExtinguisherFuelTypes {
    public static final ResourceKey<FireExtinguisherFuelType> FALLBACK = ResourceKey.create(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE, TFMG.asResource("fallback"));

    public static void bootstrap(BootstrapContext<FireExtinguisherFuelType> ctx) {
        register(ctx, "fallback", new FireExtinguisherFuelType.Builder()
                .spread(0).speed(0)
                .color(0x000000).clearRadius(0)
                .build());

        register(ctx, "carbon_dioxide", new FireExtinguisherFuelType.Builder()
                .spread(10).speed(0.5f)
                .color(0x525252).clearRadius(1)
                .addFluids(TFMGFluids.CARBON_DIOXIDE.getSource())
                .build());
    }

    private static void register(BootstrapContext<FireExtinguisherFuelType> ctx, String name, FireExtinguisherFuelType type) {
        ctx.register(ResourceKey.create(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE, TFMG.asResource(name)), type);
    }
}
