package com.drmangotea.createindustry;

import com.drmangotea.createindustry.registry.TFMGContraptions;
import com.drmangotea.createindustry.config.TFMGConfigs;
import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire.TFMGColoredFires;
import com.drmangotea.createindustry.registry.*;
import com.drmangotea.createindustry.worldgen.TFMGFeatures;
import com.drmangotea.createindustry.worldgen.TFMGOreConfigEntries;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import static com.simibubi.create.content.fluids.tank.BoilerHeaters.registerHeater;


public class CreateTFMG implements ModInitializer {

    public static final String MOD_ID = "createindustry";
    public static final String NAME = "Create: The Factory Must Grow";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        REGISTRATE.setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    @Override
    public void onInitialize() {

        REGISTRATE.register();

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
        TFMGParticleTypes.register();
        TFMGMobEffects.register();
        TFMGPotions.register();
        TFMGPackets.registerPackets();


        TFMGColoredFires.register();
        TFMGFeatures.register();
        TFMGRecipeTypes.register();

        TFMGConfigs.register();

        CreateTFMG.init();

        //modEventBus.addListener(EventPriority.LOWEST, CreateTFMG::gatherData); CreateTFMGData gaming
        TFMGSoundEvents.register();
    }

    public static void init() {
        TFMGFluids.registerFluidInteractions();

        //event.enqueueWork(() -> {

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

        //});
    }





    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
