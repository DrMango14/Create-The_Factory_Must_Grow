package com.drmangotea.createindustry;

import com.drmangotea.createindustry.base.TFMGRegistrate;
import com.drmangotea.createindustry.base.datagen.TFMGDataGen;
import com.drmangotea.createindustry.items.weapons.flamethrover.BuiltinFlamethrowerFuelTypes;
import com.drmangotea.createindustry.items.weapons.flamethrover.FlamethrowerFuelType;
import com.drmangotea.createindustry.items.weapons.flamethrover.FlamethrowerFuelTypeManager;
import com.drmangotea.createindustry.registry.TFMGContraptions;
import com.drmangotea.createindustry.base.TFMGLangPartials;
import com.drmangotea.createindustry.config.TFMGConfigs;
import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.createindustry.registry.*;
import com.drmangotea.createindustry.worldgen.TFMGConfiguredFeatures;
import com.drmangotea.createindustry.worldgen.TFMGFeatures;
import com.drmangotea.createindustry.worldgen.TFMGOreConfigEntries;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.LangMerger;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
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
import net.minecraftforge.fml.ModLoadingContext;
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

    public static final String MOD_ID = "createindustry";
    public static final String NAME = "Create: The Factory Must Grow";
    public static final TFMGRegistrate REGISTRATE = TFMGRegistrate.create();
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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        //

        TFMGBlocks.register();
        TFMGItems.register();
        TFMGBlockEntities.register();
        TFMGEntityTypes.register();
        TFMGCreativeModeTabs.init();
        TFMGFluids.register();
        TFMGTags.init();
        TFMGPaletteBlocks.register();
        TFMGSoundEvents.prepare();
        TFMGContraptions.prepare();
        TFMGOreConfigEntries.init();
        TFMGParticleTypes.register(modEventBus);
        TFMGMobEffects.register(modEventBus);
        TFMGPotions.register(modEventBus);
        TFMGPackets.registerPackets();


        TFMGColoredFires.register(modEventBus);
        TFMGFeatures.register(modEventBus);
        TFMGRecipeTypes.register(modEventBus);

        TFMGConfigs.register(ModLoadingContext.get());

        //
        modEventBus.addListener(CreateTFMG::init);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(EventPriority.LOWEST, TFMGDataGen::gatherData);
        modEventBus.addListener(TFMGSoundEvents::register);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CreateTFMGClient::new);
        modEventBus.addListener(this::clientSetup);

    }

    public static void init(final FMLCommonSetupEvent event) {
        TFMGFluids.registerFluidInteractions();

        event.enqueueWork(() -> {
            BuiltinFlamethrowerFuelTypes.register();
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
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            final Holder<PlacedFeature> initializeOil = TFMGConfiguredFeatures.OIL_PLACED;
            final Holder<PlacedFeature> initializeSimulatedOil = TFMGConfiguredFeatures.OIL_DEPOSIT_PLACED;

        });
        TFMGMobEffects.init();
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("YEEEHAAW");
        for (FlamethrowerFuelType type : FlamethrowerFuelTypeManager.BUILTIN_TYPE_MAP.values()) {
            LOGGER.info("Registered Builtin Flamethrower Fuel type: {}", FlamethrowerFuelTypeManager.getIdForType(type));
        }
        for (FlamethrowerFuelType type : FlamethrowerFuelTypeManager.CUSTOM_TYPE_MAP.values()) {
            LOGGER.info("Registered Custom Flamethrower Fuel type: {}", FlamethrowerFuelTypeManager.getIdForType(type));
        }
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
