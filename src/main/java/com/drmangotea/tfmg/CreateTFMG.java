package com.drmangotea.tfmg;

import com.drmangotea.tfmg.base.TFMGLangPartials;
import com.drmangotea.tfmg.items.gadgets.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.tfmg.registry.*;
import com.drmangotea.tfmg.worldgen.TFMGConfiguredFeatures;
import com.drmangotea.tfmg.worldgen.TFMGFeatures;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.LangMerger;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(CreateTFMG.MOD_ID)
public class CreateTFMG
{

    public static final String MOD_ID = "tfmg";
    public static final String NAME = "Create: The Factory Must Grow";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateTFMG()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        //

        TFMGBlocks.register();
        TFMGItems.register();
        TFMGBlockEntities.register();
        TFMGEntityTypes.register();
        TFMGCreativeModeTabs.init();
        TFMGFluids.register();
        TFMGPaletteBlocks.register();

        TFMGColoredFires.register(modEventBus);
        TFMGFeatures.register(modEventBus);
        TFMGRecipeTypes.register(modEventBus);

        //
        modEventBus.addListener(EventPriority.LOWEST, CreateTFMG::gatherData);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CreateTFMGClient::new);
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(true, new LangMerger(gen, MOD_ID, NAME, TFMGLangPartials.values()));
    }
    @SuppressWarnings("removal")
    private void clientSetup(final FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.GREEN_FIRE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.BLUE_FIRE.get(), RenderType.cutout());
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            final Holder<PlacedFeature> initializeOil = TFMGConfiguredFeatures.OIL_PLACED;
            final Holder<PlacedFeature> initializeSimulatedOil = TFMGConfiguredFeatures.SIMULATED_OIL_PLACED;

        });
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("YEEEHAAW");
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
