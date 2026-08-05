package com.drmangotea.tfmg.base.events;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelTypeManager;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TFMGCommonEvents {



    @SubscribeEvent
    public static void onUnloadWorld(LevelEvent.Unload event) {
        LevelAccessor world = event.getLevel();
        TFMG.NETWORK_MANAGER.onUnloadWorld(world);


    }

    @SubscribeEvent
    public static void onLoadWorld(LevelEvent.Load event) {
        LevelAccessor world = event.getLevel();
        TFMG.NETWORK_MANAGER.onLoadWorld(world);
        TFMG.DEPOSITS.levelLoaded(world);
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {

        if (event.getEntity() instanceof Player player) {
            if (player.getItemInHand(InteractionHand.OFF_HAND).is(TFMGItems.CONFIGURATION_WRENCH.get()) && event.getLevel().getBlockEntity(event.getPos()) instanceof IElectric be && be.canBeInGroups()) {

                // group system removed - wrench no longer sets group id
                be.updateNextTick();
                be.sendStuff();

            }
        }
    }

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(EngineFuelTypeManager.ReloadListener.INSTANCE);
    }


}
