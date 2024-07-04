package com.drmangotea.tfmg;

import com.drmangotea.tfmg.base.TFMGContraptions;
import com.drmangotea.tfmg.base.datagen.TFMGDatagen;
import com.drmangotea.tfmg.items.weapons.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.tfmg.registry.*;
import com.drmangotea.tfmg.worldgen.TFMGFeatures;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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

import static com.simibubi.create.content.fluids.tank.BoilerHeaters.registerHeater;


@Mod(CreateTFMG.MOD_ID)
public class CreateTFMG
{

    public static final String MOD_ID = "tfmg";
    public static final String NAME = "Create: The Factory Must Grow";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();


    static {
        REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public CreateTFMG()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());



        TFMGSoundEvents.prepare();
        TFMGTags.init();
        TFMGBlocks.register();
        TFMGItems.register();
        TFMGBlockEntities.register();
        TFMGEntityTypes.register();
        TFMGCreativeModeTabs.register(modEventBus);
        TFMGFluids.register();
        TFMGPaletteBlocks.register();
        TFMGParticleTypes.register(modEventBus);
        TFMGMobEffects.register(modEventBus);
        TFMGPotions.register(modEventBus);
        TFMGPackets.registerPackets();


        TFMGColoredFires.register(modEventBus);
        TFMGFeatures.FEATURES.register(modEventBus);
        TFMGRecipeTypes.register(modEventBus);
        TFMGContraptions.prepare();

        modEventBus.addListener(CreateTFMG::init);
        modEventBus.addListener(EventPriority.LOWEST, TFMGDatagen::gatherData);
        modEventBus.addListener(TFMGSoundEvents::register);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CreateTFMGClient::new);
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);


    }
    public static void init(final FMLCommonSetupEvent event) {
        TFMGFluids.registerFluidInteractions();

        event.enqueueWork(() -> {

            registerHeater(TFMGBlocks.FIREBOX.get(), (level, pos, state) -> {
                BlazeBurnerBlock.HeatLevel value = state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
                if (value == BlazeBurnerBlock.HeatLevel.NONE) {
                    return -1;
                }
                if (value == BlazeBurnerBlock.HeatLevel.SEETHING) {
                    return 3;
                }
                if (value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
                    return 2;
                }
                return -1;
            });

        });
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
