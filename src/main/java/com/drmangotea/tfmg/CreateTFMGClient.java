package com.drmangotea.tfmg;


import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CreateTFMGClient {

    public CreateTFMGClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        //CIPartialModels.init();
        modEventBus.register(this);

    }


    @SubscribeEvent
    public void setup(final FMLClientSetupEvent event) {
      //  CIPonderIndex.register();
        //CIPonderIndex.registerTags();
    }


}




