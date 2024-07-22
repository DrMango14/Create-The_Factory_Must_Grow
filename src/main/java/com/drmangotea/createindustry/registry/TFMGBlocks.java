package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.base.*;
import com.drmangotea.createindustry.base.multiblock.FluidOutputBlock;
import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.blocks.cogwheeels.TFMGCogWheelBlock;
import com.drmangotea.createindustry.blocks.cogwheeels.TFMGCogwheelBlockItem;
import com.drmangotea.createindustry.blocks.concrete.formwork.FormWorkBlock;
import com.drmangotea.createindustry.blocks.concrete.formwork.FormWorkGenerator;
import com.drmangotea.createindustry.blocks.concrete.formwork.rebar.RebarFormWorkBlock;
import com.drmangotea.createindustry.blocks.decoration.LithiumTorchBlock;
import com.drmangotea.createindustry.blocks.decoration.LithiumTorchGenerator;
import com.drmangotea.createindustry.blocks.decoration.TFMGGravityBlock;
import com.drmangotea.createindustry.blocks.decoration.TrussBlock;
import com.drmangotea.createindustry.blocks.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.createindustry.blocks.decoration.kinetics.SteelGearboxBlock;
import com.drmangotea.createindustry.blocks.decoration.kinetics.flywheels.TFMGFlywheelBlock;
import com.drmangotea.createindustry.blocks.deposits.FluidDepositBlock;
import com.drmangotea.createindustry.blocks.electricity.base.ConverterBlock;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.CableTubeBlock;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.DiagonalCableBlock;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.DiagonalCableGenerator;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.copycat_cable_block.CopycatCableBlockModel;
import com.drmangotea.createindustry.blocks.electricity.capacitor.AccumulatorBlock;
import com.drmangotea.createindustry.blocks.electricity.capacitor.AccumulatorCTBehavior;
import com.drmangotea.createindustry.blocks.electricity.electrical_switch.ElectricalSwitchBlock;
import com.drmangotea.createindustry.blocks.electricity.electrical_switch.LeverGenerator;
import com.drmangotea.createindustry.blocks.electricity.generation.creative_generator.VoltageCubeBlock;
import com.drmangotea.createindustry.blocks.electricity.lights.neon.NeonTubeBlock;
import com.drmangotea.createindustry.blocks.electricity.voltmeter.energy_meter.EnergyMeterBlock;
import com.drmangotea.createindustry.blocks.engines.compact.CompactEngineBlock;
import com.drmangotea.createindustry.blocks.engines.diesel.DieselEngineBlock;
import com.drmangotea.createindustry.blocks.engines.diesel.engine_expansion.DieselEngineExpansionBlock;
import com.drmangotea.createindustry.blocks.engines.intake.AirIntakeBlock;
import com.drmangotea.createindustry.blocks.engines.intake.AirIntakeGenerator;
import com.drmangotea.createindustry.blocks.engines.low_grade_fuel.LowGradeFuelEngineBlock;
import com.drmangotea.createindustry.blocks.engines.radial.RadialEngineBlock;
import com.drmangotea.createindustry.blocks.engines.radial.input.RadialEngineInputBlock;
import com.drmangotea.createindustry.blocks.engines.radial.large.LargeRadialEngineBlock;
import com.drmangotea.createindustry.blocks.engines.small.EngineGenerator;
import com.drmangotea.createindustry.blocks.engines.small.gasoline.GasolineEngineBackBlock;
import com.drmangotea.createindustry.blocks.engines.small.gasoline.GasolineEngineBlock;
import com.drmangotea.createindustry.blocks.engines.small.lpg.LPGEngineBackBlock;
import com.drmangotea.createindustry.blocks.engines.small.lpg.LPGEngineBlock;
import com.drmangotea.createindustry.blocks.engines.small.turbine.TurbineEngineBackBlock;
import com.drmangotea.createindustry.blocks.engines.small.turbine.TurbineEngineBlock;
import com.drmangotea.createindustry.blocks.electricity.base.cables.CableConnectorGenerator;
import com.drmangotea.createindustry.blocks.electricity.base.cables.CableConnectorBlock;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.CableHubBlock;
import com.drmangotea.createindustry.blocks.electricity.capacitor.CapacitorCTBehavior;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.copycat_cable_block.CopycatCableBlock;
import com.drmangotea.createindustry.blocks.electricity.lights.LightBulbBlock;
import com.drmangotea.createindustry.blocks.electricity.lights.rgb.RGBLightBulbBlock;
import com.drmangotea.createindustry.blocks.electricity.resistors.ResistorBlock;
import com.drmangotea.createindustry.blocks.electricity.transformer.CoilBlock;
import com.drmangotea.createindustry.blocks.electricity.capacitor.CapacitorBlock;
import com.drmangotea.createindustry.blocks.electricity.generation.creative_generator.CreativeGeneratorBlock;
import com.drmangotea.createindustry.blocks.electricity.electric_motor.ElectricMotorBlock;
import com.drmangotea.createindustry.blocks.electricity.batteries.GalvanicCellBlock;
import com.drmangotea.createindustry.blocks.electricity.generation.generator.GeneratorBlock;
import com.drmangotea.createindustry.blocks.electricity.generation.large_generator.RotorBlock;
import com.drmangotea.createindustry.blocks.electricity.generation.large_generator.StatorBlock;
import com.drmangotea.createindustry.blocks.electricity.generation.large_generator.StatorGenerator;
import com.drmangotea.createindustry.blocks.electricity.polarizer.PolarizerBlock;
import com.drmangotea.createindustry.blocks.electricity.transformer.CoilGenerator;
import com.drmangotea.createindustry.blocks.electricity.voltmeter.VoltMeterBlock;
import com.drmangotea.createindustry.blocks.machines.exhaust.ExhaustBlock;
import com.drmangotea.createindustry.blocks.machines.firebox.FireboxBlock;
import com.drmangotea.createindustry.blocks.machines.firebox.FireboxGenerator;
import com.drmangotea.createindustry.blocks.machines.flarestack.FlarestackBlock;
import com.drmangotea.createindustry.blocks.machines.flarestack.FlarestackGenerator;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_stove.BlastStoveBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_stove.BlastStoveGenerator;
import com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenCTBehavior;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.IndustrialPipeBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.controller.DistillationControllerBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.output.DistillationOutputBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.base.PumpjackBaseBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackGenerator;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerConnectorBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerHeadBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerPartBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerConnectorBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerHeadBlock;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.large.LargePumpjackHammerPartBlock;
import com.drmangotea.createindustry.items.weapons.explosives.napalm.NapalmBombBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_spout.CastingSpoutBlock;
import com.drmangotea.createindustry.items.CoalCokeBlockItem;
import com.drmangotea.createindustry.items.FossilstoneItem;
import com.drmangotea.createindustry.blocks.deposits.surface_scanner.SurfaceScannerBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.MoltenMetalBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenBlock;
import com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenGenerator;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlock;
import com.drmangotea.createindustry.blocks.tanks.SteelFluidTankModel;
import com.drmangotea.createindustry.blocks.tanks.SteelTankBlock;
import com.drmangotea.createindustry.blocks.tanks.SteelTankGenerator;
import com.drmangotea.createindustry.blocks.tanks.SteelTankItem;
import com.simibubi.create.*;
import com.simibubi.create.content.contraptions.bearing.StabilizedBearingMovementBehaviour;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorGenerator;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraftforge.common.Tags;


