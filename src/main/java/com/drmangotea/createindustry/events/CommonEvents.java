package com.drmangotea.createindustry.events;

import com.drmangotea.createindustry.items.weapons.flamethrover.FlamethrowerFuelTypeManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(FlamethrowerFuelTypeManager.ReloadListener.INSTANCE);
    }
}
