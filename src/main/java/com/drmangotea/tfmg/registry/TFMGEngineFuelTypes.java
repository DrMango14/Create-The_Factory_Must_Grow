package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class TFMGEngineFuelTypes {
    public static final ResourceKey<EngineFuelType> FALLBACK = key("fallback");

    public static final ResourceKey<EngineFuelType> GASOLINE = key("gasoline");
    public static final ResourceKey<EngineFuelType> DIESEL = key("diesel");
    public static final ResourceKey<EngineFuelType> LPG = key("lpg");
    public static final ResourceKey<EngineFuelType> KEROSENE = key("kerosene");
    public static final ResourceKey<EngineFuelType> NAPHTHA = key("naphtha");
    public static final ResourceKey<EngineFuelType> CREOSOTE = key("creosote");
    public static final ResourceKey<EngineFuelType> FURNACE_GAS = key("furnace_gas");

    private static ResourceKey<EngineFuelType> key(String name) {
        return ResourceKey.create(TFMGRegistries.ENGINE_FUEL_TYPE, TFMG.asResource(name));
    }

    public static void bootstrap(BootstrapContext<EngineFuelType> ctx) {
        register(ctx, FALLBACK, new EngineFuelType.Builder()
                .speed(1)
                .efficiency(1)
                .torque(1)
                .build());

        register(ctx, GASOLINE, new EngineFuelType.Builder(TFMGTags.Fluids.GASOLINE.tag)
                .speed(1)
                .efficiency(1)
                .torque(1)
                .build());

        register(ctx, DIESEL, new EngineFuelType.Builder(TFMGTags.Fluids.DIESEL.tag)
                .speed(0.8f)
                .efficiency(0.8f)
                .torque(1.4f)
                .build());

        register(ctx, LPG, new EngineFuelType.Builder(TFMGTags.Fluids.LPG.tag)
                .speed(1.2f)
                .efficiency(0.7f)
                .torque(0.7f)
                .build());

        register(ctx, KEROSENE, new EngineFuelType.Builder(TFMGTags.Fluids.KEROSENE.tag)
                .speed(0.7f)
                .efficiency(1f)
                .torque(1.4f)
                .build());

        register(ctx, NAPHTHA, new EngineFuelType.Builder(TFMGTags.Fluids.NAPHTHA.tag)
                .speed(1f)
                .efficiency(0.7f)
                .torque(1.3f)
                .build());

        register(ctx, CREOSOTE, new EngineFuelType.Builder(TFMGTags.Fluids.CREOSOTE.tag)
                .speed(0.7f)
                .efficiency(0.4f)
                .torque(0.5f)
                .build());

        register(ctx, FURNACE_GAS, new EngineFuelType.Builder(TFMGTags.Fluids.FURNACE_GAS.tag)
                .speed(0.5f)
                .efficiency(0.3f)
                .torque(0.3f)
                .build());
    }

    private static void register(BootstrapContext<EngineFuelType> ctx, ResourceKey<EngineFuelType> name, EngineFuelType type) {
        ctx.register(name, type);
    }
}
