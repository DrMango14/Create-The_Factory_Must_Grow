package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.world.placement_modifier.BooleanConfigPlacementModifier;
import com.drmangotea.tfmg.content.world.placement_modifier.WorldBottomModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

public class TFMGPlacementModifiers {
    private static final DeferredRegister<PlacementModifierType<?>> REGISTER = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, TFMG.MOD_ID);

    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<BooleanConfigPlacementModifier>> CONFIG_BOOLEAN = REGISTER.register("config_boolean", () -> () -> BooleanConfigPlacementModifier.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<WorldBottomModifier>> WORLD_BOTTOM = REGISTER.register("world_bottom", () -> () -> WorldBottomModifier.CODEC);

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
