package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGCreativeTabs;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.base.TFMGTiers;
import com.drmangotea.tfmg.content.decoration.kinetics.gearbox.SteelVerticalGearboxItem;
import com.drmangotea.tfmg.content.electricity.configuration_wrench.ElectriciansWrenchItem;
import com.drmangotea.tfmg.base.debug.DebugCinderBlockItem;
import com.drmangotea.tfmg.content.electricity.experimental.BetterSpoolItem;
import com.drmangotea.tfmg.content.electricity.measurement.MultimeterItem;
import com.drmangotea.tfmg.content.electricity.utilities.polarizer.MagnetItem;
import com.drmangotea.tfmg.content.electricity.utilities.resistor.ResistorItem;
import com.drmangotea.tfmg.content.electricity.network.transformer.small.ElectromagneticCoilItem;

import com.drmangotea.tfmg.content.engines.CylinderItem;
import com.drmangotea.tfmg.content.engines.FluidContainingItem;
import com.drmangotea.tfmg.content.items.CoalCokeItem;
import com.drmangotea.tfmg.content.items.ScrewdriverItem;
import com.drmangotea.tfmg.content.items.weapons.LeadAxeItem;
import com.drmangotea.tfmg.content.items.weapons.LeadSwordItem;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonItem;
import com.drmangotea.tfmg.content.items.weapons.explosives.pipe_bomb.PipeBombItem;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenade;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.drmangotea.tfmg.content.items.weapons.fire_extinguisher.FireExtinguisherItem;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerItem;
import com.drmangotea.tfmg.content.items.weapons.lithium_blade.LitLithiumBladeItem;
import com.drmangotea.tfmg.content.items.weapons.lithium_blade.LithiumBladeItem;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonItem;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.content.machinery.oil_processing.OilHammerItem;
import com.drmangotea.tfmg.registry.TFMGTags.TFMGItemTags;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.drmangotea.tfmg.base.TFMGBuilderTransformers.COLORS;
import static com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenade.ChemicalColor.*;
import static com.simibubi.create.AllTags.AllItemTags.CREATE_INGOTS;
import static com.simibubi.create.AllTags.AllItemTags.PLATES;

public class TFMGItems {

