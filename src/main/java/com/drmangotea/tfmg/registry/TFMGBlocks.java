package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.*;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.blocks.TFMGVanillaBlockStates;
import com.drmangotea.tfmg.base.dyes.DyedMaterialSetList;
import com.drmangotea.tfmg.config.server.TFMGStress;
import com.drmangotea.tfmg.content.electricity.CableConnectorGenerator;
import com.drmangotea.tfmg.content.decoration.*;
import com.drmangotea.tfmg.content.decoration.concrete.*;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.cogs.TFMGCogWheelBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.flywheels.TFMGFlywheelBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.gearbox.SteelGearboxBlock;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGTankGenerator;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumFluidTankModel;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumTankItem;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronFluidTankModel;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronTankItem;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelFluidTankModel;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankItem;
import com.drmangotea.tfmg.content.electricity.DiagonalCableBlock;
import com.drmangotea.tfmg.content.electricity.DiagonalCableGenerator;
import com.drmangotea.tfmg.content.electricity.content.devices.segmented_display.SegmentedDisplayBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.segmented_display.SegmentedDisplayCTBehavior;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.cable_hubs.CableHubBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.copycat_cable.CopycatCableBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.copycat_cable.CopycatCableBlockModel;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.three_phase_generator.ThreePhaseGeneratorBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.resistor.DebugResistorBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.electric_motor.ElectricMotorBlock;
import com.drmangotea.tfmg.content.electricity.content.lights.ElectricLightBlock;
import com.drmangotea.tfmg.content.electricity.content.lights.LampGenerator;
import com.drmangotea.tfmg.content.electricity.content.measuring_instruments.VoltMeterBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters.ElectricAdapterBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.CableBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_control_unit.AccumulatorControlUnitBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_rack.AccumulatorRackBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeCoilBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeElectromagneticCoilItem;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerGenerator;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small.TransformerBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.RealConnectorBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.creative_generator.CreativeGeneratorBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator.RotorBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator.StatorBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator.StatorGenerator;
import com.drmangotea.tfmg.content.electricity.content.lights.neon_tube.NeonTubeBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.electric_switch.ElectricSwitchBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.large_switch.LargeSwitchBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.large_switch.LargeSwitchGenerator;

import com.drmangotea.tfmg.content.electricity.content.devices.electric_pump.ElectricPumpBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.polarizer.PolarizerBlock;

import com.drmangotea.tfmg.content.electricity.content.devices.voltage_observer.VoltageObserverBlock;
import com.drmangotea.tfmg.content.electricity.content.devices.voltage_observer.VoltageObserverGenerator;
import com.drmangotea.tfmg.content.engines.base.EngineCTBehavior;
import com.drmangotea.tfmg.content.engines.base.EngineGenerator;
import com.drmangotea.tfmg.content.engines.engine_gearbox.EngineGearboxBlock;
import com.drmangotea.tfmg.content.engines.types.large_engine.LargeEngineBlock;
import com.drmangotea.tfmg.content.engines.types.radial_engine.RadialEngineBlock;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlock;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlock;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineGenerator;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.content.items.weapons.explosives.napalm.NapalmBombBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceHatchBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceOutputBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement.BlastFurnaceReinforcementBlockItem;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement.BlastFurnaceReinforcementWallBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.BlastStoveBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.BlastStoveItem;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.MultiblockCTBehavior;
import com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin.CastingBasinBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenCTBehavior;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenGenerator;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeBlock;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeGenerator;
import com.drmangotea.tfmg.content.machinery.misc.concrete_hose.ConcreteHoseBlock;
import com.drmangotea.tfmg.content.machinery.misc.exhaust.ExhaustBlock;
import com.drmangotea.tfmg.content.machinery.misc.firebox.FireboxBlock;
import com.drmangotea.tfmg.content.machinery.misc.firebox.FireboxGenerator;
import com.drmangotea.tfmg.content.machinery.misc.flarestack.FlarestackBlock;
import com.drmangotea.tfmg.content.machinery.misc.flarestack.FlarestackGenerator;
import com.drmangotea.tfmg.content.machinery.misc.gas_lamp.GasLampBlock;
import com.drmangotea.tfmg.content.machinery.misc.gas_lamp.GasLampGenerator;
import com.drmangotea.tfmg.content.machinery.misc.machine_input.MachineInputBlock;
import com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackBlock;
import com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackGenerator;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.IndustrialPipeBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationControllerBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.output.DistillationOutputBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base.PumpjackBaseBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.crank.PumpjackCrankBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.PumpjackBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.PumpjackGenerator;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.parts.PumpjackHammerConnectorBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.parts.PumpjackHammerHeadBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.parts.PumpjackHammerPartBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerConnectorBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerHeadBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerPartBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner.SurfaceScannerBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatGenerator;
import com.drmangotea.tfmg.content.machinery.vat.base.VatItem;
import com.drmangotea.tfmg.content.machinery.vat.compressor.CompressorBlock;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderBlock;
import com.drmangotea.tfmg.content.machinery.vat.freezer.FreezerBlock;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlock;
import com.drmangotea.tfmg.content.world.resevoir.OilDepositBlock;
import com.drmangotea.tfmg.recipes.condition.TagFilledCondition;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.contraptions.bearing.StabilizedBearingMovementBehaviour;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.TrainTrapdoorBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.fluids.tank.FluidTankMovementBehavior;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorGenerator;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.drmangotea.tfmg.base.TFMGBuilderTransformers.*;
import static com.drmangotea.tfmg.content.electricity.content.lights.ElectricLightBlock.LIGHT;
import static com.drmangotea.tfmg.registry.TFMGTags.Items;
import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.api.behaviour.display.DisplayTarget.displayTarget;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType.mountedFluidStorage;
import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;

