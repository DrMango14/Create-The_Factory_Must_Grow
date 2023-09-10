package com.drmangotea.tfmg;


import com.drmangotea.tfmg.content.gadgets.quad_potato_cannon.QuadPotatoCannonRenderHandler;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CreateTFMGClient {
    public static final QuadPotatoCannonRenderHandler QUAD_POTATO_CANNON_RENDER_HANDLER = new QuadPotatoCannonRenderHandler();
    public CreateTFMGClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TFMGPartialModels.init();
        modEventBus.register(this);

    }


    @SubscribeEvent
    public void setup(final FMLClientSetupEvent event) {
      //  CIPonderIndex.register();
        //CIPonderIndex.registerTags();
    }


}




