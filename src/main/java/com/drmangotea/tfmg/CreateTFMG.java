package com.drmangotea.tfmg;

import com.drmangotea.tfmg.base.TFMGLangPartials;
import com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.tfmg.registry.*;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.data.AllLangPartials;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.LangMerger;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeGen;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.foundation.data.recipe.StandardRecipeGen;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
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
        //

        TFMGBlocks.register();
        TFMGItems.register();
        TFMGBlockEntities.register();
        TFMGEntityTypes.register();
        TFMGCreativeModeTabs.init();
        TFMGFluids.register();
        TFMGColoredFires.register(modEventBus);
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