@SuppressWarnings({"unused","removal"})
public class TFMGBlocks {
    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_MAIN);
    }

    public static final BlockEntry<LargeTransformerBlock> LARGE_TRANSFORMER =
            REGISTRATE.block("large_transformer", LargeTransformerBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .blockstate(new LargeTransformerGenerator()::generate)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<VoltMeterBlock> VOLTMETER =
            REGISTRATE.block("voltmeter", p->new VoltMeterBlock(p,true))
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<AccumulatorControlUnitBlock> ACCUMULATOR_CONTROL_UNIT =
            REGISTRATE.block("accumulator_control_unit", AccumulatorControlUnitBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<AccumulatorRackBlock> ACCUMULATOR_RACK =
            REGISTRATE.block("accumulator_rack", AccumulatorRackBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<VoltageObserverBlock> VOLTAGE_OBSERVER =
            REGISTRATE.block("voltage_observer", VoltageObserverBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new VoltageObserverGenerator()::generate)
                    .transform(displaySource(TFMGDisplaySources.CURRENT))
                    .transform(displaySource(TFMGDisplaySources.POWER_USAGE))
                    .transform(displaySource(TFMGDisplaySources.RESISTANCE))
                    .transform(displaySource(TFMGDisplaySources.VOLTAGE))
                    .transform(displaySource(TFMGDisplaySources.POWER_GENERATION))
                    .transform(displaySource(TFMGDisplaySources.VOLTAGE_GENERATION))
                    .transform(displaySource(TFMGDisplaySources.NETWORK_CONSUMPTION))
                    .transform(displaySource(TFMGDisplaySources.NETWORK_GENERATION))
                    .transform(displaySource(TFMGDisplaySources.NETWORK_ID))
                    .transform(displaySource(TFMGDisplaySources.NETWORK_RESISTANCE))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<VoltMeterBlock> AMMETER =
            REGISTRATE.block("ammeter", p->new VoltMeterBlock(p,false))
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricPumpBlock> ELECTRIC_PUMP =
            REGISTRATE.block("electric_pump", ElectricPumpBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<PolarizerBlock> POLARIZER =
            REGISTRATE.block("polarizer", PolarizerBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ThreePhaseGeneratorBlock> THREE_PHASE_GENERATOR =
            REGISTRATE.block("three_phase_generator", ThreePhaseGeneratorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .transform(TFMGStress.setImpact(50.0f))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item()
					.properties(p -> p.component(TFMGDataComponents.ENGINE_UPGRADE, new EngineUpgrade.Stored(TFMGEngineUpgrades.GENERATOR)))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricLightBlock> LIGHT_BULB =
            REGISTRATE.block("light_bulb", p -> new ElectricLightBlock(p, TFMGBlockEntities.ELECTRIC_LIGHT, TFMGShapes.LIGHT_BULB))
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricLightBlock> CIRCULAR_LIGHT =
            REGISTRATE.block("circular_light", p -> new ElectricLightBlock(p, TFMGBlockEntities.CIRCULAR_LIGHT, TFMGShapes.CIRCULAR_LIGHT))
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new LampGenerator()::generate)
                    .lang("Circular Lamp")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<ElectricLightBlock> MODERN_LIGHT =
            REGISTRATE.block("modern_light", p -> new ElectricLightBlock(p, TFMGBlockEntities.MODERN_LIGHT, TFMGShapes.MODERN_LIGHT))
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new LampGenerator()::generate)
                    .lang("Light Panel")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<ElectricLightBlock> ALUMINUM_LAMP =
            REGISTRATE.block("aluminum_lamp", p -> new ElectricLightBlock(p, TFMGBlockEntities.ALUMINUM_LAMP, TFMGShapes.ALUMINUM_LAMP))
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new LampGenerator()::generate)
                    .lang("Aluminum Lamp")
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<RealConnectorBlock> CABLE_CONNECTOR = REGISTRATE.block("cable_connector", RealConnectorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(new CableConnectorGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            //    .lang("Ceramic Insulator")
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CopycatCableBlock> COPYCAT_CABLE_BLOCK =
            REGISTRATE.block("copycat_cable_block", CopycatCableBlock::new)
                    .transform(TFMGBuilderTransformers.copycatCable())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatCableBlockModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<RealConnectorBlock> GLASS_CABLE_CONNECTOR = REGISTRATE.block("glass_cable_connector", RealConnectorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(new CableConnectorGenerator()::generate)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            //  .lang("Glass Insulator")
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ElectricAdapterBlock> TWO_SLOT_ADAPTER = REGISTRATE.block("two_slot_cable_adapter", p -> new ElectricAdapterBlock(p, 2))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ElectricAdapterBlock> FOUR_SLOT_ADAPTER = REGISTRATE.block("four_slot_cable_adapter", p -> new ElectricAdapterBlock(p, 4))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DebugResistorBlock> RESISTOR = REGISTRATE.block("resistor", DebugResistorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new CreativeMotorGenerator()::generate)
            //.item(ResistorBlockItem::new)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CableBlock> CABLE = REGISTRATE.block("universal_cable", CableBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .register();

    //------------------ENGINES------------------//
    public static final BlockEntry<TurbineEngineBlock> TURBINE_ENGINE = REGISTRATE.block("turbine_engine", TurbineEngineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(TFMGStress.setCapacity(100))
            .transform(pickaxeOnly())
            .blockstate(new TurbineEngineGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<RegularEngineBlock> REGULAR_ENGINE = REGISTRATE.block("regular_engine", RegularEngineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(connectedTextures(() -> new EngineCTBehavior(TFMGSpriteShifts.REGULAR_ENGINE_TOP, TFMGSpriteShifts.REGULAR_ENGINE_BOTTOM, TFMGSpriteShifts.REGULAR_ENGINE_SIDE)))
            .blockstate(new EngineGenerator()::generate)
            .transform(TFMGStress.setCapacity(100))
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<RadialEngineBlock> RADIAL_ENGINE = REGISTRATE.block("radial_engine", RadialEngineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TFMGStress.setCapacity(100))
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new EngineCTBehavior(TFMGSpriteShifts.REGULAR_ENGINE_TOP, TFMGSpriteShifts.REGULAR_ENGINE_BOTTOM, TFMGSpriteShifts.REGULAR_ENGINE_SIDE)))
            .blockstate(new TurbineEngineGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<LargeEngineBlock> LARGE_ENGINE = REGISTRATE.block("large_engine", p -> new LargeEngineBlock(p, TFMGTags.EngineFuel.LARGE_ENGINE.tag))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TFMGStress.setCapacity(55))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.directionalBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .item().transform(customItemModel())
            .register();
    public static final BlockEntry<LargeEngineBlock> SIMPLE_LARGE_ENGINE = REGISTRATE.block("simple_large_engine", p -> new LargeEngineBlock(p, TFMGTags.EngineFuel.SIMPLE_LARGE_ENGINE.tag))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TFMGStress.setCapacity(40))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.directionalBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .item().transform(customItemModel())
            .register();

    public static final BlockEntry<EngineGearboxBlock> ENGINE_GEARBOX = REGISTRATE.block("engine_gearbox", EngineGearboxBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(pickaxeOnly())
            .item().transform(customItemModel())
            .register();


    //------------------TANKS------------------//
    public static final BlockEntry<AluminumTankBlock> ALUMINUM_FLUID_TANK =
            REGISTRATE.block("aluminum_fluid_tank", AluminumTankBlock::regular)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.sound(SoundType.COPPER))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .transform(mountedFluidStorage(TFMGMountedStorageTypes.TFMG_FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .blockstate(new TFMGTankGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> AluminumFluidTankModel::standard))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(AluminumTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();
    public static final BlockEntry<CastIronTankBlock> CAST_IRON_FLUID_TANK =
            REGISTRATE.block("cast_iron_fluid_tank", CastIronTankBlock::regular)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.sound(SoundType.METAL))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(mountedFluidStorage(TFMGMountedStorageTypes.TFMG_FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new TFMGTankGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> CastIronFluidTankModel::standard))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(CastIronTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();

    //------------------DISTILLATION_TOWER------------------//
    public static final BlockEntry<SteelTankBlock> STEEL_FLUID_TANK =
            REGISTRATE.block("steel_fluid_tank", SteelTankBlock::regular)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new TFMGTankGenerator()::generate)
                    .transform(mountedFluidStorage(TFMGMountedStorageTypes.TFMG_FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::standard))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(SteelTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();

    public static final BlockEntry<DistillationOutputBlock> STEEL_DISTILLATION_OUTPUT =
            REGISTRATE.block("steel_distillation_output", DistillationOutputBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<DistillationControllerBlock> STEEL_DISTILLATION_CONTROLLER =
            REGISTRATE.block("steel_distillation_controller", DistillationControllerBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<IndustrialPipeBlock> INDUSTRIAL_PIPE = REGISTRATE.block("industrial_pipe", IndustrialPipeBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(p -> p.sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .tag(TFMGTags.Blocks.INDUSTRIAL_PIPE.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.STEEL.ingots), RecipeCategory.BUILDING_BLOCKS, c, 8))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .properties(p -> p.component(TFMGDataComponents.ENGINE_UPGRADE, new EngineUpgrade.Stored(TFMGEngineUpgrades.PIPING)))
            .build()
            .register();
    public static final BlockEntry<Block> CONCRETE_ENCASED_INDUSTRIAL_PIPE = REGISTRATE.block("concrete_encased_industrial_pipe", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.requiresCorrectToolForDrops().noOcclusion())
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .transform(pickaxeOnly())
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.INDUSTRIAL_PIPE.get().asItem()))
            .tag(TFMGTags.Blocks.INDUSTRIAL_PIPE.tag)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .register();

    //------------------PUMPJACK------------------//
    public static final BlockEntry<PumpjackBlock> PUMPJACK_HAMMER =
            REGISTRATE.block("pumpjack_hammer", PumpjackBlock::new)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new PumpjackGenerator()::generate)
                    .onRegister(movementBehaviour(new StabilizedBearingMovementBehaviour()))
                    .item()
                    .transform(customItemModel())
                    .lang("Pumpjack Hammer Holder")
                    .register();


    public static final BlockEntry<PumpjackCrankBlock> PUMPJACK_CRANK =
            REGISTRATE.block("pumpjack_crank", PumpjackCrankBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .build()
                    .lang("Pumpjack Crank")
                    .register();

    public static final BlockEntry<PumpjackHammerPartBlock> PUMPJACK_HAMMER_PART = REGISTRATE.block("pumpjack_hammer_part", PumpjackHammerPartBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.Blocks.PUMPJACK_SMALL_PART.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.STEEL.storageBlocks.items()),
                    RecipeCategory.DECORATIONS, c, 2))
            .simpleItem()
            .register();
    public static final BlockEntry<PumpjackHammerHeadBlock> PUMPJACK_HAMMER_HEAD = REGISTRATE.block("pumpjack_hammer_head", PumpjackHammerHeadBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.Blocks.PUMPJACK_HEAD.tag)
            .simpleItem()
            .register();

    public static final BlockEntry<PumpjackHammerConnectorBlock> PUMPJACK_HAMMER_CONNECTOR = REGISTRATE.block("pumpjack_hammer_connector", PumpjackHammerConnectorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.Blocks.PUMPJACK_CONNECTOR.tag)
            .simpleItem()
            .register();
    public static final BlockEntry<LargePumpjackHammerPartBlock> LARGE_PUMPJACK_HAMMER_PART = REGISTRATE.block("large_pumpjack_hammer_part", LargePumpjackHammerPartBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.Blocks.PUMPJACK_PART.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.STEEL.storageBlocks.items()),
                    RecipeCategory.DECORATIONS, c, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<LargePumpjackHammerHeadBlock> LARGE_PUMPJACK_HAMMER_HEAD = REGISTRATE.block("large_pumpjack_hammer_head", LargePumpjackHammerHeadBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.Blocks.PUMPJACK_HEAD.tag)
            .simpleItem()
            .register();
    public static final BlockEntry<LargePumpjackHammerConnectorBlock> LARGE_PUMPJACK_HAMMER_CONNECTOR = REGISTRATE.block("large_pumpjack_hammer_connector", LargePumpjackHammerConnectorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.Blocks.PUMPJACK_CONNECTOR.tag)
            .simpleItem()
            .register();
    public static final BlockEntry<PumpjackBaseBlock> PUMPJACK_BASE = REGISTRATE.block("pumpjack_base", PumpjackBaseBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .simpleItem()
            .register();
    public static final BlockEntry<OilDepositBlock> OIL_DEPOSIT = REGISTRATE.block("oil_deposit", OilDepositBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.BEDROCK)
            .properties(p -> p)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .tag(TFMGTags.Blocks.SURFACE_SCANNER_FINDABLE.tag)
            .tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
            .tag(BlockTags.INFINIBURN_OVERWORLD, BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS)
            .tag(BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .simpleItem()
            .register();
    //------------------VAT_MACHINES------------------//
    public static final BlockEntry<VatBlock> STEEL_CHEMICAL_VAT =
            REGISTRATE.block("steel_chemical_vat", VatBlock::steel)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new VatGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::steelVat))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(VatItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();

    public static final BlockEntry<VatBlock> CAST_IRON_CHEMICAL_VAT =
            REGISTRATE.block("cast_iron_chemical_vat", VatBlock::castIron)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new VatGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::castIronVat))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(VatItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();

    public static final BlockEntry<VatBlock> FIREPROOF_CHEMICAL_VAT =
            REGISTRATE.block("fireproof_chemical_vat", VatBlock::fireproof)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new VatGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::fireproofVat))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(VatItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single"))
                    .build()
                    .register();

    public static final BlockEntry<IndustrialMixerBlock> INDUSTRIAL_MIXER = REGISTRATE.block("industrial_mixer", IndustrialMixerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .transform(TFMGStress.setImpact(3.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<ElectrodeHolderBlock> ELECTRODE_HOLDER = REGISTRATE.block("electrode_holder", ElectrodeHolderBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<FreezerBlock> FREEZER = REGISTRATE.block("freezer", FreezerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CompressorBlock> COMPRESSOR = REGISTRATE.block("compressor", CompressorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            // .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .transform(TFMGStress.setImpact(24.0))
            .item()
            .transform(customItemModel())
            .register();


    //------------------ORES------------------//
    public static final BlockEntry<Block> LEAD_ORE = REGISTRATE.block("lead_ore", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LEAD.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.ORES, Tags.Items.ORES,
                    CommonMetal.LEAD.ores.blocks(), CommonMetal.LEAD.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE
            )))
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_LEAD_ORE = REGISTRATE.block("deepslate_lead_ore", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LEAD.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.ORES, Tags.Items.ORES,
                    CommonMetal.LEAD.ores.blocks(), CommonMetal.LEAD.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE
            )))
            .build()
            .register();

    public static final BlockEntry<Block> NICKEL_ORE = REGISTRATE.block("nickel_ore", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_NICKEL.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.ORES, Tags.Items.ORES,
                    CommonMetal.NICKEL.ores.blocks(), CommonMetal.NICKEL.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE
            )))
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_NICKEL_ORE = REGISTRATE.block("deepslate_nickel_ore", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_NICKEL.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.ORES, Tags.Items.ORES,
                    CommonMetal.NICKEL.ores.blocks(), CommonMetal.NICKEL.ores.items(),
                    Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE
            )))
            .build()
            .register();

    public static final BlockEntry<Block> LITHIUM_ORE = REGISTRATE.block("lithium_ore", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LITHIUM.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.ORES, Tags.Items.ORES,
                    TFMGTags.Blocks.ORES_LITHIUM.tag, Items.ORES_LITHIUM.tag,
                    Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE
            )))
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_LITHIUM_ORE = REGISTRATE.block("deepslate_lithium_ore", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);

                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LITHIUM.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.ORES, Tags.Items.ORES,
                    TFMGTags.Blocks.ORES_LITHIUM.tag, Items.ORES_LITHIUM.tag,
                    Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE
            )))
            .build()
            .register();
    public static final BlockEntry<Block> SULFUR = REGISTRATE.block("sulfur", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.CALCITE)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();
    public static final BlockEntry<Block> LIGNITE = REGISTRATE.block("lignite", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.CALCITE)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();
    public static final BlockEntry<Block> FIRECLAY = REGISTRATE.block("fireclay", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.CLAY)
            .tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .loot((p, b) -> p
                    .add(b, p.createSingleItemTable(TFMGItems.FIRECLAY_BALL.get())))
            .simpleItem()
            .register();
    //------------------MISC------------------//
    public static final BlockEntry<Block> FOSSILSTONE = REGISTRATE.block("fossilstone", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.OBSIDIAN)
            .properties(p -> p.strength(100f, 1200f))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();
    public static final BlockEntry<Block> SLAG_BLOCK =
            REGISTRATE.block("slag_block", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CALCITE))
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .lang("Block of Slag")
                    .register();
    public static final BlockEntry<Block> RAW_NICKEL_BLOCK = REGISTRATE.block("raw_nickel_block", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.RAW_GOLD_BLOCK)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .lang("Block of Raw Nickel")
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.NICKEL.rawStorageBlocks.blocks(), CommonMetal.NICKEL.rawStorageBlocks.items()
            )))
            .build()
            .register();
    public static final BlockEntry<Block> RAW_LEAD_BLOCK = REGISTRATE.block("raw_lead_block", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.RAW_GOLD_BLOCK)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .lang("Block of Raw Lead")
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.LEAD.rawStorageBlocks.blocks(), CommonMetal.LEAD.rawStorageBlocks.items()
            )))
            .build()
            .register();
    public static final BlockEntry<Block> RAW_LITHIUM_BLOCK = REGISTRATE.block("raw_lithium_block", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.RAW_GOLD_BLOCK)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .lang("Block of Raw Lithium")
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    TFMGTags.Blocks.STORAGE_BLOCKS_RAW_LITHIUM.tag, Items.STORAGE_BLOCKS_RAW_LITHIUM.tag
            )))
            .build()
            .register();

    //------------------MISC_MACHINERY------------------//
    public static final BlockEntry<AirIntakeBlock> AIR_INTAKE = REGISTRATE.block("air_intake", AirIntakeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new AirIntakeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<FireboxBlock> FIREBOX =
            REGISTRATE.block("firebox", FireboxBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.NETHER_BRICKS)
                            .lightLevel(FireboxBlock::getLight))
                    .transform(pickaxeOnly())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new MultiblockCTBehavior(null, TFMGSpriteShifts.FIREBOX_TOP)))
                    .blockstate(new FireboxGenerator()::generate)
                    .item(SteelTankItem::new)
                    .build()
                    .register();

    public static final BlockEntry<WindingMachineBlock> WINDING_MACHINE = REGISTRATE.block("winding_machine", WindingMachineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .transform(TFMGStress.setImpact(4.0))
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SurfaceScannerBlock> SURFACE_SCANNER = REGISTRATE.block("surface_scanner", SurfaceScannerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::translucent)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<MachineInputBlock> MACHINE_INPUT =
            REGISTRATE.block("machine_input", MachineInputBlock::new)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p
                            .strength(4.5F))
                    .transform(axeOrPickaxe())
                    .transform(TFMGStress.setImpact(4.0))
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<ConcreteHoseBlock> CONCRETE_HOSE = REGISTRATE.block("concrete_hose", ConcreteHoseBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(TFMGStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();


    //------------------METALLURGY------------------//
    public static final BlockEntry<BlastFurnaceOutputBlock> BLAST_FURNACE_OUTPUT = REGISTRATE.block("blast_furnace_output", BlastFurnaceOutputBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.NETHER_BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BlastFurnaceHatchBlock> BLAST_FURNACE_HATCH = REGISTRATE.block("blast_furnace_hatch", BlastFurnaceHatchBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.NETHER_BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .tag(TFMGTags.Blocks.BLAST_FURNACE_WALL.tag)
            .tag(TFMGTags.Blocks.REINFORCED_BLAST_FURNACE_WALL.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();
    public static final BlockEntry<Block> FIREPROOF_BRICKS = REGISTRATE.block("fireproof_bricks", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.NETHER_BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .tag(TFMGTags.Blocks.BLAST_FURNACE_WALL.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();

    public static final BlockEntry<Block> REINFORCED_FIREPROOF_BRICKS = REGISTRATE.block("reinforced_fireproof_bricks", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.NETHER_BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .tag(TFMGTags.Blocks.REINFORCED_BLAST_FURNACE_WALL.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .blockstate(simpleCubeAll("fireproof_bricks"))
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.FIREPROOF_BRICKS.get().asItem()))
            .register();

    public static final BlockEntry<Block> BLAST_FURNACE_REINFORCEMENT = REGISTRATE.block("blast_furnace_reinforcement", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.BLAST_FURNACE_REINFORCEMENT)))
            .tag(TFMGTags.Blocks.REINFORCED_BLAST_FURNACE_SUPPORT.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item(BlastFurnaceReinforcementBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<BlastFurnaceReinforcementWallBlock> BLAST_FURNACE_REINFORCEMENT_WALL = REGISTRATE.block("blast_furnace_reinforcement_wall", BlastFurnaceReinforcementWallBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.BLAST_FURNACE_REINFORCEMENT)))
            .tag(BlockTags.NEEDS_STONE_TOOL, TFMGTags.Blocks.NON_DIAGONAL_WALLS.tag)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.BLAST_FURNACE_REINFORCEMENT.get().asItem()))
            .register();
    //
    public static final BlockEntry<Block> RUSTED_BLAST_FURNACE_REINFORCEMENT = REGISTRATE.block("rusted_blast_furnace_reinforcement", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.RUSTED_BLAST_FURNACE_REINFORCEMENT)))
            .tag(TFMGTags.Blocks.REINFORCED_BLAST_FURNACE_SUPPORT.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item(BlastFurnaceReinforcementBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<BlastFurnaceReinforcementWallBlock> RUSTED_BLAST_FURNACE_REINFORCEMENT_WALL = REGISTRATE.block("rusted_blast_furnace_reinforcement_wall", BlastFurnaceReinforcementWallBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.RUSTED_BLAST_FURNACE_REINFORCEMENT)))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.RUSTED_BLAST_FURNACE_REINFORCEMENT.get().asItem()))
            .register();


    public static final BlockEntry<WallBlock> FIREPROOF_BRICK_REINFORCEMENT = REGISTRATE.block("fireproof_brick_reinforcement", WallBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.NETHER_BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .tag(BlockTags.WALLS)
            .tag(TFMGTags.Blocks.BLAST_FURNACE_SUPPORT.tag)
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "fireproof_brick_reinforcement"))
            .item()
            .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "fireproof_brick_reinforcement"))
            .build()
            .register();

    public static final BlockEntry<CokeOvenBlock> COKE_OVEN = REGISTRATE.block("coke_oven", CokeOvenBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .blockstate(new CokeOvenGenerator()::generate)
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(CokeOvenCTBehavior::new))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BlastStoveBlock> BLAST_STOVE =
            REGISTRATE.block("blast_stove", BlastStoveBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new MultiblockCTBehavior(TFMGSpriteShifts.BLAST_STOVE_SIDE, TFMGSpriteShifts.BLAST_STOVE_TOP)))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(BlastStoveItem::new)
                    .build()
                    .register();

    public static final BlockEntry<CastingBasinBlock> CASTING_BASIN = REGISTRATE.block("casting_basin", CastingBasinBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.requiresCorrectToolForDrops().noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    //------------------GADGETS------------------//

    public static final BlockEntry<NapalmBombBlock> NAPALM_BOMB = REGISTRATE.block("napalm_bomb", NapalmBombBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .simpleItem()
            .register();
	
	//------------------CASINGS------------------//
	
	public static final BlockEntry<CasingBlock> STEEL_CASING = REGISTRATE.block("steel_casing", CasingBlock::new)
		.transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.STEEL_CASING))
		.register();
	public static final BlockEntry<CasingBlock> HEAVY_MACHINERY_CASING = REGISTRATE.block("heavy_machinery_casing", CasingBlock::new)
		.tag(TFMGTags.Blocks.SURFACE_SCANNER_FINDABLE.tag)
		.transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
		.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
		.register();
	public static final BlockEntry<CasingBlock> ALUMINUM_CASING = REGISTRATE.block("industrial_aluminum_casing", CasingBlock::new)
		.transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING))
		.properties(p -> p.sound(SoundType.COPPER))
		.lang("Aluminum Casing")
		.register();

    //------------------ELECTRICITY------------------//

    //public static final BlockEntry<GeneratorBlock> GENERATOR =
    //        REGISTRATE.block("generator", GeneratorBlock::new)
    //                .initialProperties(SharedProperties::softMetal)
    //                .transform(pickaxeOnly())
    //                .properties(BlockBehaviour.Properties::noOcclusion)
    //                .transform(TFMGStress.setImpact(50.0f))
    //                .blockstate(BlockStateGen.directionalBlockProvider(true))
    //                .item()
    //                .transform(customItemModel())
    //                .register();

    //public static final BlockEntry<CableConnectorBlock> CABLE_CONNECTOR =
    //        REGISTRATE.block("cable_connector", CableConnectorBlock::new)
    //                .initialProperties(() -> Blocks.TERRACOTTA)
    //                .transform(pickaxeOnly())
    //                .addLayer(() -> RenderType::cutoutMipped)
    //                .properties(BlockBehaviour.Properties::noOcclusion)
    //                .blockstate(new CableConnectorGenerator()::generate)
    //                .lang("Cable Insulator")
    //                .item()
    //                .transform(customItemModel())
    //                .register();
