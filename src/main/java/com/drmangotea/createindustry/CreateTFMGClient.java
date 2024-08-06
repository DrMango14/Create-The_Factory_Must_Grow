package com.drmangotea.createindustry;


import com.drmangotea.createindustry.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonRenderHandler;
import com.drmangotea.createindustry.items.weapons.flamethrover.FlamethrowerRenderHandler;
import com.drmangotea.createindustry.items.weapons.quad_potato_cannon.QuadPotatoCannonRenderHandler;
import com.drmangotea.createindustry.ponder.TFMGPonderIndex;
import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.drmangotea.createindustry.registry.TFMGParticleTypes;
import io.github.fabricators_of_create.porting_lib.event.client.ParticleManagerRegistrationCallback;
import net.fabricmc.api.ClientModInitializer;

public class CreateTFMGClient implements ClientModInitializer {
    public static final QuadPotatoCannonRenderHandler QUAD_POTATO_CANNON_RENDER_HANDLER = new QuadPotatoCannonRenderHandler();

    public static final AdvancedPotatoCannonRenderHandler ADVANCED_POTATO_CANNON_RENDER_HANDLER = new AdvancedPotatoCannonRenderHandler();

    public static final FlamethrowerRenderHandler FLAMETHROWER_RENDER_HANDLER = new FlamethrowerRenderHandler();


    @Override
    public void onInitializeClient() {
        TFMGPartialModels.init();

        ADVANCED_POTATO_CANNON_RENDER_HANDLER.registerListeners();
        QUAD_POTATO_CANNON_RENDER_HANDLER.registerListeners();
        FLAMETHROWER_RENDER_HANDLER.registerListeners();


//        ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.GREEN_FIRE.get(), RenderType.cutout());
//        ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.BLUE_FIRE.get(), RenderType.cutout());

        TFMGPonderIndex.register();
        TFMGPonderIndex.registerTags();
        ParticleManagerRegistrationCallback.EVENT.register(TFMGParticleTypes::registerFactories);
        initCompat();
    }

    @SuppressWarnings("Convert2MethodRef") // may cause class loading issues if changed
    private static void initCompat() {
        //used for client related compat
        //example: Mods.<YOUR MOD>.executeIfInstalled(() -> () -> <Compat Class>.<clientInit or init>());
    }
}




