package com.drmangotea.tfmg.base.events;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuelType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.ApiStatus;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class TFMGRegistriesImpl {
    @ApiStatus.Internal
    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                TFMGRegistries.FLAMETHROWER_FUEL_TYPE,
                FlamethrowerFuelType.CODEC,
                FlamethrowerFuelType.CODEC
        );
    }
}
