package com.drmangotea.tfmg;

import com.drmangotea.tfmg.base.*;
import com.drmangotea.tfmg.content.electricity.base.ElectricNetworkManager;
import com.drmangotea.tfmg.content.engines.fuels.BaseFuelTypes;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.base.TestSavedDataManager;
import com.drmangotea.tfmg.datagen.TFMGDatagen;
import com.drmangotea.tfmg.base.fluid.TFMGFluidInteractions;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.registry.*;
import com.drmangotea.tfmg.worldgen.TFMGFeatures;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

import static net.createmod.catnip.lang.FontHelper.styleFromColor;


@SuppressWarnings("removal")
@Mod(TFMG.MOD_ID)
public class TFMG {

    public static final String MOD_ID = "tfmg";
    public static final ElectricNetworkManager NETWORK_MANAGER = new ElectricNetworkManager();
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final TestSavedDataManager DEPOSITS = new TestSavedDataManager();



    public static final TFMGRegistrate REGISTRATE = TFMGRegistrate.create();

    public static final FontHelper.Palette TFMG_PALETTE = new FontHelper.Palette(styleFromColor(0x4c5155), styleFromColor(0x838c8a));

    static {
        REGISTRATE.setTooltipModifierFactory((item) -> (new ItemDescription.Modifier(item, TFMG_PALETTE)).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
        //.andThen(TooltipModifier.mapNull(CableTypeStats.create(item))) (save this for whenever the fuck I figure out what resistivity is meant to do)
    }


    public TFMG() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // ===== PHASE 1: CORE SETUP =====
        LOGGER.info("[TFMG] Initializing core systems...");
        REGISTRATE.registerEventListeners(modEventBus);
        TFMGConfigs.register(ModLoadingContext.get());
        TFMGRegistries.register();

        // Register event listeners early
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(EventPriority.LOWEST, TFMGDatagen::gatherData);
        modEventBus.addListener(TFMGSoundEvents::register);

        // ===== PHASE 2: CONTENT REGISTRATION =====
        LOGGER.info("[TFMG] Registering content...");

        // Sounds/Particles
        TFMGSoundEvents.prepare();
        TFMGParticleTypes.register(modEventBus);
        TFMGRecipeTypes.register(modEventBus);
        TFMGMobEffects.register(modEventBus);
        TFMGColoredFires.register(modEventBus);

        // Block/Item hierarchy
        TFMGBlocks.init();
        TFMGItems.init();
        TFMGEncasedBlocks.init();
        TFMGPaletteBlocks.init();
        TFMGBlockEntities.init();
        TFMGEntityTypes.init();
        TFMGMenuTypes.init();

        // Fluids after blocks
        TFMGFluids.init();

        // ===== PHASE 3: SYSTEMS SETUP =====
        LOGGER.info("[TFMG] Initializing systems...");
        TFMGPipes.init();
        TFMGElectrodes.register();
        TFMGCableTypes.register();
        TFMGMountedStorageTypes.register();
        TFMGFeatures.register(modEventBus);
        TFMGPackets.registerPackets();

        // ===== PHASE 4: CLIENT & CREATIVE =====
        LOGGER.info("[TFMG] Setting up client...");
        TFMGPartialModels.init();
        TFMGCreativeTabs.register(modEventBus);
        modEventBus.addListener(TFMGCreativeTabs::addCreative);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TFMGClient::new);

        // Common event bus registrations
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("[TFMG] Initialization complete!");
    }

    @SuppressWarnings("removal")
    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.GREEN_FIRE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.BLUE_FIRE.get(), RenderType.cutout());
    }

    /**
     * fluid interaction & firebox heating
     */
    public void commonSetup(final FMLCommonSetupEvent event) {
        TFMGFluidInteractions.registerFluidInteractions();

        event.enqueueWork(() -> {
            BaseFuelTypes.register();
            TFMGBoilerHeaters.registerDefaults();

        });
    }

    public void onRegister(final RegisterEvent event) {
        TFMGContraptions.prepare();
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
