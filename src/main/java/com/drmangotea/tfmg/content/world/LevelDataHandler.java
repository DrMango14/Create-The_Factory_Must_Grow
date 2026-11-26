package com.drmangotea.tfmg.content.world;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

//Possibly useful for future stuff
public class LevelDataHandler {
    public static LevelDataHandler instance = new LevelDataHandler();

    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide()  && event.getLevel() instanceof ServerLevel serverLevel) {

        }
    }

    @SubscribeEvent
    public void onLevelSave(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverLevel) {

        }
    }
}