//
    //public static final BlockEntry<CableConnectorBlock> GLASS_CABLE_CONNECTOR =
    //        REGISTRATE.block("glass_cable_insulator", CableConnectorBlock::new)
    //                .initialProperties(() -> Blocks.TERRACOTTA)
    //                .transform(pickaxeOnly())
    //                .properties(BlockBehaviour.Properties::noOcclusion)
    //                .addLayer(() -> RenderType::translucent)
    //                .blockstate(new CableConnectorGenerator()::generate)
    //                .lang("Glass Cable Insulator")
    //                .item()
    //                .transform(customItemModel())
    //                .register();

    //public static final BlockEntry<ResistorBlock> RESISTOR =
    //        REGISTRATE.block("resistor", ResistorBlock::new)
    //                .initialProperties(SharedProperties::softMetal)
    //                .transform(pickaxeOnly())
    //                .properties(BlockBehaviour.Properties::noOcclusion)
    //                .addLayer(() -> RenderType::cutoutMipped)
    //                .blockstate(BlockStateGen.directionalBlockProvider(true))
    //                .item(ResistorBlockItem::new)
    //                .transform(customItemModel())
    //                .register();


    public static final BlockEntry<Block> COPYCAT_CABLE_BASE = REGISTRATE.block("copycat_cable_base", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .addLayer(() -> RenderType::cutoutMipped)
            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .register();
    public static final BlockEntry<CableHubBlock> BRASS_CABLE_HUB =
            REGISTRATE.block("brass_cable_hub", CableHubBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> COPPER_CABLE_HUB =
            REGISTRATE.block("copper_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.COPPER_BLOCK)
                    .properties(p -> p.sound(SoundType.COPPER))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> STEEL_CABLE_HUB =
            REGISTRATE.block("steel_cable_hub", CableHubBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> ALUMINUM_CABLE_HUB =
            REGISTRATE.block("aluminum_cable_hub", p -> new CableHubBlock(p, ALUMINUM_CASING::get))
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.ALUMINUM_CASING_CABLE_HUB)))
                    .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING)))
                    .simpleItem()
                    .register();
    public static final BlockEntry<CableHubBlock> STEEL_CASING_CABLE_HUB =
            REGISTRATE.block("steel_casing_cable_hub", p -> new CableHubBlock(p, STEEL_CASING::get))
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_CASING_CABLE_HUB)))
                    .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_CASING)))
                    .simpleItem()
                    .register();
    public static final BlockEntry<CableHubBlock> HEAVY_CABLE_HUB =
            REGISTRATE.block("heavy_cable_hub", p -> new CableHubBlock(p, HEAVY_MACHINERY_CASING::get))
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING_CABLE_HUB)))

                    .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.HEAVY_MACHINERY_CASING)))
					.simpleItem()
                    .register();

    public static final BlockEntry<DiagonalCableBlock> DIAGONAL_CABLE_BLOCK =
            REGISTRATE.block("diagonal_cable_block", DiagonalCableBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new DiagonalCableGenerator()::generate)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricMotorBlock> ELECTRIC_MOTOR =
            REGISTRATE.block("electric_motor", ElectricMotorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CreativeMotorGenerator()::generate)
                    .transform(TFMGStress.setCapacity(12))
                    .onRegister(BlockStressValues.setGeneratorSpeed(64, true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricMotorBlock> HEAVY_ELECTRIC_MOTOR =
            REGISTRATE.block("heavy_electric_motor", ElectricMotorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CreativeMotorGenerator()::generate)
                    .transform(TFMGStress.setCapacity(100))
                    .onRegister(BlockStressValues.setGeneratorSpeed(64, true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<CreativeGeneratorBlock> CREATIVE_GENERATOR = REGISTRATE.block("creative_generator", CreativeGeneratorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .simpleItem()
            .register();



    public static final BlockEntry<NeonTubeBlock> NEON_TUBE =
            REGISTRATE.block("neon_tube", NeonTubeBlock::new)
                    .initialProperties(() -> net.minecraft.world.level.block.Blocks.GLASS)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(TFMGBuilderTransformers::generateNeonTubeBlockState)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricSwitchBlock> ELECTRICAL_SWITCH =
            REGISTRATE.block("electrical_switch", ElectricSwitchBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .lang("Electric Switch")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<LargeSwitchBlock> LARGE_SWITCH =
            REGISTRATE.block("large_switch", LargeSwitchBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .blockstate(new LargeSwitchGenerator()::generate)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item()
                    .transform(customItemModel())
                    .register();





    public static final BlockEntry<RotorBlock> ROTOR =
            REGISTRATE.block("rotor", RotorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.axisBlockProvider(true))
                    .transform(TFMGStress.setImpact(240))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<StatorBlock> STATOR =
            REGISTRATE.block("stator", StatorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new StatorGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();
    /*public static final BlockEntry<TrafficLightBlock> TRAFFIC_LIGHT =
            REGISTRATE.block("traffic_light", TrafficLightBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();*/
    public static final BlockEntry<SegmentedDisplayBlock> SEGMENTED_DISPLAY =
            REGISTRATE.block("segmented_display", SegmentedDisplayBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(SegmentedDisplayCTBehavior::new))
					.onRegister(casingConnectivity((b,cc) -> cc.make(
						b, TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
						(s, f) -> s.getValue(SegmentedDisplayBlock.FACING) != f
					)))
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
					.transform(displaySource(TFMGDisplaySources.SEGMENTED_DISPLAY))
					.transform(displayTarget(TFMGDisplayTargets.SEGMENTED_DISPLAY))
                    .item().transform(customItemModel())
                    .register();

    public static final BlockEntry<TransformerBlock> TRANSFORMER =
            REGISTRATE.block("transformer", TransformerBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .transform(customItemModel())
                    .register();



    public static final BlockEntry<LargeCoilBlock> LARGE_COIL =
            REGISTRATE.block("large_coil", LargeCoilBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .transform(pickaxeOnly())
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item(LargeElectromagneticCoilItem::new)
                    .transform(customItemModel())
                    .register();

    //------------------EXHAUST/WASTE_REMOVAL------------------//
    public static final BlockEntry<SmokestackBlock> BRICK_SMOKESTACK = REGISTRATE.block("brick_smokestack", SmokestackBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.BRICKS)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(new SmokestackGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SmokestackBlock> METAL_SMOKESTACK = REGISTRATE.block("metal_smokestack", SmokestackBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(new SmokestackGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SmokestackBlock> CONCRETE_SMOKESTACK = REGISTRATE.block("concrete_smokestack", SmokestackBlock::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.STONE)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(new SmokestackGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ExhaustBlock> EXHAUST =
            REGISTRATE.block("exhaust", ExhaustBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<FlarestackBlock> FLARESTACK =
            REGISTRATE.block("flarestack", FlarestackBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p
                            .lightLevel(s -> s.getValue(FlarestackBlock.LIT) ? 15 : 0)
                            .noOcclusion())
                    .blockstate(new FlarestackGenerator()::generate)
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<GasLampBlock> GAS_LAMP =
            REGISTRATE.block("gas_lamp", GasLampBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p
                            .lightLevel(s -> s.getValue(GasLampBlock.LIT) ? 15 : 0)
                            .noOcclusion())
                    .blockstate(new GasLampGenerator()::generate)
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    //------------------COGWHEELS------------------//

    public static final BlockEntry<TFMGCogWheelBlock>
		STEEL_COGWHEEL = cogwheel("steel_cogwheel", false, CommonMetal.STEEL.ingots)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).register(),
		LARGE_STEEL_COGWHEEL = cogwheel("large_steel_cogwheel", true, CommonMetal.STEEL.ingots)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).register(),
		ALUMINUM_COGWHEEL = cogwheel("aluminum_cogwheel", false, CommonMetal.ALUMINUM.ingots)
			.properties(p -> p.sound(SoundType.COPPER)).addLayer(() -> RenderType::cutoutMipped).register(),
		LARGE_ALUMINUM_COGWHEEL = cogwheel("large_aluminum_cogwheel", true, CommonMetal.ALUMINUM.ingots)
			.properties(p -> p.sound(SoundType.COPPER)).addLayer(() -> RenderType::cutoutMipped).register();
	
    //------------------STORAGE_BLOCKS------------------//
    public static final BlockEntry<Block> STEEL_BLOCK = REGISTRATE.block("steel_block", Block::new)
            .initialProperties(SharedProperties::softMetal)

            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(TFMGTags.Blocks.REINFORCED_BLAST_FURNACE_WALL.tag)
            .tag(TFMGTags.Blocks.REINFORCED_BLAST_FURNACE_SUPPORT.tag)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.STEEL.storageBlocks.blocks(), CommonMetal.STEEL.storageBlocks.items()
            )))
            .build()
            .lang("Block of Steel")
            .register();
    public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE.block("cast_iron_block", Block::new)
            .initialProperties(SharedProperties::softMetal)

            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .blockstate(simpleCubeAll("cast_iron_block"))
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    TFMGTags.Blocks.STORAGE_BLOCKS_CAST_IRON.tag, Items.STORAGE_BLOCKS_CAST_IRON.tag
            )))
            .build()
            .lang("Block of Cast Iron")
            .register();

    public static final BlockEntry<Block> ALUMINUM_BLOCK = REGISTRATE.block("aluminum_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("aluminum_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.ALUMINUM.storageBlocks.blocks(), CommonMetal.ALUMINUM.storageBlocks.items()
            )))
            .build()
            .lang("Block of Aluminum")
            .register();
    public static final BlockEntry<Block> PLASTIC_BLOCK = REGISTRATE.block("plastic_block", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.QUARTZ_BLOCK)

            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("plastic_block"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    TFMGTags.Blocks.STORAGE_BLOCKS_PLASTIC.tag, Items.STORAGE_BLOCKS_PLASTIC.tag
            )))
            .build()
            .lang("Block of Plastic")
            .register();

    public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("lead_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.LEAD.storageBlocks.blocks(), CommonMetal.LEAD.storageBlocks.items()
            )))
            .build()
            .lang("Block of Lead")
            .register();

    public static final BlockEntry<Block> CONSTANTAN_BLOCK = REGISTRATE.block("constantan_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("constantan_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.CONSTANTAN.storageBlocks.blocks(), CommonMetal.CONSTANTAN.storageBlocks.items()
            )))
            .build()
            .lang("Block of Constantan")
            .register();

    public static final BlockEntry<Block> NICKEL_BLOCK = REGISTRATE.block("nickel_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.NICKEL_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.NICKEL_BLOCK)))
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    CommonMetal.NICKEL.storageBlocks.blocks(), CommonMetal.NICKEL.storageBlocks.items()
            )))
            .build()
            .lang("Block of Nickel")
            .register();

    public static final BlockEntry<LithiumBlock> LITHIUM_BLOCK = REGISTRATE.block("lithium_block", LithiumBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("lithium_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(Map.of(
                    Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS,
                    TFMGTags.Blocks.STORAGE_BLOCKS_LITHIUM.tag, Items.STORAGE_BLOCKS_LITHIUM.tag
            )))
            .build()
            .lang("Block of Lithium")
            .register();


    public static final BlockEntry<Block> COAL_COKE_BLOCK = REGISTRATE.block("coal_coke_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("coal_coke_block"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(TFMGTags.Blocks.STORAGE_BLOCKS_COAL_COKE.tag)
            .item()
            .tag(Tags.Items.STORAGE_BLOCKS)
            .tag(Items.STORAGE_BLOCKS_COAL_COKE.tag)
            .build()
            .lang("Block of Coal Coke")
            .register();

    public static final BlockEntry<Block> LAMINATED_MAGNETIC_ALLOY_BLOCK = REGISTRATE.block("laminated_magnetic_alloy_block", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .blockstate(simpleCubeAll("laminated_magnetic_alloy_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item().build()
			.recipe((ctx, p) -> p.square(
				DataIngredient.ingredient(Ingredient.of(TFMGItems.MAGNETIC_ALLOY_SHEET), TFMGItems.MAGNETIC_ALLOY_SHEET),
				RecipeCategory.MISC,
				ctx, false
			))
            .register();

    public static final BlockEntry<ColoredFallingBlock> CEMENT = REGISTRATE.block("cement", p -> new ColoredFallingBlock(new ColorRGBA(-8356741), p))
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.CLAY)
            .tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .simpleItem()
            .register();

    //------------------DOOR------------------//
    public static final BlockEntry<TFMGSlidingDoorBlock> HEAVY_CASING_DOOR =
            REGISTRATE.block("heavy_casing_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), false))
                    .transform(TFMGBuilderTransformers.slidingDoor("heavy_casing"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();
    public static final BlockEntry<TFMGSlidingDoorBlock> STEEL_CASING_DOOR =
            REGISTRATE.block("steel_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), true))
                    .transform(TFMGBuilderTransformers.slidingDoor("steel"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();

    public static final BlockEntry<TFMGSlidingDoorBlock> ALUMINUM_DOOR =
            REGISTRATE.block("aluminum_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), false))
                    .transform(TFMGBuilderTransformers.slidingDoor("aluminum"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();
    public static final BlockEntry<TFMGSlidingDoorBlock> HEAVY_PLATED_DOOR =
            REGISTRATE.block("heavy_plated_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), false))
                    .transform(TFMGBuilderTransformers.slidingDoor("heavy_plated"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();


    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_DECORATION);
    }

    //------------------GEARBOXES------------------//
    public static final BlockEntry<SteelGearboxBlock> STEEL_GEARBOX = REGISTRATE.block("steel_gearbox", SteelGearboxBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TFMGStress.setNoImpact())
            .transform(axeOrPickaxe())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                    (s, f) -> f.getAxis() == s.getValue(GearboxBlock.AXIS))))
            .blockstate((c, p) -> axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p), true))
            .item()
            .transform(customItemModel())
            .register();

    //------------------BARS------------------//
    public static final BlockEntry<IronBarsBlock> STEEL_BARS = TFMGMetalBarsGen.createBars("steel", true,
            () -> DataIngredient.tag(CommonMetal.STEEL.ingots), MapColor.TERRACOTTA_CYAN);
    public static final BlockEntry<IronBarsBlock> ALUMINUM_BARS = TFMGMetalBarsGen.createBars("aluminum", true,
            () -> DataIngredient.tag(CommonMetal.ALUMINUM.ingots), MapColor.TERRACOTTA_WHITE);

    public static final BlockEntry<IronBarsBlock> CAST_IRON_BARS = TFMGMetalBarsGen.createBars("cast_iron", true,
            () -> DataIngredient.tag(Items.INGOTS_CAST_IRON.tag), MapColor.COLOR_BLACK);

    public static final BlockEntry<IronBarsBlock> LEAD_BARS = TFMGMetalBarsGen.createBars("lead", true,
            () -> DataIngredient.tag(CommonMetal.LEAD.ingots), MapColor.TERRACOTTA_BLUE);

    public static final BlockEntry<IronBarsBlock> NICKEL_BARS = TFMGMetalBarsGen.createBars("nickel", true,
            () -> DataIngredient.tag(CommonMetal.NICKEL.ingots), MapColor.TERRACOTTA_YELLOW);

    //------------------LADDERS------------------//
    public static final BlockEntry<MetalLadderBlock> STEEL_LADDER =
            REGISTRATE.block("steel_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("steel",
                            () -> DataIngredient.tag(CommonMetal.STEEL.ingots), MapColor.TERRACOTTA_CYAN))
                    .register();
    public static final BlockEntry<MetalLadderBlock> ALUMINUM_LADDER =
            REGISTRATE.block("aluminum_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("aluminum",
                            () -> DataIngredient.tag(CommonMetal.ALUMINUM.ingots), MapColor.TERRACOTTA_WHITE))
                    .register();

    public static final BlockEntry<MetalLadderBlock> CAST_IRON_LADDER =
            REGISTRATE.block("cast_iron_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("cast_iron",
                            () -> DataIngredient.tag(Items.INGOTS_CAST_IRON.tag), MapColor.TERRACOTTA_WHITE))
                    .register();

    public static final BlockEntry<MetalLadderBlock> LEAD_LADDER =
            REGISTRATE.block("lead_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("lead",
                            () -> DataIngredient.tag(CommonMetal.LEAD.ingots), MapColor.TERRACOTTA_WHITE))
                    .lang("Leadder")
                    .register();

    public static final BlockEntry<MetalLadderBlock> NICKEL_LADDER =
            REGISTRATE.block("nickel_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("nickel",
                            () -> DataIngredient.tag(CommonMetal.NICKEL.ingots), MapColor.TERRACOTTA_WHITE))
                    .register();
    public static final BlockEntry<MetalLadderBlock> CONSTANTAN_LADDER =
            REGISTRATE.block("constantan_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("constantan",
                            () -> DataIngredient.tag(CommonMetal.CONSTANTAN.ingots), MapColor.TERRACOTTA_WHITE))
                    .register();
    //------------------SCAFFOLDINGS------------------//
    public static final BlockEntry<MetalScaffoldingBlock> STEEL_SCAFFOLD =
            REGISTRATE.block("steel_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(4.0F)
                            .requiresCorrectToolForDrops())
                    .transform(BuilderTransformers.scaffold("steel",
                            () -> DataIngredient.tag(CommonMetal.STEEL.ingots), MapColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.STEEL_SCAFFOLD, TFMGSpriteShifts.STEEL_SCAFFOLD_INSIDE, TFMGSpriteShifts.STEEL_CASING))
                    .register();

    public static final BlockEntry<MetalScaffoldingBlock> ALUMINUM_SCAFFOLD =
            REGISTRATE.block("aluminum_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops())

                    .transform(BuilderTransformers.scaffold("aluminum",
                            () -> DataIngredient.tag(CommonMetal.ALUMINUM.ingots), MapColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.ALUMINUM_SCAFFOLD, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_INSIDE, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_TOP))
                    .register();
    //------------------DECOR------------------//
    public static final BlockEntry<Block> SLAG_BRICKS =
            REGISTRATE.block("slag_bricks", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CALCITE))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.SLAG_BLOCK.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 4))
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> CINDER_BLOCK =
            REGISTRATE.block("cinder_block", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.BASALT))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<Block> CINDERFLOUR_BLOCK =
            REGISTRATE.block("cinderflour_block", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERRACK))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    //public static final MaterialSet SLAG_BRICKS_SET = makeVariants(SLAG_BRICKS, true);
    public static final BlockEntry<LithiumTorchBlock> LITHIUM_TORCH =
            REGISTRATE.block("lithium_torch", LithiumTorchBlock::new)
                    .initialProperties(() -> net.minecraft.world.level.block.Blocks.TORCH)
                    .properties(p -> p.lightLevel(s -> 14).instabreak().noOcclusion())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new LithiumTorchGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<Block> FACTORY_FLOOR =
            REGISTRATE.block("factory_floor", Block::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.ALUMINUM.ingots), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();

    // public static final MaterialSet FACTORY_FLOOR_SET = makeVariants(FACTORY_FLOOR, true);

    public static final BlockEntry<Block> HARDENED_PLANKS =
		REGISTRATE.block("hardened_planks", Block::new)
			.initialProperties(() -> net.minecraft.world.level.block.Blocks.OAK_PLANKS)
			.properties(p -> p.strength(2.0F).sound(SoundType.CHERRY_WOOD))
			.transform(axeOnly())
			.simpleItem()
			.register();

    //  public static final MaterialSet HARDENED_PLANKS_SET = makeVariants(HARDENED_PLANKS, true);

    public static final BlockEntry<TrainTrapdoorBlock> STEEL_TRAPDOOR =
            REGISTRATE.block("steel_trapdoor", TrainTrapdoorBlock::metal)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN)
                            .sound(SoundType.NETHERITE_BLOCK))
                    .transform(BuilderTransformers.trapdoor(true))
                    .register();


    //------------------FLYWHEELS------------------//
    public static final BlockEntry<TFMGFlywheelBlock>
            STEEL_FLYWHEEL = flywheel("steel", CommonMetal.STEEL.ingots, TFMGFlywheelBlock::steel),
            LEAD_FLYWHEEL = flywheel("lead", CommonMetal.LEAD.ingots, TFMGFlywheelBlock::lead),
            CAST_IRON_FLYWHEEL = flywheel("cast_iron", TFMGTags.Items.INGOTS_CAST_IRON.tag, TFMGFlywheelBlock::cast_iron),
            ALUMINUM_FLYWHEEL = flywheel("aluminum", CommonMetal.ALUMINUM.ingots, TFMGFlywheelBlock::aluminum),
            NICKEL_FLYWHEEL = flywheel("nickel", CommonMetal.NICKEL.ingots, TFMGFlywheelBlock::nickel);

    public static String[] DECOR_METALS = {"steel", "aluminum", "cast_iron", "lead", "nickel", "constantan", "copper", "zinc", "brass"};

    public static final List<BlockEntry<TrussBlock>> TRUSSES = new ArrayList<>();
    public static final List<BlockEntry<FrameBlock>> FRAMES = new ArrayList<>();

    static {
        //------------------TRUSSES------------------//
        for (String metal : DECOR_METALS) {
            TRUSSES.add(truss(metal));
            FRAMES.add(frame(metal));
        }
    }

    //------------------CAUTION_BLOCKS------------------//
    public static final DyedBlockList<TFMGHorizontalDirectionalBlock> CAUTION_BLOCKS = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_caution_block", TFMGHorizontalDirectionalBlock::new)
                .initialProperties(() -> Blocks.COPPER_BLOCK)
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                        .withExistingParent(c.getName(), p.modLoc("block/caution_block"))
                        .texture("0", p.modLoc("block/caution_block/" + colourName))
                        .texture("particle", p.modLoc("block/caution_block/" + colourName))
                ))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .recipe((c, p) -> {
                    p.stonecutting(DataIngredient.tag(CommonMetal.ALUMINUM.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2);
                    boolean optional = colour.getId() > 15;
					ICondition[] conditions = optional ? new ICondition[]{new TagFilledCondition(colour.getTag())} : new ICondition[]{};
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, c.get())
                            .requires(colour.getTag())
                            .requires(Items.CAUTION_BLOCKS.tag)
                            .unlockedBy("has_caution_block", RegistrateRecipeProvider.has(Items.CAUTION_BLOCKS.tag))
							.save(p.withConditions(conditions), TFMG.asResource("crafting/materials/" + c.getName() + "_from_other_caution_block"));
                })
                .item()
                .tag(Items.CAUTION_BLOCKS.tag)
                .build()
                .register();
    });

    //------------------CONCRETE------------------//
    public static final BlockEntry<SimpleConcreteloggedBlock> REBAR_BLOCK =
            REGISTRATE.block("rebar_block", SimpleConcreteloggedBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarFloorBlock> REBAR_FLOOR =
            REGISTRATE.block("rebar_floor", RebarFloorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarWallBlock> REBAR_WALL =
            REGISTRATE.block("rebar_wall", RebarWallBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<TFMGDirectionalBlock> REBAR_PILE =
            REGISTRATE.block("rebar_pile", TFMGDirectionalBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarStairsBlock> REBAR_STAIRS =
            REGISTRATE.block("rebar_stairs", p -> new RebarStairsBlock(REBAR_PILE.getDefaultState(), p))
                    .initialProperties(SharedProperties::softMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new RebarStairsGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarPillarBlock> REBAR_PILLAR =
            REGISTRATE.block("rebar_pillar", RebarPillarBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final MaterialSet CONCRETE = generateConcrete(false);
    public static final MaterialSet REBAR_CONCRETE = generateConcrete(true);


    public static final DyedMaterialSetList COLORED_CONCRETE = generateColoredConcrete(false);
    public static final DyedMaterialSetList COLORED_REBAR_CONCRETE = generateColoredConcrete(true);


    public static final BlockEntry<Block> ASPHALT = REGISTRATE.block("asphalt", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.STONE)
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();
    public static final MaterialSet ASPHALT_SET = makeVariants(ASPHALT);

    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_MAIN);
    }

    public static void init() {

    }
}