import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;
import static com.drmangotea.createindustry.blocks.electricity.lights.LightBulbBlock.LIGHT;
import static com.drmangotea.createindustry.blocks.electricity.lights.neon.NeonTubeBlock.ACTIVE;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;

@SuppressWarnings("removal")
public class TFMGBlocks {


    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }


    //-----------------------MISC---------------------------//



    public static final BlockEntry<Block> STEEL_BLOCK = REGISTRATE.block("steel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/steel"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Steel")
            .register();
    public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE.block("cast_iron_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("cast_iron_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/cast_iron"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Cast Iron")
            .register();

    public static final BlockEntry<Block> ALUMINUM_BLOCK = REGISTRATE.block("aluminum_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("aluminum_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/aluminum"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Aluminum")
            .register();
    public static final BlockEntry<Block> PLASTIC_BLOCK = REGISTRATE.block("plastic_block", Block::new)
            .initialProperties(() -> Blocks.QUARTZ_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("plastic_block"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/plastic"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Plastic")
            .register();

    public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("lead_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/lead"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Lead")
            .register();

    public static final BlockEntry<Block> NICKEL_BLOCK = REGISTRATE.block("nickel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/nickel"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Nickel")
            .register();

    public static final BlockEntry<Block> LITHIUM_BLOCK = REGISTRATE.block("lithium_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())

            .blockstate(simpleCubeAll("lithium_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/lithium"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Lithium")
            .register();


    public static final BlockEntry<Block> COAL_COKE_BLOCK = REGISTRATE.block("coal_coke_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("coal_coke_block"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .item(CoalCokeBlockItem::new)
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Coal Coke")
            .register();


    public static final BlockEntry<TFMGSlidingDoorBlock> HEAVY_CASING_DOOR =
            REGISTRATE.block("heavy_casing_door", p -> new TFMGSlidingDoorBlock(p, false))
                    .transform(TFMGBuilderTransformers.slidingDoor("heavy_casing"))
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();
    public static final BlockEntry<TFMGSlidingDoorBlock> STEEL_CASING_DOOR =
            REGISTRATE.block("steel_door", p -> new TFMGSlidingDoorBlock(p, true))
                    .transform(TFMGBuilderTransformers.slidingDoor("steel"))
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();

    public static final BlockEntry<SteelGearboxBlock> STEEL_GEARBOX = REGISTRATE.block("steel_gearbox", SteelGearboxBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().color(MaterialColor.PODZOL))
            .transform(BlockStressDefaults.setNoImpact())
            .transform(axeOrPickaxe())
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                    (s, f) -> f.getAxis() == s.getValue(GearboxBlock.AXIS))))
            .blockstate((c, p) -> axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p), true))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<NapalmBombBlock> NAPALM_BOMB = REGISTRATE.block("napalm_bomb", NapalmBombBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_GREEN))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Napalm Bomb")
            .register();


    public static final BlockEntry<Block> FOSSILSTONE = REGISTRATE.block("fossilstone", Block::new)
            .initialProperties(() -> Blocks.OBSIDIAN)
            .properties(p -> p.strength(100f, 1200f))
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("fossilstone"))
            .item(FossilstoneItem::new)
            .build()
            .lang("Fossilstone")
            .register();

    public static final BlockEntry<LithiumTorchBlock> LITHIUM_TORCH =
            REGISTRATE.block("lithium_torch", LithiumTorchBlock::new)
            .initialProperties(() -> Blocks.TORCH)
            .properties(p -> p.lightLevel(s -> 14).instabreak().noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new LithiumTorchGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<FluidDepositBlock> OIL_DEPOSIT = REGISTRATE.block("oil_deposit", FluidDepositBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.strength(69696969))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("oil_deposit"))
            .item()
            .build()
            .lang("Oil Deposit")
            .register();

    public static final BlockEntry<CasingBlock> STEEL_CASING = REGISTRATE.block("steel_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.STEEL_CASING))
            .register();
    public static final BlockEntry<CasingBlock> HEAVY_MACHINERY_CASING = REGISTRATE.block("heavy_machinery_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .register();

    public static final BlockEntry<CasingBlock> ELECTRIC_CASING = REGISTRATE.block("electric_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.ELECTRIC_CASING))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .register();




    public static final BlockEntry<Block> LEAD_ORE = REGISTRATE.block("lead_ore", Block::new)
            .initialProperties(() -> Blocks.GOLD_ORE)
            .properties(p -> p.color(MaterialColor.METAL)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LEAD.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/lead", "ores_in_ground/stone"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_LEAD_ORE = REGISTRATE.block("deepslate_lead_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p.color(MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LEAD.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/lead", "ores_in_ground/deepslate"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> NICKEL_ORE = REGISTRATE.block("nickel_ore", Block::new)
            .initialProperties(() -> Blocks.GOLD_ORE)
            .properties(p -> p.color(MaterialColor.METAL)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_NICKEL.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/nickel", "ores_in_ground/stone"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_NICKEL_ORE = REGISTRATE.block("deepslate_nickel_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p.color(MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_NICKEL.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .transform(tagBlockAndItem("ores/nickel", "ores_in_ground/deepslate"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

     public static final BlockEntry<Block> LITHIUM_ORE = REGISTRATE.block("lithium_ore", Block::new)
             .initialProperties(() -> Blocks.GOLD_ORE)
             .properties(p -> p.color(MaterialColor.METAL)
                     .requiresCorrectToolForDrops()
                     .sound(SoundType.STONE))
             .transform(pickaxeOnly())
             .loot((lt, b) -> lt.add(b,
                     RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                             RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LITHIUM.get())
                                     .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
             .tag(BlockTags.NEEDS_STONE_TOOL)
             .tag(Tags.Blocks.ORES)
             .transform(tagBlockAndItem("ores/lithium", "ores_in_ground/stone"))
             .tag(Tags.Items.ORES)
             .build()
             .register();

     public static final BlockEntry<Block> DEEPSLATE_LITHIUM_ORE = REGISTRATE.block("deepslate_lithium_ore", Block::new)
             .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
             .properties(p -> p.color(MaterialColor.STONE)
                     .requiresCorrectToolForDrops()
                     .sound(SoundType.DEEPSLATE))
             .transform(pickaxeOnly())
             .loot((lt, b) -> lt.add(b,
                     RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                             RegistrateBlockLootTables.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LITHIUM.get())
                                     .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
             .tag(BlockTags.NEEDS_STONE_TOOL)
             .tag(Tags.Blocks.ORES)
             .transform(tagBlockAndItem("ores/lithium", "ores_in_ground/deepslate"))
             .tag(Tags.Items.ORES)
             .build()
             .register();



    public static final BlockEntry<Block> SULFUR = REGISTRATE.block("sulfur", Block::new)
            .initialProperties(() -> Blocks.CALCITE)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_YELLOW))
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Sulfur")
            .register();
    public static final BlockEntry<Block> LIGNITE = REGISTRATE.block("lignite", Block::new)
            .initialProperties(() -> Blocks.CALCITE)
            .properties(p -> p.color(MaterialColor.COLOR_BROWN))
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Lignite")
            .register();

    public static final BlockEntry<TFMGGravityBlock> CEMENT = REGISTRATE.block("cement", TFMGGravityBlock::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            //.transform(pickaxeOnly())
            .blockstate(simpleCubeAll("cement"))
            // .tag(Tags.Blocks)
            .item()
            .build()
            .lang("Cement")
            .register();

    public static final BlockEntry<Block> FIRECLAY = REGISTRATE.block("fireclay", Block::new)
            .initialProperties(() -> Blocks.CLAY)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_RED))
            //.transform(pickaxeOnly())
            .blockstate(simpleCubeAll("fireclay"))
            .loot((p, b) -> p.add(b, RegistrateBlockLootTables.createSingleItemTable(TFMGItems.FIRECLAY_BALL.get())
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(TFMGItems.FIRECLAY_BALL.get(), LootPool.lootPool()
                            .add(LootItem.lootTableItem(TFMGItems.FIRECLAY_BALL.get()))))
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(TFMGItems.FIRECLAY_BALL.get(), LootPool.lootPool()
                            .add(LootItem.lootTableItem(TFMGItems.FIRECLAY_BALL.get()))))
                    .withPool(RegistrateBlockLootTables.applyExplosionCondition(TFMGItems.FIRECLAY_BALL.get(), LootPool.lootPool()
                            .add(LootItem.lootTableItem(TFMGItems.FIRECLAY_BALL.get()))))))


            // .tag(Tags.Blocks)
            .item()
            .build()
            .lang("Fireclay")
            .register();

    //-----------------------MACHINES---------------------------//
    
    public static final BlockEntry<FluidOutputBlock> FLUID_OUTPUT = REGISTRATE.block("fluid_output", FluidOutputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate(simpleCubeAll("fluid_output"))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .build()
            .register();
    
    
    public static final BlockEntry<LightBulbBlock> LIGHT_BULB =
            REGISTRATE.block("light_bulb", LightBulbBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    //public static final BlockEntry<LightBulbBlock> INDUSTRIAL_LIGHT =
    //        REGISTRATE.block("industrial_light", LightBulbBlock::new)
    //                .initialProperties(() -> Blocks.IRON_BLOCK)
    //                .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
    //                .transform(pickaxeOnly())
    //                .addLayer(() -> RenderType::cutoutMipped)
    //                .properties(BlockBehaviour.Properties::noOcclusion)
    //                .blockstate(BlockStateGen.directionalBlockProvider(true))
    //                .item()
    //                .transform(customItemModel())
    //                .register();

    public static final BlockEntry<RGBLightBulbBlock> RGB_LIGHT_BULB =
            REGISTRATE.block("rgb_light_bulb", RGBLightBulbBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<CopycatCableBlock> COPYCAT_CABLE_BLOCK =
            REGISTRATE.block("copycat_cable_block", CopycatCableBlock::new)
                    .transform(TFMGBuilderTransformers.copycatCable())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatCableBlockModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<Block> COPYCAT_CABLE_BASE = REGISTRATE.block("copycat_cable_base", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.GLOW_LICHEN))
            .addLayer(() -> RenderType::cutoutMipped)
            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .register();
    public static final BlockEntry<CableHubBlock> BRASS_CABLE_HUB =
            REGISTRATE.block("brass_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
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
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> ALUMINUM_CABLE_HUB =
            REGISTRATE.block("aluminum_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> STEEL_CASING_CABLE_HUB =
            REGISTRATE.block("steel_casing_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> HEAVY_CABLE_HUB =
            REGISTRATE.block("heavy_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();


    public static final BlockEntry<CableTubeBlock> CABLE_TUBE =
            REGISTRATE.block("cable_tube", CableTubeBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(p -> p.noOcclusion())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<NeonTubeBlock> NEON_TUBE =
            REGISTRATE.block("neon_tube", NeonTubeBlock::new)
                    .initialProperties(() -> Blocks.GLASS)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p.noOcclusion()
                            .lightLevel(s -> s.getValue(ACTIVE) ? 3 : 0))
                    .blockstate(BlockStateGen.axisBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<DiagonalCableBlock> DIAGONL_CABLE_BLOCK =
            REGISTRATE.block("diagonal_cable_block", DiagonalCableBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new DiagonalCableGenerator()::generate)
                    .properties(p -> p.noOcclusion())
                    .item()
                    .transform(customItemModel())
                    .register();



    public static final BlockEntry<FireboxBlock> FIREBOX =
            REGISTRATE.block("firebox", FireboxBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY).lightLevel(FireboxBlock::getLight))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new FireboxGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();


    //public static final BlockEntry<ElectricalSwitchBlock> ELECTRICAL_SWITCH =
    //        REGISTRATE.block("electrical_switch", ElectricalSwitchBlock::new)
    //                .initialProperties(() -> Blocks.LEVER)
    //                .transform(axeOrPickaxe())
    //                .addLayer(() -> RenderType::cutoutMipped)
    //                .blockstate(new LeverGenerator()::generate)
    //                .onRegister(ItemUseOverrides::addBlock)
    //                .item()
    //                .transform(customItemModel())
    //                .register();


    public static final BlockEntry<AccumulatorBlock> ACCUMULATOR =
            REGISTRATE.block("accumulator", AccumulatorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(AccumulatorCTBehavior::new))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .build()
                    .register();

    //public static final BlockEntry<WeldingMachineBlock> WELDING_MACHINE =
    //        REGISTRATE.block("welding_machine", WeldingMachineBlock::new)
    //                .initialProperties(SharedProperties::softMetal)
    //                .properties(p -> p.noOcclusion().color(MaterialColor.PODZOL))
    //                .transform(axeOrPickaxe())
    //                .blockstate(BlockStateGen.horizontalBlockProvider(true))
    //                .item(AssemblyOperatorBlockItem::new)
    //                .transform(customItemModel())
    //                .register();

    public static final BlockEntry<CapacitorBlock> CAPACITOR =
            REGISTRATE.block("capacitor", CapacitorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(CapacitorCTBehavior::new))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<ConverterBlock> CONVERTER =
            REGISTRATE.block("converter", ConverterBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(CapacitorCTBehavior::new))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .build()
                    .register();



    public static final BlockEntry<GalvanicCellBlock> GALVANIC_CELL =
            REGISTRATE.block("galvanic_cell", GalvanicCellBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<PolarizerBlock> POLARIZER =
            REGISTRATE.block("polarizer", PolarizerBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<VoltMeterBlock> VOLTMETER =
            REGISTRATE.block("voltmeter", VoltMeterBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<EnergyMeterBlock> ENERGY_METER =
            REGISTRATE.block("energy_meter", EnergyMeterBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ResistorBlock> RESISTOR =
            REGISTRATE.block("resistor", ResistorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<CoilBlock> COPPER_COIL =
            REGISTRATE.block("copper_coil", CoilBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .blockstate(new CoilGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<ElectricMotorBlock> ELECTRIC_MOTOR =
            REGISTRATE.block("electric_motor", ElectricMotorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CreativeMotorGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(45.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RotorBlock> ROTOR =
            REGISTRATE.block("rotor", RotorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.axisBlockProvider(true))
                    .transform(BlockStressDefaults.setImpact(350))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<StatorBlock> STATOR =
            REGISTRATE.block("stator", StatorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new StatorGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<GeneratorBlock> GENERATOR =
            REGISTRATE.block("generator", GeneratorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(BlockStressDefaults.setImpact(30.0))
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<CableConnectorBlock> CABLE_CONNECTOR = REGISTRATE.block("cable_connector", CableConnectorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .properties(p -> p.requiresCorrectToolForDrops())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate(new CableConnectorGenerator()::generate)
            .item()
            .transform(customItemModel())
            .lang("Cable Connector")
            .register();

    //public static final BlockEntry<CableConnectorBlock> GLASS_CABLE_CONNECTOR = REGISTRATE.block("glass_cable_connector", CableConnectorBlock::new)
    //        .initialProperties(() -> Blocks.IRON_BLOCK)
    //        .properties(p -> p.color(MaterialColor.TERRACOTTA_GREEN))
    //        .properties(p -> p.requiresCorrectToolForDrops())
    //        .properties(BlockBehaviour.Properties::noOcclusion)
    //        .transform(pickaxeOnly())
    //        .blockstate(new CableConnectorGenerator()::generate)
    //        .item()
    //        .transform(customItemModel())
    //        .lang("Glass Cable Connector")
    //        .register();


    public static final BlockEntry<CreativeGeneratorBlock> CREATIVE_GENERATOR = REGISTRATE.block("creative_generator", CreativeGeneratorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Creative Generator")
            .register();

    public static final BlockEntry<VoltageCubeBlock> VOLTAGE_CUBE =
            REGISTRATE.block("voltage_cube", VoltageCubeBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();

    ////
    public static final BlockEntry<FormWorkBlock> FORMWORK_BLOCK =
            REGISTRATE.block("formwork_block", FormWorkBlock::new)
                    .initialProperties(() -> Blocks.OAK_PLANKS)
                    .properties(p -> p.color(MaterialColor.WOOD))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new FormWorkGenerator()::generate)
                    .transform(axeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<RebarFormWorkBlock> REBAR_FORMWORK_BLOCK =
            REGISTRATE.block("rebar_formwork_block", RebarFormWorkBlock::new)
                    .initialProperties(() -> Blocks.OAK_PLANKS)
                    .properties(p -> p.color(MaterialColor.WOOD))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new FormWorkGenerator()::generate)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(axeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<ExhaustBlock> EXHAUST =
            REGISTRATE.block("exhaust", ExhaustBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<FlarestackBlock> FLARESTACK =
            REGISTRATE.block("flarestack", FlarestackBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                            .lightLevel(s -> s.getValue(FlarestackBlock.LIT) ? 15 : 0)
                            .noOcclusion())
                    .blockstate(new FlarestackGenerator()::generate)
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<SurfaceScannerBlock> SURFACE_SCANNER =
            REGISTRATE.block("surface_scanner", SurfaceScannerBlock::new)
                    //  .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    //  .properties(p -> p
                    //          .strength(4.5F))
                    //  .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(TFMGBuilderTransformers.surfaceScanner())
                    .transform(pickaxeOnly())
                    //  .transform(axeOrPickaxe())
                    //  .transform(BlockStressDefaults.setImpact(10.0))
                    .register();


    //fluid stuff


    @SuppressWarnings("'addLayer(java.util.function.Supplier<java.util.function.Supplier<net.minecraft.client.renderer.RenderType>>)' is deprecated and marked for removal ")
    public static final BlockEntry<SteelTankBlock> STEEL_FLUID_TANK =
            REGISTRATE.block("steel_fluid_tank", SteelTankBlock::regular)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new SteelTankGenerator()::generate)
            .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(SteelTankItem::new)
            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
            .build()
            .register();
    /////

    //Distillation


    public static final BlockEntry<DistillationOutputBlock> STEEL_DISTILLATION_OUTPUT =
            REGISTRATE.block("steel_distillation_output", DistillationOutputBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<DistillationControllerBlock> STEEL_DISTILLATION_CONTROLLER =
            REGISTRATE.block("steel_distillation_controller", DistillationControllerBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<IndustrialPipeBlock> INDUSTRIAL_PIPE = REGISTRATE.block("industrial_pipe", IndustrialPipeBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops().noOcclusion())
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Industrial Pipe")
            .register();
////////////////


    //Pumpjack
    public static final BlockEntry<MachineInputBlock> MACHINE_INPUT =
            REGISTRATE.block("machine_input", MachineInputBlock::new)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p
                            .strength(4.5F))
                    .transform(axeOrPickaxe())
                    .transform(BlockStressDefaults.setImpact(4.0))
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .build()
                    .register();



    public static final BlockEntry<PumpjackBlock> PUMPJACK_HAMMER =
            REGISTRATE.block("pumpjack_hammer", PumpjackBlock::new)
                    .properties(p -> p.color(MaterialColor.PODZOL))
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
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .build()
                    .lang("Pumpjack Crank")
                    .register();

    public static final BlockEntry<PumpjackHammerPartBlock> PUMPJACK_HAMMER_PART = REGISTRATE.block("pumpjack_hammer_part", PumpjackHammerPartBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .item()
            .build()
            .lang("Pumpjack Hammer Part")
            .register();

    public static final BlockEntry<PumpjackHammerHeadBlock> PUMPJACK_HAMMER_HEAD = REGISTRATE.block("pumpjack_hammer_head", PumpjackHammerHeadBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .item()
            .build()
            .lang("Pumpjack Hammer Head")
            .register();


    public static final BlockEntry<PumpjackHammerConnectorBlock> PUMPJACK_HAMMER_CONNECTOR = REGISTRATE.block("pumpjack_hammer_connector", PumpjackHammerConnectorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .item()
            .build()
            .lang("Pumpjack Hammer Connector")
            .register();
    ////////
    public static final BlockEntry<LargePumpjackHammerPartBlock> LARGE_PUMPJACK_HAMMER_PART = REGISTRATE.block("large_pumpjack_hammer_part", LargePumpjackHammerPartBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .item()
            .build()
            .lang("Large Pumpjack Hammer Part")
            .register();

    public static final BlockEntry<LargePumpjackHammerHeadBlock> LARGE_PUMPJACK_HAMMER_HEAD = REGISTRATE.block("large_pumpjack_hammer_head", LargePumpjackHammerHeadBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .item()
            .build()
            .lang("Large Pumpjack Hammer Head")
            .register();


    public static final BlockEntry<LargePumpjackHammerConnectorBlock> LARGE_PUMPJACK_HAMMER_CONNECTOR = REGISTRATE.block("large_pumpjack_hammer_connector", LargePumpjackHammerConnectorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .item()
            .build()
            .lang("Large Pumpjack Hammer Connector")
            .register();
    ////////
    public static final BlockEntry<PumpjackBaseBlock> PUMPJACK_BASE = REGISTRATE.block("pumpjack_base", PumpjackBaseBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Pumpjack Base")
            .register();

    ///////////

    //Blast Furnace
    
    public static final BlockEntry<BlastStoveBlock> BLAST_STOVE = REGISTRATE.block("blast_stove", BlastStoveBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(new BlastStoveGenerator()::generate)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .lang("Blast Stove")
            .register();

    public static final BlockEntry<Block> FIREPROOF_BRICKS = REGISTRATE.block("fireproof_bricks", Block::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Fireproof Bricks")
            .register();
    public static final BlockEntry<WallBlock> FIREPROOF_BRICK_REINFORCEMENT = REGISTRATE.block("fireproof_brick_reinforcement", WallBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(BlockTags.WALLS)
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "fireproof_brick_reinforcement"))
            .item()
            .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "fireproof_brick_reinforcement"))
            .build()
            .lang("Fireproof Brick Reinforcement")
            .register();
    public static final BlockEntry<BlastFurnaceOutputBlock> BLAST_FURNACE_OUTPUT = REGISTRATE.block("blast_furnace_output", BlastFurnaceOutputBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                    .getExistingFile(p.modLoc("block/blast_furnace_output/block"))))
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .lang("Blast Furnace Output")
            .register();
    public static final BlockEntry<MoltenMetalBlock> MOLTEN_METAL =
            REGISTRATE.block("molten_metal", MoltenMetalBlock::new)
                    .initialProperties(SharedProperties.CRUSHING_WHEEL_CONTROLLER_MATERIAL)
                    .properties(p -> p.color(MaterialColor.COLOR_ORANGE))
                    .properties(p -> p.lightLevel(s -> 15))
                    .properties(p -> p.noOcclusion()
                            .noLootTable()
                            .air())

                    .register();

    //--Casting Basin
    public static final BlockEntry<CastingBasinBlock> CASTING_BASIN = REGISTRATE.block("casting_basin", CastingBasinBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .build()
            .lang("Casting Basin")
            .register();

    public static final BlockEntry<CastingSpoutBlock> CASTING_SPOUT = REGISTRATE.block("casting_spout", CastingSpoutBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .lang("Casting Spout")
            .register();


    //////////
    public static final BlockEntry<CokeOvenBlock> COKE_OVEN = REGISTRATE.block("coke_oven", CokeOvenBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(new CokeOvenGenerator()::generate)
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(CokeOvenCTBehavior::new))
            .item()
            .transform(customItemModel())
            .lang("Coke Oven")
            .register();
    ///////


    public static final BlockEntry<AirIntakeBlock> AIR_INTAKE = REGISTRATE.block("air_intake", AirIntakeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new AirIntakeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .lang("Air Intake")
            .register();
    //-----------------------COGWHEELS-------------------------//

    public static final BlockEntry<TFMGCogWheelBlock> STEEL_COGWHEEL =
            REGISTRATE.block("steel_cogwheel", TFMGCogWheelBlock::small)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).color(MaterialColor.DIRT))
                    .transform(BlockStressDefaults.setNoImpact())
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();

    public static final BlockEntry<TFMGCogWheelBlock> LARGE_STEEL_COGWHEEL =
            REGISTRATE.block("large_steel_cogwheel", TFMGCogWheelBlock::large)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).color(MaterialColor.DIRT))
                    .transform(axeOrPickaxe())
                    .transform(BlockStressDefaults.setNoImpact())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();
    //
    public static final BlockEntry<TFMGCogWheelBlock> ALUMINUM_COGWHEEL =
            REGISTRATE.block("aluminum_cogwheel", TFMGCogWheelBlock::small)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.COPPER).color(MaterialColor.DIRT))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(BlockStressDefaults.setNoImpact())
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();

    public static final BlockEntry<TFMGCogWheelBlock> LARGE_ALUMINUM_COGWHEEL =
            REGISTRATE.block("large_aluminum_cogwheel", TFMGCogWheelBlock::large)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.COPPER).color(MaterialColor.DIRT))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(axeOrPickaxe())
                    .transform(BlockStressDefaults.setNoImpact())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();

    //-----------------------ENGINES---------------------------//
    public static final BlockEntry<GasolineEngineBlock> GASOLINE_ENGINE =
            REGISTRATE.block("gasoline_engine", GasolineEngineBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(66.0+20.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<GasolineEngineBackBlock> GASOLINE_ENGINE_BACK =
            REGISTRATE.block("gasoline_engine_back", GasolineEngineBackBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<LPGEngineBlock> LPG_ENGINE =
            REGISTRATE.block("lpg_engine", LPGEngineBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(66.0+20.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<LPGEngineBackBlock> LPG_ENGINE_BACK =
            REGISTRATE.block("lpg_engine_back", LPGEngineBackBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TurbineEngineBlock> TURBINE_ENGINE =
            REGISTRATE.block("turbine_engine", TurbineEngineBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(66.0+20.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TurbineEngineBackBlock> TURBINE_ENGINE_BACK =
            REGISTRATE.block("turbine_engine_back", TurbineEngineBackBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<DieselEngineBlock> DIESEL_ENGINE =
            REGISTRATE.block("diesel_engine", DieselEngineBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
                    .transform(BlockStressDefaults.setCapacity(30.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(DieselEngineBlock::getSpeedRange))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<DieselEngineExpansionBlock> DIESEL_ENGINE_EXPANSION = REGISTRATE.block("diesel_engine_expansion", DieselEngineExpansionBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProvider(false))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .build()
            .lang("Diesel Engine Expansion")
            .register();

    public static final BlockEntry<RadialEngineBlock> RADIAL_ENGINE =
            REGISTRATE.block("radial_engine", RadialEngineBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(70.0+25.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    //  .lang("Radial Engine")
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<LargeRadialEngineBlock> LARGE_RADIAL_ENGINE =
            REGISTRATE.block("large_radial_engine", LargeRadialEngineBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(93.0+25.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    // .lang("Large Radial Engine")
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<RadialEngineInputBlock> RADIAL_ENGINE_INPUT =
            REGISTRATE.block("radial_engine_input", RadialEngineInputBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .register();
    public static final BlockEntry<DebugBlock> RADIAL_ENGINE_INPUT_PONDER =
            REGISTRATE.block("radial_engine_input_ponder", DebugBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();


    public static final BlockEntry<CompactEngineBlock> COMPACT_ENGINE =
            REGISTRATE.block("compact_engine", CompactEngineBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(20.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    //.lang("Small Engine")
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<LowGradeFuelEngineBlock> LOW_GRADE_FUEL_ENGINE =
            REGISTRATE.block("low_grade_fuel_engine", LowGradeFuelEngineBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(10.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    // .lang("Low Grade Fuel Engine")
                    .transform(customItemModel())
                    .register();




    //-----------------------BUILDING BLOCKS---------------------------//

    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BUILDING_BLOCKS);
    }
    public static final BlockEntry<TrussBlock> STEEL_TRUSS = REGISTRATE.block("steel_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .lang("Steel Truss")
            .register();
    public static final BlockEntry<TrussBlock> ALUMINUM_TRUSS = REGISTRATE.block("aluminum_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .lang("Aluminum Truss")
            .register();

    public static final BlockEntry<TrussBlock> CAST_IRON_TRUSS = REGISTRATE.block("cast_iron_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .lang("Cast Iron Truss")
            .register();

    public static final BlockEntry<TrussBlock> LEAD_TRUSS = REGISTRATE.block("lead_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/lead")), c::get, 4))
            .item()
            .build()
            .lang("Lead Truss")
            .register();

    public static final BlockEntry<TrussBlock> NICKEL_TRUSS = REGISTRATE.block("nickel_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/nickel")), c::get, 4))
            .item()
            .build()
            .lang("Nickel Truss")
            .register();

    public static final BlockEntry<TrussBlock> COPPER_TRUSS = REGISTRATE.block("copper_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), c::get, 4))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .register();
    public static final BlockEntry<TrussBlock> ZINC_TRUSS = REGISTRATE.block("zinc_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/zinc")), c::get, 4))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .register();

    public static final BlockEntry<TrussBlock> BRASS_TRUSS = REGISTRATE.block("brass_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/brass")), c::get, 4))
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .register();


    public static final BlockEntry<Block> STEEL_FRAME = REGISTRATE.block("steel_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Steel Frame")
            .register();


    public static final BlockEntry<Block> LEAD_FRAME = REGISTRATE.block("lead_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(2))
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/lead")), c::get, 4))
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Lead Frame")
            .register();

    public static final BlockEntry<Block> COPPER_FRAME = REGISTRATE.block("copper_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(2))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/copper")), c::get, 4))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .register();

    public static final BlockEntry<Block> ZINC_FRAME = REGISTRATE.block("zinc_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(2))
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/zinc")), c::get, 4))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .register();

    public static final BlockEntry<Block> BRASS_FRAME = REGISTRATE.block("brass_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(2))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/brass")), c::get, 4))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .register();

    public static final BlockEntry<Block> ALUMINUM_FRAME = REGISTRATE.block("aluminum_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Aluminum Frame")
            .register();

    public static final BlockEntry<Block> CAST_IRON_FRAME = REGISTRATE.block("cast_iron_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Cast Iron Frame")
            .register();

    public static final BlockEntry<Block> NICKEL_FRAME = REGISTRATE.block("nickel_frame", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/nickel")), c::get, 4))
            .addLayer(() -> RenderType::cutoutMipped)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .lang("Nickel Frame")
            .register();

    public static final BlockEntry<Block> HARDENED_PLANKS = REGISTRATE.block("hardened_planks", Block::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(axeOnly())
            .item()
            .build()
            .lang("Hardened Planks")
            .register();

    public static final BlockEntry<SlabBlock> HARDENED_PLANKS_SLAB = REGISTRATE.block("hardened_planks_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.DARK_OAK_WOOD)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "hardened_planks"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item()
            .transform(customItemModel("hardened_planks_bottom"))
            .lang("Hardened Planks Slab")
            .register();

    public static final BlockEntry<GlassBlock> LEAD_GLASS = REGISTRATE.block("lead_glass", GlassBlock::new)
            .initialProperties(() -> Blocks.GLASS)
            .properties(p -> p.color(MaterialColor.COLOR_BLUE))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            //.blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_GLASS)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_GLASS)))
            .transform(pickaxeOnly())
            .item()
            .build()
            //  .transform(customItemModel())
            .lang("Lead Glass")
            .register();


    public static final BlockEntry<Block> ASPHALT = REGISTRATE.block("asphalt", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Asphalt")
            .register();
    public static final BlockEntry<MetalScaffoldingBlock> STEEL_SCAFFOLD =
            REGISTRATE.block("steel_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(4.0F)
                            .requiresCorrectToolForDrops())
                    .transform(BuilderTransformers.scaffold("steel",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/steel")), MaterialColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.STEEL_SCAFFOLD, TFMGSpriteShifts.STEEL_SCAFFOLD_INSIDE, TFMGSpriteShifts.STEEL_CASING))
                    .register();

    public static final BlockEntry<MetalScaffoldingBlock> ALUMINUM_SCAFFOLD =
            REGISTRATE.block("aluminum_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops())
                    .transform(BuilderTransformers.scaffold("aluminum",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/aluminum")), MaterialColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.ALUMINUM_SCAFFOLD, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_INSIDE, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_TOP))
                    .register();



    public static final BlockEntry<IronBarsBlock> STEEL_BARS = TFMGMetalBarsGen.createBars("steel", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/steel")), MaterialColor.TERRACOTTA_CYAN);
    public static final BlockEntry<IronBarsBlock> ALUMINUM_BARS = TFMGMetalBarsGen.createBars("aluminum", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/aluminum")), MaterialColor.TERRACOTTA_WHITE);

    public static final BlockEntry<IronBarsBlock> CAST_IRON_BARS = TFMGMetalBarsGen.createBars("cast_iron", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/cast_iron")), MaterialColor.COLOR_BLACK);

    public static final BlockEntry<IronBarsBlock> LEAD_BARS = TFMGMetalBarsGen.createBars("lead", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/lead")), MaterialColor.TERRACOTTA_BLUE);

    public static final BlockEntry<IronBarsBlock> NICKEL_BARS = TFMGMetalBarsGen.createBars("nickel", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/nickel")), MaterialColor.TERRACOTTA_YELLOW);


    public static final BlockEntry<MetalLadderBlock> STEEL_LADDER =
            REGISTRATE.block("steel_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("steel",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/steel")), MaterialColor.TERRACOTTA_CYAN))
                    .register();
    public static final BlockEntry<MetalLadderBlock> ALUMINUM_LADDER =
            REGISTRATE.block("aluminum_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("aluminum",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/aluminum")), MaterialColor.TERRACOTTA_WHITE))
                    .register();

    public static final BlockEntry<MetalLadderBlock> CAST_IRON_LADDER =
            REGISTRATE.block("cast_iron_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("cast_iron",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/cast_iron")), MaterialColor.TERRACOTTA_WHITE))
                    .register();

    public static final BlockEntry<MetalLadderBlock> LEAD_LADDER =
            REGISTRATE.block("lead_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("lead",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/lead")), MaterialColor.TERRACOTTA_WHITE))
                    .lang("Leadder")
                    .register();

    public static final BlockEntry<MetalLadderBlock> NICKEL_LADDER =
            REGISTRATE.block("nickel_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("nickel",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/nickel")), MaterialColor.TERRACOTTA_WHITE))
                    .register();


    public static final BlockEntry<TFMGFlywheelBlock> STEEL_FLYWHEEL = REGISTRATE.block("steel_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<TFMGFlywheelBlock> ALUMINUM_FLYWHEEL = REGISTRATE.block("aluminum_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_WHITE))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<TFMGFlywheelBlock> CAST_IRON_FLYWHEEL = REGISTRATE.block("cast_iron_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGFlywheelBlock> LEAD_FLYWHEEL = REGISTRATE.block("lead_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGFlywheelBlock> NICKEL_FLYWHEEL = REGISTRATE.block("nickel_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<Block> FACTORY_FLOOR = withVariants("factory_floor", Blocks.STONE,
            MaterialColor.COLOR_GRAY, BlockTags.NEEDS_STONE_TOOL, SoundType.NETHERITE_BLOCK, 3, false);

    public static final BlockEntry<SlabBlock> FACTORY_FLOOR_SLAB = REGISTRATE.block("factory_floor_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "factory_floor"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(BlockTags.WALLS)
            .item()
            .transform(customItemModel("factory_floor_bottom"))
            .lang("Factory Floor Slab")
            .register();


        public static final BlockEntry<Block> CINDER_BLOCK = REGISTRATE.block("cinder_block", Block::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(p->p.sound(SoundType.CALCITE))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
                .item()
                .transform(customItemModel())
                .register();

        public static final BlockEntry<Block> CINDERFLOUR_BLOCK = REGISTRATE.block("cinderflour_block", Block::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(p->p.sound(SoundType.NETHER_BRICKS))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
                .item()
                .transform(customItemModel())
                .register();


    //







    //-----------------------CONCRETE---------------------------//

    //public static final BlockEntry<Block> CONCRETE = REGISTRATE.block("concrete", Block::new)
    //        .initialProperties(() -> Blocks.STONE)
    //        .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
    //        .properties(p -> p.requiresCorrectToolForDrops())
    //        .transform(pickaxeOnly())
    //        .blockstate(simpleCubeAll("concrete"))
    //        .tag(BlockTags.NEEDS_STONE_TOOL)
    //        .transform(tagBlockAndItem("concrete"))
    //        .build()
    //        .lang("Concrete")
    //        .register();


    public static final BlockEntry<Block> CONCRETE = REGISTRATE.block("concrete", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem("concrete"))
            .build()
            .lang("Concrete")
            .register();
    
    public static final BlockEntry<WallBlock> CONCRETE_WALL = REGISTRATE.block("concrete_wall", WallBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(BlockTags.WALLS)
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.CONCRETE.get()), c::get, 1))
            .item()
            .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "concrete"))
            .build()
            .lang("Concrete Wall")
            .register();
    
    public static final BlockEntry<StairBlock> CONCRETE_STAIRS = REGISTRATE.block("concrete_stairs", p -> new StairBlock(() -> TFMGBlocks.CONCRETE.get().defaultBlockState(), p))
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, "concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(BlockTags.STAIRS)
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.CONCRETE.get()), c::get, 1))
            .item()
            .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, "concrete"))
            .build()
            .lang("Concrete Stairs")
            .register();


    public static final BlockEntry<SlabBlock> CONCRETE_SLAB = REGISTRATE.block("concrete_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(BlockTags.SLABS)
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.CONCRETE.get()), c::get, 2))
            .item()
            .transform(customItemModel("concrete_bottom"))
            .lang("Concrete Slab")
            .register();


    public static final BlockEntry<Block> REBAR_CONCRETE = REGISTRATE.block("rebar_concrete", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.sound(SoundType.STONE))
            .properties(p -> p.strength(40, 40))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("rebar_concrete"))
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .transform(tagBlockAndItem("rebar_concrete"))
            .build()
            //.lang(displayName)
            .register();
    
    public static final BlockEntry<WallBlock> REBAR_CONCRETE_WALL = REGISTRATE.block("rebar_concrete_wall", WallBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.sound(SoundType.STONE))
            .properties(p -> p.strength(40, 40))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "rebar_concrete"))
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .tag(BlockTags.WALLS)
                    .item()
                    .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "rebar_concrete"))
            .build()
            .register();
    
    public static final BlockEntry<StairBlock> REBAR_CONCRETE_STAIRS = REGISTRATE.block("rebar_concrete_stairs", p -> new StairBlock(() -> TFMGBlocks.CONCRETE.get().defaultBlockState(), p))
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.sound(SoundType.STONE))
            .properties(p -> p.strength(40, 40))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, "rebar_concrete"))
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .tag(BlockTags.STAIRS)
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, "rebar_concrete"))
            .build()
            .register();

    public static final BlockEntry<SlabBlock> REBAR_CONCRETE_SLAB = REGISTRATE.block("rebar_concrete_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .properties(p -> p.strength(40, 40))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "rebar_concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.REBAR_CONCRETE.get()), c::get, 2))
            .tag(BlockTags.SLABS)
            .item()
            .transform(customItemModel("rebar_concrete_bottom"))
            .lang("Rebar Concrete Slab")
            .register();


    public static BlockEntry<Block> withVariants(String name, Block properties, MaterialColor color, TagKey<Block> toolRequired, SoundType sound, int strenght, boolean wall) {


        if (wall)
            REGISTRATE.block(name + "_wall", WallBlock::new)
                    .initialProperties(() -> properties)
                    .properties(p -> p.color(color))
                    .properties(p -> p.sound(sound))
                    .properties(p -> p.strength(strenght, strenght))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, name))
                    .tag(toolRequired)
                    .tag(BlockTags.WALLS)
                    .item()
                    .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, name))
                    .build()
                    //.lang(displayName + " Wall")
                    .register();
//
        REGISTRATE.block(name + "_stairs", p -> new StairBlock(() -> TFMGBlocks.CONCRETE.get().defaultBlockState(), p))
                .initialProperties(() -> properties)
                .properties(p -> p.color(color))
                .properties(p -> p.sound(sound))
                .properties(p -> p.strength(strenght, strenght))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, name))
                .tag(toolRequired)
                .tag(BlockTags.STAIRS)
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, name))
                .build()
             //   .lang(displayName + " Stairs")
                .register();


//


        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> properties)
                .properties(p -> p.color(color))
                .properties(p -> p.sound(sound))
                .properties(p -> p.strength(strenght, strenght))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate(simpleCubeAll(name))
                .tag(toolRequired)
                .transform(tagBlockAndItem(name))
                .build()
                //.lang(displayName)
                .register();




    }

    public static void register() {
        TFMGEncasedBlocks.register();
        TFMGPipes.register();
        TFMGColoredBlocks.register();
    }
}
