package com.drmangotea.tfmg;

import com.drmangotea.tfmg.base.*;
import com.drmangotea.tfmg.content.electricity.base.ElectricNetworkManager;

import com.drmangotea.tfmg.content.electricity.experimental.saved_data.NetworkDataManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.engines.fuels.BaseFuelTypes;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base.OilDataManager;
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
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;


@Mod(TFMG.MOD_ID)
public class TFMG {

    public static final String MOD_ID = "tfmg";
    public static final ElectricNetworkManager NETWORK_MANAGER = new ElectricNetworkManager();

    public static final RealElectricNetworkManager ELECTRIC_NETWORK_MANAGER = new RealElectricNetworkManager();
    public static final NetworkDataManager ELECTRICAL_NETWORK_DATA = new NetworkDataManager();

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final OilDataManager DEPOSITS = new OilDataManager();

    public static final TFMGRegistrate REGISTRATE = TFMGRegistrate.create(MOD_ID)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );


    public TFMG(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);



        TFMGSoundEvents.prepare();
        TFMGElectrodes.init();
        TFMGCableTypes.init();
        TFMGDisplaySources.init();
        TFMGCreativeTabs.register(modEventBus);
        TFMGBlocks.init();
        TFMGPipes.init();
        TFMGBlockEntities.init();
        TFMGItems.init();
        TFMGEntityTypes.init();
        TFMGPartialModels.init();
        TFMGFluids.init();
        TFMGMenuTypes.init();
        TFMGEncasedBlocks.init();
        TFMGPaletteBlocks.init();



        TFMGParticleTypes.register(modEventBus);

        TFMGDataComponents.register(modEventBus);
        TFMGMobEffects.register(modEventBus);
        TFMGRecipeTypes.register(modEventBus);
       // TFMGArmorMaterials.register(modEventBus);
        TFMGColoredFires.register(modEventBus);
        TFMGFeatures.register(modEventBus);
        TFMGMountedStorageTypes.register();

        modEventBus.addListener(TFMG::onRegister);
        TFMGPackets.register();
        TFMGConfigs.register(modLoadingContext, modContainer);
        modEventBus.addListener(EventPriority.HIGHEST, TFMGDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, TFMGDatagen::gatherData);
        modEventBus.addListener(TFMGSoundEvents::register);
        modEventBus.addListener(TFMG::commonSetup);
        modEventBus.addListener(this::clientSetup);
      //  NeoForge.EVENT_BUS.register(this);
       // DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TFMGClient::new);
        modEventBus.addListener(TFMGCreativeTabs::addCreative);

    }
    @SuppressWarnings("deprecation")
    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.GREEN_FIRE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TFMGColoredFires.BLUE_FIRE.get(), RenderType.cutout());
    }

    /**
     * fluid interaction & firebox heating
     */
    public static void commonSetup(final FMLCommonSetupEvent event) {
        TFMGFluidInteractions.registerFluidInteractions();

        event.enqueueWork(() -> {
            BaseFuelTypes.register();
            TFMGBoilerHeaters.registerDefaults();

        });
    }

    public static void onRegister(final RegisterEvent event) {
        TFMGContraptions.prepare();
    }


    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static TFMGRegistrate registrate() {
        return REGISTRATE;
    }
}
