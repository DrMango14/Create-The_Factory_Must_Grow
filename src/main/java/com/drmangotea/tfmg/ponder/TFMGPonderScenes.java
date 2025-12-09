package com.drmangotea.tfmg.ponder;

import com.drmangotea.tfmg.ponder.scenes.electricity.GeneratorScenes;
import com.drmangotea.tfmg.ponder.scenes.kinetics.engines.EngineScenes;
import com.drmangotea.tfmg.ponder.scenes.metallurgy.BlastFurnaceScenes;
import com.drmangotea.tfmg.ponder.scenes.metallurgy.CokeOvenScenes;
import com.drmangotea.tfmg.ponder.scenes.metallurgy.DistillationScenes;
import com.drmangotea.tfmg.ponder.scenes.metallurgy.PumpjackScenes;
import com.drmangotea.tfmg.ponder.scenes.vat.VatScenes;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class TFMGPonderScenes {


    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER, TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
                .addStoryBoard("distillation_tower", DistillationScenes::distillation_tower, TFMGPonderTags.OIL_PROCESSING);
        HELPER.forComponents(TFMGBlocks.BLAST_FURNACE_OUTPUT, TFMGBlocks.BLAST_FURNACE_HATCH)
                .addStoryBoard("blast_furnace", BlastFurnaceScenes::blast_furnace, TFMGPonderTags.METALLURGY);
        HELPER.forComponents(TFMGBlocks.STEEL_CHEMICAL_VAT, TFMGBlocks.CAST_IRON_CHEMICAL_VAT, TFMGBlocks.FIREPROOF_CHEMICAL_VAT).addStoryBoard("chemical_vat", VatScenes::chemical_vat, TFMGPonderTags.CHEMICAL_VAT);
        HELPER.forComponents( TFMGBlocks.INDUSTRIAL_MIXER).addStoryBoard("chemical_vat", VatScenes::industrial_mixer, TFMGPonderTags.CHEMICAL_VAT);
        HELPER.forComponents( TFMGBlocks.ELECTRODE_HOLDER).addStoryBoard("chemical_vat", VatScenes::electrolysis, TFMGPonderTags.CHEMICAL_VAT);
        HELPER.forComponents( TFMGBlocks.ELECTRODE_HOLDER).addStoryBoard("chemical_vat", VatScenes::arc_furnace, TFMGPonderTags.CHEMICAL_VAT);
        HELPER.forComponents(TFMGBlocks.COKE_OVEN)
                .addStoryBoard("coke_oven", CokeOvenScenes::coke_oven, TFMGPonderTags.METALLURGY);
        HELPER.forComponents(TFMGBlocks.PUMPJACK_BASE,TFMGBlocks.PUMPJACK_CRANK,TFMGBlocks.PUMPJACK_HAMMER)
                .addStoryBoard("pumpjack", PumpjackScenes::pumpjack, TFMGPonderTags.OIL_PROCESSING);
        HELPER.forComponents(TFMGBlocks.REGULAR_ENGINE, TFMGBlocks.TURBINE_ENGINE, TFMGBlocks.RADIAL_ENGINE)
                .addStoryBoard("engines", EngineScenes::engines, TFMGPonderTags.ENGINES);
        HELPER.forComponents(TFMGBlocks.GENERATOR, TFMGBlocks.ROTOR, TFMGBlocks.STATOR, TFMGBlocks.ELECTRIC_MOTOR)
                .addStoryBoard("electricity", GeneratorScenes::electricity, TFMGPonderTags.ELECTRIC_MACHINERY)
                .addStoryBoard("electricity_two", GeneratorScenes::electricy_two, TFMGPonderTags.ELECTRIC_MACHINERY);

    }
}
