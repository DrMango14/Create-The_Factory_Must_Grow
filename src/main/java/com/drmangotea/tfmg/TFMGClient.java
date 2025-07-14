package com.drmangotea.tfmg;

import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonRenderHandler;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerRenderHandler;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonRenderHandler;
import com.drmangotea.tfmg.ponder.TFMGPonderPlugin;
import com.drmangotea.tfmg.registry.TFMGParticleTypes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = TFMG.MOD_ID, dist = Dist.CLIENT)
public class TFMGClient {

    /**
     * does not work, too bad!
     */
    public static final QuadPotatoCannonRenderHandler QUAD_POTATO_CANNON_RENDER_HANDLER = new QuadPotatoCannonRenderHandler();
    public static final AdvancedPotatoCannonRenderHandler ADVANCED_POTATO_CANNON_RENDER_HANDLER = new AdvancedPotatoCannonRenderHandler();

    public static final FlamethrowerRenderHandler FLAMETHROWER_RENDER_HANDLER = new FlamethrowerRenderHandler();

    @SuppressWarnings("removal")
    public TFMGClient(net.neoforged.bus.api.IEventBus modEventBus) {
        //IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(TFMGParticleTypes::registerFactories);
        modEventBus.register(this);

        PonderIndex.addPlugin(new TFMGPonderPlugin());


      // ADVANCED_POTATO_CANNON_RENDER_HANDLER.registerListeners(forgeEventBus);
      // QUAD_POTATO_CANNON_RENDER_HANDLER.registerListeners(forgeEventBus);
      // FLAMETHROWER_RENDER_HANDLER.registerListeners(forgeEventBus);
    }


    @SubscribeEvent
    public void setup(final FMLClientSetupEvent event) {
        //TFMGPonderIndex.register();
        //TFMGPonderIndex.registerTags();
    }
}