    public static final TFMGRegistrate REGISTRATE = (TFMGRegistrate) TFMG.REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_MAIN);
    //materials


    public static final ItemEntry<Item>
            STEEL_INGOT = taggedIngredient("steel_ingot", CommonMetal.STEEL.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            CAST_IRON_INGOT = taggedIngredient("cast_iron_ingot", TFMGItemTags.INGOTS_CAST_IRON.tag, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            ALUMINUM_INGOT = taggedIngredient("aluminum_ingot", CommonMetal.ALUMINUM.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            PLASTIC_SHEET = taggedIngredient("plastic_sheet", TFMGItemTags.INGOTS_PLASTIC.tag, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            HEAVY_PLATE = taggedIngredient("heavy_plate", CommonMetal.STEEL.plates, PLATES.tag),
            ALUMINUM_SHEET = taggedIngredient("aluminum_sheet", CommonMetal.ALUMINUM.plates, PLATES.tag),
            NICKEL_SHEET = taggedIngredient("nickel_sheet", CommonMetal.NICKEL.plates, PLATES.tag),
            CAST_IRON_SHEET = taggedIngredient("cast_iron_sheet", TFMGItemTags.PLATES_CAST_IRON.tag, PLATES.tag),
            LEAD_SHEET = taggedIngredient("lead_sheet", CommonMetal.LEAD.plates, PLATES.tag),
            LEAD_INGOT = taggedIngredient("lead_ingot", CommonMetal.LEAD.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            NICKEL_INGOT = taggedIngredient("nickel_ingot", CommonMetal.NICKEL.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            CONSTANTAN_INGOT = taggedIngredient("constantan_ingot", CommonMetal.CONSTANTAN.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            LITHIUM_INGOT = taggedIngredient("lithium_ingot", TFMGItemTags.INGOTS_LITHIUM.tag, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            ALUMINUM_NUGGET = taggedIngredient("aluminum_nugget", CommonMetal.ALUMINUM.nuggets, Tags.Items.NUGGETS),
            STEEL_NUGGET = taggedIngredient("steel_nugget", CommonMetal.STEEL.nuggets, Tags.Items.NUGGETS),
            CAST_IRON_NUGGET = taggedIngredient("cast_iron_nugget", TFMGItemTags.NUGGETS_CAST_IRON.tag, Tags.Items.NUGGETS),
            CONSTANTAN_NUGGET = taggedIngredient("constantan_nugget", CommonMetal.CONSTANTAN.nuggets, Tags.Items.NUGGETS),
            LEAD_NUGGET = taggedIngredient("lead_nugget", CommonMetal.LEAD.nuggets, Tags.Items.NUGGETS),
            NICKEL_NUGGET = taggedIngredient("nickel_nugget", CommonMetal.NICKEL.nuggets, Tags.Items.NUGGETS),
            LITHIUM_NUGGET = taggedIngredient("lithium_nugget", TFMGItemTags.NUGGETS_LITHIUM.tag, Tags.Items.NUGGETS),
            RAW_LEAD = taggedIngredient("raw_lead", CommonMetal.LEAD.rawOres, Tags.Items.RAW_MATERIALS),
            RAW_NICKEL = taggedIngredient("raw_nickel", CommonMetal.NICKEL.rawOres, Tags.Items.RAW_MATERIALS),
            RAW_LITHIUM = taggedIngredient("raw_lithium", TFMGItemTags.RAW_LITHIUM.tag, Tags.Items.RAW_MATERIALS),
            SYNTHETIC_LEATHER = taggedIngredient("synthetic_leather", Tags.Items.LEATHERS),
            LIMESAND = taggedIngredient("limesand", TFMGItemTags.FLUX.tag),
            SULFUR_DUST = taggedIngredient("sulfur_dust", TFMGItemTags.DUSTS_SULFUR.tag, Tags.Items.DUSTS),
            NITRATE_DUST = taggedIngredient("nitrate_dust", TFMGItemTags.DUSTS_SALTPETER.tag, Tags.Items.DUSTS),
            RUBBER_SHEET = taggedIngredient("rubber_sheet", TFMGItemTags.INGOTS_RUBBER.tag, PLATES.tag),
            SILICON_INGOT = taggedIngredient("silicon_ingot", TFMGItemTags.INGOTS_SILICON.tag, CREATE_INGOTS.tag),
            CRUSHED_LITHIUM = taggedIngredient("crushed_raw_lithium", AllTags.AllItemTags.CRUSHED_RAW_MATERIALS.tag)
            ;


    public static final ItemEntry<Item>
            REBAR = REGISTRATE.item("rebar", Item::new)
            .tag(TFMGItemTags.RODS_STEEL.tag, Tags.Items.RODS)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.STEEL.ingots), RecipeCategory.BUILDING_BLOCKS, c, 4))
            .register(),
            SYNTHETIC_STRING = REGISTRATE.item("synthetic_string", Item::new)
                    .tag(Tags.Items.STRINGS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(TFMGItemTags.INGOTS_RUBBER.tag), RecipeCategory.MISC, c, 4))
                    .register();

    public static final ItemEntry<Item>
            COPPER_WIRE = REGISTRATE.item("copper_wire", Item::new).tag(TFMGItemTags.WIRES_COPPER.tag, TFMGItemTags.WIRES.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.COPPER.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2)).register(),
            ALUMINUM_WIRE = REGISTRATE.item("aluminum_wire", Item::new).tag(TFMGItemTags.WIRES_ALUMINUM.tag, TFMGItemTags.WIRES.tag)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.ALUMINUM.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2)).register(),
            CONSTANTAN_WIRE = REGISTRATE.item("constantan_wire", Item::new).tag(TFMGItemTags.WIRES_CONSTANTAN.tag, TFMGItemTags.WIRES.tag)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.CONSTANTAN.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2)).register();

    public static final ItemEntry<Item>
            SPARK_PLUG = REGISTRATE.item("spark_plug", Item::new).register(),
            SLAG = REGISTRATE.item("slag", Item::new).register(),
            BITUMEN = REGISTRATE.item("bitumen", Item::new).register(),
            FIREPROOF_BRICK = REGISTRATE.item("fireproof_brick", Item::new).register(),
            FIRECLAY_BALL = REGISTRATE.item("fireclay_ball", Item::new).register(),
            SCREW = REGISTRATE.item("screw", Item::new)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.STEEL.ingots), RecipeCategory.BUILDING_BLOCKS, c, 4))
                    .lang("Screws")
                    .register(),
            THERMITE_POWDER = REGISTRATE.item("thermite_powder", Item::new).register(),
            STEEL_MECHANISM = REGISTRATE.item("steel_mechanism", Item::new).register(),
            CONCRETE_MIXTURE = REGISTRATE.item("concrete_mixture", Item::new).register(),
            ASPHALT_MIXTURE = REGISTRATE.item("asphalt_mixture", Item::new).register(),
            MAGNETIC_ALLOY_INGOT = REGISTRATE.item("magnetic_alloy_ingot", Item::new).register(),
            MAGNETIC_ALLOY_SHEET = REGISTRATE.item("magnetic_alloy_sheet", Item::new).register(),
            BAUXITE_POWDER = REGISTRATE.item("bauxite_powder", Item::new).register(),
            EMPTY_CIRCUIT_BOARD = REGISTRATE.item("empty_circuit_board", Item::new).register(),
            COATED_CIRCUIT_BOARD = REGISTRATE.item("coated_circuit_board", Item::new).register(),
            ETCHED_CIRCUIT_BOARD = REGISTRATE.item("etched_circuit_board", Item::new).register(),
            CIRCUIT_BOARD = REGISTRATE.item("circuit_board", Item::new).register(),
            TRANSISTOR = REGISTRATE.item("transistor_item", Item::new).lang("Transistor").register(),
            CAPACITOR = REGISTRATE.item("capacitor_item", Item::new).lang("Capacitor").register(),
            COPPER_SULFATE = REGISTRATE.item("copper_sulfate", Item::new).register(),
            LITHIUM_CHARGE = REGISTRATE.item("lithium_charge", Item::new).register(),
            TURBO = REGISTRATE.item("turbo", Item::new).register(),
            GOLDEN_TURBO = REGISTRATE.item("golden_turbo", Item::new).register(),
            CINDERBLOCK = REGISTRATE.item("cinderblock", Item::new)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.CONCRETE.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 8))
                    .register(),
            CINDERFLOURBLOCK = REGISTRATE.item("cinderflourblock", Item::new).register(),
            NAPALM_POTATO = REGISTRATE.item("napalm_potato", Item::new).register(),
            MIXER_BLADE = REGISTRATE.item("mixer_blade", Item::new).register(),
            CENTRIFUGE = REGISTRATE.item("centrifuge", Item::new).register(),
            CRANKSHAFT = REGISTRATE.item("crankshaft", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/crankshaft_model")))
                    .register(),
            P_SEMICONDUCTOR = REGISTRATE.item("p_semiconductor", Item::new)
                    .lang("P-Semiconductor").register(),
            N_SEMICONDUCTOR = REGISTRATE.item("n_semiconductor", Item::new)
                    .lang("N-Semiconductor").register(),
            UNFINISHED_ELECTROMAGNETIC_COIL = REGISTRATE.item("unfinished_electromagnetic_coil", Item::new).register(),
            COPPER_ELECTRODE = REGISTRATE.item("copper_electrode", Item::new)
                    .properties(p -> p.stacksTo(1))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.COPPER.storageBlocks.items()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/copper_electrode_model"))).register(),
            ZINC_ELECTRODE = REGISTRATE.item("zinc_electrode", Item::new)
                    .properties(p -> p.stacksTo(1))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.ZINC.storageBlocks.items()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/zinc_electrode_model"))).register(),
            GRAPHITE_ELECTRODE = REGISTRATE.item("graphite_electrode", Item::new)
                    .properties(p -> p.stacksTo(1))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(TFMGItemTags.STORAGE_BLOCKS_COAL_COKE.tag), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/graphite_electrode_model"))).register(),
            UNFIRED_INSULATOR = REGISTRATE.item("unfired_insulator", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/unfired_insulator_model")))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(Blocks.CLAY.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .register(),
            UNFINISHED_INSULATOR = REGISTRATE.item("unfinished_insulator", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/unfinished_insulator_model"))).register(),
            GLASS_INSULATOR_SEGMENT = REGISTRATE.item("glass_insulator_segment", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/glass_insulator_segment_model"))).register();
    //public static final ItemEntry<TransmissionItem>
    //        TRANSMISSION = REGISTRATE.item("transmission", TransmissionItem::new)
    //        .properties(p -> p.stacksTo(1))
    //        .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/transmission_model"))).register();
    public static final ItemEntry<MagnetItem>
            MAGNET = REGISTRATE.item("magnet", MagnetItem::new).register();

    public static final ItemEntry<ResistorItem>
            UNFINISHED_RESISTOR = REGISTRATE.item("unfinished_resistor", ResistorItem::new).register();
   //public static final ItemEntry<CylinderItem>
   //        DIESEL_ENGINE_CYLINDER = REGISTRATE.item("diesel_engine_cylinder", CylinderItem::new).register(),
   //        SIMPLE_ENGINE_CYLINDER = REGISTRATE.item("simple_engine_cylinder", CylinderItem::new).register(),
   //        ENGINE_CYLINDER = REGISTRATE.item("engine_cylinder", CylinderItem::new).register(),
   //        TURBINE_BLADE = REGISTRATE.item("turbine_blade", CylinderItem::new).register();
    public static final ItemEntry<SpoolItem>
            EMPTY_SPOOL = spoolItem("empty", 0x000000, TFMG.asResource("empty"))
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.HARDENED_PLANKS.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
            .register(),
            COPPER_SPOOL = spoolItem("copper", 0xD8735A, TFMG.asResource("copper"))
                    .register(),
            ALUMINUM_SPOOL = spoolItem("aluminum", 0xEDEFEF, TFMG.asResource("aluminum"))
                    .register(),
            CONSTANTAN_SPOOL = spoolItem("constantan", 0xCFC2A8, TFMG.asResource("constantan"))
                    .register();

    public static final ItemEntry<BetterSpoolItem> DEBUG_SPOOL =
            REGISTRATE.item("debug_spool", BetterSpoolItem::new)
                    .properties(p -> p.stacksTo(1))
                    .register();

    public static final ItemEntry<ElectromagneticCoilItem> ELECTROMAGNETIC_COIL =
            REGISTRATE.item("electromagnetic_coil", ElectromagneticCoilItem::new)
                    .properties(p -> p.stacksTo(1))
                    .register();
    //public static final ItemEntry<FuseItem> FUSE = REGISTRATE.item("fuse", FuseItem::new)
    //        .properties(p -> p.stacksTo(1))
    //        .register();
    public static final ItemEntry<CoalCokeItem> COAL_COKE_DUST = REGISTRATE.item("coal_coke_dust", CoalCokeItem::new)
            .tag(TFMGItemTags.DUSTS_COAL_COKE.tag, TFMGItemTags.BLAST_FURNACE_FUEL.tag, Tags.Items.DUSTS)
            .register();

    public static final ItemEntry<OilHammerItem> OIL_HAMMER = REGISTRATE.item("oil_hammer", OilHammerItem::new)
            .register();

    public static final ItemEntry<CoalCokeItem> COAL_COKE = REGISTRATE.item("coal_coke", CoalCokeItem::new)
            .register();

    public static final ItemEntry<DebugCinderBlockItem> DEBUG_CINDERBLOCK = REGISTRATE.item("debug_cinderblock", DebugCinderBlockItem::new)
            .properties(p -> p.rarity(Rarity.EPIC).stacksTo(1))
            .register();
    public static final ItemEntry<ScrewdriverItem> SCREWDRIVER = REGISTRATE.item("screwdriver", ScrewdriverItem::new)
            .properties(p -> p.stacksTo(1)
                    .durability(256))
            .register();



   // public static final ItemEntry<ArmorItem>
   //         STEEL_HELMET = armor("steel_helmet", TFMGArmorMaterials.STEEL, ArmorItem.Type.HELMET),
   //         STEEL_CHESTPLATE = armor("steel_chestplate", TFMGArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE),
   //         STEEL_LEGGINGS = armor("steel_leggings", TFMGArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS),
   //         STEEL_BOOTS = armor("steel_boots", TFMGArmorMaterials.STEEL, ArmorItem.Type.BOOTS);

    public static final List<ItemEntry<?>>
            STEEL_TOOLS = toolset("steel", TFMGTiers.STEEL),
            ALUMINUM_TOOLS = toolset("aluminum", TFMGTiers.ALUMINUM);

    public static final List<ItemEntry<?>> LEAD_TOOLS = leadToolset();

    public static final ItemEntry<LithiumBladeItem> LITHIUM_BLADE =
            REGISTRATE.item("lithium_blade", p -> new LithiumBladeItem(TFMGTiers.STEEL, p))
                    .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.STEEL, 2, -2.4F)))
                    .model((ctx, prov) -> prov
                            .withExistingParent("lithium_blade", "minecraft:item/handheld")
                            .texture("layer0", "tfmg:item/lithium_blade"))
                    .register();
    public static final ItemEntry<LitLithiumBladeItem> LIT_LITHIUM_BLADE =
            REGISTRATE.item("lit_lithium_blade", p -> new LitLithiumBladeItem(TFMGTiers.STEEL, p))
                    .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.STEEL, 3, -2.4F)))
                    .model((ctx, prov) -> prov
                            .withExistingParent("lit_lithium_blade", "minecraft:item/handheld")
                            .texture("layer0", "tfmg:item/lithium_blade_lit"))
                    .lang("Lithium Blade")
                    .register();


    public static final ItemEntry<AdvancedPotatoCannonItem> ADVANCED_POTATO_CANNON =
            REGISTRATE.item("advanced_potato_cannon", AdvancedPotatoCannonItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .register();

    public static final ItemEntry<FlamethrowerItem> FLAMETHROWER =
            REGISTRATE.item("flamethrower", FlamethrowerItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .properties(p -> p.stacksTo(1))
                    .register();

    public static final ItemEntry<FireExtinguisherItem> FIRE_EXTINGUISHER =
            REGISTRATE.item("fire_extinguisher", FireExtinguisherItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .properties(p -> p.stacksTo(1))
                    .register();

    public static final Map<String, ItemEntry<MultimeterItem>> MULTIMETERS = multimeters();

    public static final ItemEntry<MultimeterItem> MULTIMETER = REGISTRATE.item("multimeter", MultimeterItem::new)
            .register();

    public static final ItemEntry<SequencedAssemblyItem>
            UNFINISHED_POTENTIOMETER = sequencedIngredient("unfinished_potentiometer", "block/potentiometer/unfinished"),
            UNFINISHED_ELECTRIC_MOTOR = sequencedIngredient("unfinished_electric_motor", "block/electric_motor/unfinished"),
            UNFINISHED_HEAVY_ELECTRIC_MOTOR = sequencedIngredient("unfinished_heavy_electric_motor", "block/heavy_electric_motor/unfinished"),
            UNFINISHED_GENERATOR = sequencedIngredient("unfinished_generator", "item/unfinished_generator_model"),
            UNFINISHED_STEEL_MECHANISM = sequencedIngredient("unfinished_steel_mechanism"),
            UNFINISHED_TRANSISTOR = sequencedIngredient("unfinished_transistor"),
            UNFINISHED_CAPACITOR = sequencedIngredient("unfinished_capacitor"),
            UNFINISHED_CIRCUIT_BOARD = sequencedIngredient("unfinished_circuit_board"),
            UNPROCESSED_HEAVY_PLATE = sequencedIngredient("unprocessed_heavy_plate");


    public static final ItemEntry<QuadPotatoCannonItem> QUAD_POTATO_CANNON =
            REGISTRATE.item("quad_potato_cannon", QuadPotatoCannonItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .register();

    public static final ItemEntry<PipeBombItem>
            PIPE_BOMB = REGISTRATE.item("pipe_bomb", PipeBombItem::new)
            .register();

    public static final ItemEntry<CylinderItem>
            DIESEL_ENGINE_CYLINDER = REGISTRATE.item("diesel_engine_cylinder", CylinderItem::new).register(),
            SIMPLE_ENGINE_CYLINDER = REGISTRATE.item("simple_engine_cylinder", CylinderItem::new).register(),
            ENGINE_CYLINDER = REGISTRATE.item("engine_cylinder", CylinderItem::new).register(),
            TURBINE_BLADE = REGISTRATE.item("turbine_blade", CylinderItem::new).register();


    public static final ItemEntry<FluidContainingItem>
            OIL_CAN = REGISTRATE.item("oil_can", p -> new FluidContainingItem(p, TFMGFluids.LUBRICATION_OIL))
            .properties(p -> p.stacksTo(1))
            .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/oil_can_model")))
            .register(),
            COOLING_FLUID_BOTTLE = REGISTRATE.item("cooling_fluid_bottle", p -> new FluidContainingItem(p, TFMGFluids.COOLING_FLUID))
                    .properties(p -> p.stacksTo(1))
                    .register();

    //public static final ItemEntry<FluidContainingItem>
    //        OIL_CAN = REGISTRATE.item("oil_can", p -> new FluidContainingItem(p, TFMGFluids.LUBRICATION_OIL))
    //        .properties(p -> p.stacksTo(1))
    //        .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/oil_can_model")))
    //        .register(),
    //        COOLING_FLUID_BOTTLE = REGISTRATE.item("cooling_fluid_bottle", p -> new FluidContainingItem(p, TFMGFluids.COOLING_FLUID))
    //                .properties(p -> p.stacksTo(1))
    //                .register();
    public static final ItemEntry<ElectriciansWrenchItem>
            CONFIGURATION_WRENCH = REGISTRATE.item("electricians_wrench", ElectriciansWrenchItem::new)
            .lang("Configuration Wrench")
            .register();

    public static final ItemEntry<ThermiteGrenadeItem>
            THERMITE_GRENADE = thermiteGrenade("thermite_grenade", BASE);
    public static final ItemEntry<ThermiteGrenadeItem>
            ZINC_GRENADE = thermiteGrenade("zinc_grenade", GREEN);
    public static final ItemEntry<ThermiteGrenadeItem>
            COPPER_GRENADE = thermiteGrenade("copper_grenade", BLUE);

    /// /////////////////////////

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name, String model) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource(model)))
                .register();
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }

    private static ItemEntry<ArmorItem> armor(String name, Holder<ArmorMaterial> material, ArmorItem.Type slot) {
        return REGISTRATE.item(name, p -> new ArmorItem(material, slot, p)).register();
    }

    private static List<ItemEntry<?>> toolset(String material, Tier tier) {

        List<ItemEntry<?>> list = new ArrayList<>();

        list.add(REGISTRATE.item(material + "_sword", p -> new SwordItem(tier, p))
                .properties(p -> p.attributes(SwordItem.createAttributes(tier, 2, -2.4F)))
                .model((ctx, prov) -> prov
                        .withExistingParent(material + "_sword", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/" + material + "_sword"))
                .register());
        list.add(REGISTRATE.item(material + "_pickaxe", p -> new PickaxeItem(tier, p))
                .properties(p -> p.attributes(PickaxeItem.createAttributes(tier, 1, 1)))
                .model((ctx, prov) -> prov
                        .withExistingParent(material + "_pickaxe", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/" + material + "_pickaxe"))
                .register());
        list.add(REGISTRATE.item(material + "_axe", p -> new AxeItem(tier, p))
                .properties(p -> p.attributes(AxeItem.createAttributes(tier, 6.0F, -3.2F)))
                .model((ctx, prov) -> prov
                        .withExistingParent(material + "_axe", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/" + material + "_axe"))
                .register());
        list.add(REGISTRATE.item(material + "_shovel", p -> new ShovelItem(tier, p))
                .properties(p -> p.attributes(ShovelItem.createAttributes(tier, 1, 1)))
                .model((ctx, prov) -> prov
                        .withExistingParent(material + "_shovel", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/" + material + "_shovel"))
                .register());
        list.add(REGISTRATE.item(material + "_hoe", p -> new HoeItem(tier, p))
                .properties(p -> p.attributes(HoeItem.createAttributes(tier, 1, 1)))
                .model((ctx, prov) -> prov
                        .withExistingParent(material + "_hoe", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/" + material + "_hoe"))
                .register());


        return list;
    }

    public static final ItemEntry<SteelVerticalGearboxItem> STEEL_VERTICAL_GEARBOX =
            REGISTRATE.item("steel_vertical_gearbox", SteelVerticalGearboxItem::new)
                    .model(AssetLookup.customBlockItemModel("steel_gearbox", "item_vertical"))
                    .lang("Steel Vertical Gearbox")
                    .register();


    private static List<ItemEntry<?>> leadToolset() {
        List<ItemEntry<?>> list = new ArrayList<>();
        list.add(REGISTRATE.item("lead_sword", p -> new LeadSwordItem(TFMGTiers.LEAD, p))
                .properties(p -> p.attributes(SwordItem.createAttributes(TFMGTiers.LEAD, 2, -2.4F)))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_sword", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/lead_sword"))
                .register());
        list.add(REGISTRATE.item("lead_pickaxe", p -> new PickaxeItem(TFMGTiers.LEAD, p))
                .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.LEAD, 1,1)))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_pickaxe", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/lead_pickaxe"))
                .register());
        list.add(REGISTRATE.item("lead_axe", p -> new LeadAxeItem(TFMGTiers.LEAD, p))
                .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.LEAD, 6.0F, -3.2F)))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_axe", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/lead_axe"))
                .register());
        list.add(REGISTRATE.item("lead_shovel", p -> new ShovelItem(TFMGTiers.LEAD, p))
                .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.LEAD, 1,1)))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_shovel", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/lead_shovel"))
                .register());
        list.add(REGISTRATE.item("lead_hoe", p -> new HoeItem(TFMGTiers.LEAD, p))
                .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.LEAD, 1,1)))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_hoe", "minecraft:item/handheld")
                        .texture("layer0", "tfmg:item/lead_hoe"))
                .register());


        return list;
    }


    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    public static ItemEntry<Item> item(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }


    public static ItemEntry<SequencedAssemblyItem> assemblyItem(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }

    public static Map<String, ItemEntry<MultimeterItem>> multimeters() {
        Map<String, ItemEntry<MultimeterItem>> map = new HashMap<>();

        for (String color : COLORS) {

            map.put(color, REGISTRATE.item(color + "_multimeter", MultimeterItem::new)
                    .register());
        }

        return map;
    }

    public static ItemBuilder<SpoolItem, CreateRegistrate> spoolItem(String name, int barColor, ResourceLocation type) {
        return REGISTRATE.item(name + "_spool", p -> new SpoolItem(p, barColor, type))
                .tag(TFMGItemTags.SPOOLS.tag)
                .properties(p -> p.stacksTo(1));

    }


    private static ItemEntry<ThermiteGrenadeItem> thermiteGrenade(String name, ThermiteGrenade.ChemicalColor color) {
        return REGISTRATE.item(name, p -> new ThermiteGrenadeItem(p, color))
                .register();
    }

    public static void init() {
    }
}
