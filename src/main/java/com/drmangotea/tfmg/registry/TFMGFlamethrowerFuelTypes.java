package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuelType;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class TFMGFlamethrowerFuelTypes {
    public static final ResourceKey<FlamethrowerFuelType> FALLBACK = ResourceKey.create(TFMGRegistries.FLAMETHROWER_FUEL_TYPE, TFMG.asResource("fallback"));

    public static void bootstrap(BootstrapContext<FlamethrowerFuelType> ctx) {
        register(ctx, "fallback", new FlamethrowerFuelType.Builder()
                .spread(0).speed(0)
                .amount(0).color(0x000000)
                .build());

        register(ctx, "gasoline", new FlamethrowerFuelType.Builder()
                .spread(15).speed(1)
                .amount(3).color(0xC4AA76)
                .addFluids(TFMGFluids.GASOLINE.get())
                .build());

        register(ctx, "diesel", new FlamethrowerFuelType.Builder()
                .spread(7).speed(2)
                .amount(3).color(0xBA9177)
                .addFluids(TFMGFluids.DIESEL.get())
                .build());

        register(ctx, "kerosene", new FlamethrowerFuelType.Builder()
                .spread(10).speed(1.3f)
                .amount(4).color(0x7876D5)
                .addFluids(TFMGFluids.KEROSENE.get())
                .build());

        register(ctx, "naphtha", new FlamethrowerFuelType.Builder()
                .spread(20).speed(0.8f)
                .amount(1).color(0x5E1B0A)
                .addFluids(TFMGFluids.NAPHTHA.get())
                .build());

        register(ctx, "lpg", new FlamethrowerFuelType.Builder()
                .spread(35).speed(0.6f)
                .amount(16).color(0xE0BB48)
                .addFluids(TFMGFluids.LPG.get())
                .build());

        register(ctx, "napalm", new FlamethrowerFuelType.Builder()
                .spread(20).speed(1.8f)
                .amount(15).color(0xA3C649)
                .addFluids(TFMGFluids.NAPALM.get())
                .build());

        register(ctx, "molten_slag", new FlamethrowerFuelType.Builder()
                .spread(15).speed(0.3f)
                .amount(15).color(0xFF8C00)
                .addFluids(TFMGFluids.MOLTEN_SLAG.get())
                .build());

    }

    private static void register(BootstrapContext<FlamethrowerFuelType> ctx, String name, FlamethrowerFuelType type) {
        ctx.register(ResourceKey.create(TFMGRegistries.FLAMETHROWER_FUEL_TYPE, TFMG.asResource(name)), type);
    }
}
