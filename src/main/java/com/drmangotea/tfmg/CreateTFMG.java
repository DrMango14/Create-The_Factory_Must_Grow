package com.drmangotea.tfmg;

import com.drmangotea.tfmg.base.TFMGDatagen;
import com.drmangotea.tfmg.base.TFMGLangPartials;
import com.drmangotea.tfmg.items.gadgets.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.tfmg.registry.*;
import com.drmangotea.tfmg.worldgen.TFMGConfiguredFeatures;
import com.drmangotea.tfmg.worldgen.TFMGFeatures;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.LangMerger;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
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



        TFMGSoundEvents.prepare();
        TFMGBlocks.register();
        TFMGItems.register();
        TFMGBlockEntities.register();
        TFMGEntityTypes.register();
        TFMGCreativeModeTabs.register(modEventBus);
        TFMGFluids.register();
        TFMGPaletteBlocks.register();

        TFMGColoredFires.register(modEventBus);
        TFMGFeatures.register(modEventBus);
        TFMGRecipeTypes.register(modEventBus);

        TFMGConfigs.register(ModLoadingContext.get());

        modEventBus.addListener(EventPriority.LOWEST, TFMGDatagen::gatherData);
        modEventBus.addListener(TFMGSoundEvents::register);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CreateTFMGClient::new);
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);


    }


    @SuppressWarnings("removal")
    private void clientSetup(final FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.GREEN_FIRE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.BLUE_FIRE.get(), RenderType.cutout());
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
