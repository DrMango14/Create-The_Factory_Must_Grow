package com.drmangotea.tfmg.ponder;

import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.ponder.scenes.*;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.PipeScenes;
import com.simibubi.create.infrastructure.ponder.scenes.fluid.PumpScenes;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class TFMGPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?,?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        // Chemistry
        HELPER.forComponents(TFMGBlocks.STEEL_CHEMICAL_VAT,
                TFMGBlocks.CAST_IRON_CHEMICAL_VAT,
                TFMGBlocks.FIREPROOF_CHEMICAL_VAT,
                TFMGBlocks.INDUSTRIAL_MIXER,
                TFMGBlocks.ELECTRODE_HOLDER
        ).addStoryBoard("chemical_vat", ChemistryScenes::chemicalVat, TFMGPonderTags.CHEMICAL_VAT);

        // Electricity
        HELPER.forComponents(TFMGBlocks.THREE_PHASE_GENERATOR, TFMGBlocks.ROTOR, TFMGBlocks.STATOR, TFMGBlocks.ELECTRIC_MOTOR)
                .addStoryBoard("electricity", ElectricityScenes::electricity, TFMGPonderTags.ELECTRIC_MACHINERY)
                .addStoryBoard("electric_subnetworks", ElectricityScenes::electricSubnetworks, TFMGPonderTags.ELECTRIC_MACHINERY);
        HELPER.forComponents(TFMGBlocks.LARGE_COIL)
                .addStoryBoard("large_transformer", ElectricityScenes::largeTransformer, TFMGPonderTags.ELECTRIC_MACHINERY);
        HELPER.forComponents(TFMGBlocks.WINDING_MACHINE)
                .addStoryBoard("winding_machine", ElectricityScenes::windingMachine, TFMGPonderTags.ELECTRIC_MACHINERY)
                .addStoryBoard("winding_machine", ElectricityScenes::windingMachineAutomation, TFMGPonderTags.ELECTRIC_MACHINERY);

        // Engines
        HELPER.forComponents(TFMGBlocks.REGULAR_ENGINE)
                 .addStoryBoard("regular_engine", EngineScenes::regularEngineAssembly, TFMGPonderTags.ENGINES)
                .addStoryBoard("regular_engine", EngineScenes::regularEngineOperation, TFMGPonderTags.ENGINES);
        HELPER.forComponents(TFMGBlocks.TURBINE_ENGINE)
                 .addStoryBoard("turbine_engine", EngineScenes::turbineEngineAssembly, TFMGPonderTags.ENGINES)
                .addStoryBoard("turbine_engine", EngineScenes::turbineEngineOperation, TFMGPonderTags.ENGINES);
        HELPER.forComponents(TFMGBlocks.RADIAL_ENGINE)
                 .addStoryBoard("radial_engine", EngineScenes::radialEngineAssembly, TFMGPonderTags.ENGINES)
                .addStoryBoard("radial_engine", EngineScenes::radialEngineOperation, TFMGPonderTags.ENGINES);
        HELPER.forComponents(TFMGBlocks.LARGE_ENGINE)
                 .addStoryBoard("large_engine", EngineScenes::largeEngine, TFMGPonderTags.ENGINES);
        HELPER.forComponents(TFMGBlocks.SIMPLE_LARGE_ENGINE)
                 .addStoryBoard("simple_large_engine", EngineScenes::simpleLargeEngine, TFMGPonderTags.ENGINES);

        // Metallurgy
        HELPER.forComponents(TFMGBlocks.BLAST_FURNACE_OUTPUT, TFMGBlocks.BLAST_FURNACE_HATCH)
                .addStoryBoard("blast_furnace", MetallurgyScenes::blastFurnace, TFMGPonderTags.METALLURGY);
        HELPER.forComponents(TFMGBlocks.BLAST_STOVE)
                .addStoryBoard("blast_stove", MetallurgyScenes::blastStove, TFMGPonderTags.METALLURGY);
        HELPER.forComponents(TFMGBlocks.COKE_OVEN)
                .addStoryBoard("coke_oven", MetallurgyScenes::cokeOven, TFMGPonderTags.METALLURGY);

        // Oil
        HELPER.forComponents(TFMGBlocks.PUMPJACK_BASE,TFMGBlocks.PUMPJACK_CRANK,TFMGBlocks.PUMPJACK_HAMMER)
                .addStoryBoard("pumpjack", OilScenes::pumpjack, TFMGPonderTags.OIL_PROCESSING);
        HELPER.forComponents(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER, TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
                .addStoryBoard("distillation_tower", OilScenes::distillationTower, TFMGPonderTags.OIL_PROCESSING);

        // Add our fluid manipulators to Create's ponder scenes
        HELPER.forComponents(
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getPipe(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getPipe(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getPipe(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getPipe())
                .addStoryBoard(Create.asResource("fluid_pipe/flow"), PipeScenes::flow, AllCreatePonderTags.FLUIDS)
                .addStoryBoard(Create.asResource("fluid_pipe/interaction"), PipeScenes::interaction)
                .addStoryBoard(Create.asResource("fluid_pipe/encasing"), PipeScenes::encasing);
        HELPER.forComponents(
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getPump(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPump(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getPump(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getPump(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getPump())
                .addStoryBoard(Create.asResource("mechanical_pump/flow"), PumpScenes::flow, AllCreatePonderTags.FLUIDS, AllCreatePonderTags.KINETIC_APPLIANCES)
                .addStoryBoard(Create.asResource("mechanical_pump/speed"), PumpScenes::speed);
        HELPER.forComponents(
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getValve(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getValve(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getValve(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getValve(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getValve())
                .addStoryBoard(Create.asResource("fluid_valve"), PipeScenes::valve, AllCreatePonderTags.FLUIDS, AllCreatePonderTags.KINETIC_APPLIANCES);
        HELPER.forComponents(
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getSmart(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getSmart(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getSmart(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getSmart(),
                TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getSmart())
                .addStoryBoard(Create.asResource("smart_pipe"), PipeScenes::smart, AllCreatePonderTags.FLUIDS);
    }
}
