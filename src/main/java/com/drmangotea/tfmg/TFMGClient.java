package com.drmangotea.tfmg;

import com.drmangotea.tfmg.content.electricity.ElectricNetworkRenderer;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonRenderHandler;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerRenderHandler;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonRenderHandler;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatCategoryEvent;
import com.drmangotea.tfmg.integration.curios.MultimeterCurios;
import com.drmangotea.tfmg.ponder.TFMGPonderPlugin;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGParticleTypes;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = TFMG.MOD_ID, dist = Dist.CLIENT)
public class TFMGClient {
    /**
     * does not work, too bad!
     */
    public static final QuadPotatoCannonRenderHandler QUAD_POTATO_CANNON_RENDER_HANDLER = new QuadPotatoCannonRenderHandler();
    public static final AdvancedPotatoCannonRenderHandler ADVANCED_POTATO_CANNON_RENDER_HANDLER = new AdvancedPotatoCannonRenderHandler();
    public static final FlamethrowerRenderHandler FLAMETHROWER_RENDER_HANDLER = new FlamethrowerRenderHandler();

    public static final ElectricNetworkRenderer ELECTRIC_NETWORK_RENDERER = new ElectricNetworkRenderer();

    public TFMGClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        IEventBus neoEventBus = NeoForge.EVENT_BUS;

        modEventBus.addListener(TFMGClient::clientInit);
        modEventBus.addListener(TFMGParticleTypes::registerFactories);
        //modEventBus.addListener(TFMGClientEvents::registerGuiOverlays);

        QUAD_POTATO_CANNON_RENDER_HANDLER.registerListeners(neoEventBus);
        ADVANCED_POTATO_CANNON_RENDER_HANDLER.registerListeners(neoEventBus);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new TFMGPonderPlugin());
        registerModelPredicates();
        ModLoader.postEvent(new VatCategoryEvent());
        MultimeterCurios.registerRenderer();
    }

    public static void registerModelPredicates() {
        ItemProperties.register(TFMGItems.LITHIUM_BLADE.get(), TFMG.asResource("lithium_charged"), (stack, level, entity, seed) -> stack.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) > 0 ? 1 : 0);
        ItemProperties.registerGeneric(TFMG.asResource("april_fools"), (stack, world, entity, s) -> TFMGUtils.isAprilFirst() ? 1 : 0);
    }
}
