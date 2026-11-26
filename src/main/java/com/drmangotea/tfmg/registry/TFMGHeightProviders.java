package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.world.height_provider.WorldBottomHeight;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

public class TFMGHeightProviders {
    private static final DeferredRegister<HeightProviderType<?>> REGISTER = DeferredRegister.create(Registries.HEIGHT_PROVIDER_TYPE, TFMG.MOD_ID);

    public static final DeferredHolder<HeightProviderType<?>, HeightProviderType<WorldBottomHeight>> BOTTOM = REGISTER.register("bottom", () -> () -> WorldBottomHeight.CODEC);

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
