package com.drmangotea.tfmg.ponder;

import com.drmangotea.tfmg.ponder.scenes.MetallurgyScenes;
import com.drmangotea.tfmg.ponder.scenes.MiscTFMGScenes;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class TFMGPonderScenes {


    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?,?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER, TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
                .addStoryBoard("distillation_tower", MiscTFMGScenes::distillation_tower, TFMGPonderTags.OIL_PROCESSING);
        HELPER.forComponents(TFMGBlocks.BLAST_FURNACE_OUTPUT, TFMGBlocks.BLAST_FURNACE_HATCH)
                .addStoryBoard("blast_furnace", MetallurgyScenes::blast_furnace, TFMGPonderTags.METALLURGY);
        HELPER.forComponents(TFMGBlocks.STEEL_CHEMICAL_VAT,
                TFMGBlocks.CAST_IRON_CHEMICAL_VAT,
                TFMGBlocks.FIREPROOF_CHEMICAL_VAT,
                TFMGBlocks.INDUSTRIAL_MIXER,
                TFMGBlocks.ELECTRODE_HOLDER
        ).addStoryBoard("chemical_vat", MiscTFMGScenes::chemical_vat, TFMGPonderTags.CHEMICAL_VAT);
        HELPER.forComponents(TFMGBlocks.COKE_OVEN)
                .addStoryBoard("coke_oven", MetallurgyScenes::coke_oven, TFMGPonderTags.METALLURGY);
        HELPER.forComponents(TFMGBlocks.PUMPJACK_BASE,TFMGBlocks.PUMPJACK_CRANK,TFMGBlocks.PUMPJACK_HAMMER)
                .addStoryBoard("pumpjack", MiscTFMGScenes::pumpjack, TFMGPonderTags.OIL_PROCESSING);
        HELPER.forComponents(TFMGBlocks.REGULAR_ENGINE, TFMGBlocks.TURBINE_ENGINE, TFMGBlocks.RADIAL_ENGINE)
                .addStoryBoard("engines", MiscTFMGScenes::engines, TFMGPonderTags.ENGINES);
        HELPER.forComponents(TFMGBlocks.GENERATOR, TFMGBlocks.ROTOR, TFMGBlocks.STATOR, TFMGBlocks.ELECTRIC_MOTOR)
                .addStoryBoard("electricity", MiscTFMGScenes::electricity, TFMGPonderTags.ELECTRIC_MACHINERY)
                .addStoryBoard("electricity_two", MiscTFMGScenes::electricy_two, TFMGPonderTags.ELECTRIC_MACHINERY);

    }
}
