package com.drmangotea.createindustry.events;

import com.drmangotea.createindustry.CreateTFMGClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class ClientEvents {
    public static void onTick(Minecraft client) {
        CreateTFMGClient.ADVANCED_POTATO_CANNON_RENDER_HANDLER.tick();
        CreateTFMGClient.FLAMETHROWER_RENDER_HANDLER.tick();
        CreateTFMGClient.QUAD_POTATO_CANNON_RENDER_HANDLER.tick();
    }

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(ClientEvents::onTick);
    }
}
