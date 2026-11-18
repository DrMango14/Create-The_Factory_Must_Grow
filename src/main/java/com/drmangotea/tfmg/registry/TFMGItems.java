package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.*;
import com.drmangotea.tfmg.base.data_storage.CylinderFuels;
import com.drmangotea.tfmg.base.debug.DebugCinderBlockItem;
import com.drmangotea.tfmg.base.dyes.DyedItemList;
import com.drmangotea.tfmg.content.decoration.kinetics.gearbox.SteelVerticalGearboxItem;
import com.drmangotea.tfmg.content.electricity.configuration_wrench.ElectriciansWrenchItem;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.content.electricity.content.measuring_instruments.MultimeterItem;
import com.drmangotea.tfmg.content.electricity.content.devices.polarizer.MagnetItem;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.cells.AccumulatorCellItem;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small.ElectromagneticCoilItem;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.content.items.FluidContainingItem;
import com.drmangotea.tfmg.content.items.ScrewdriverItem;
import com.drmangotea.tfmg.content.items.weapons.LeadAxeItem;
import com.drmangotea.tfmg.content.items.weapons.LeadSwordItem;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonItem;
import com.drmangotea.tfmg.content.items.weapons.explosives.pipe_bomb.PipeBombItem;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenade;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.drmangotea.tfmg.content.items.weapons.fire_extinguisher.FireExtinguisherItem;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerItem;
import com.drmangotea.tfmg.content.items.weapons.lithium_blade.LithiumBladeItem;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonItem;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.content.machinery.oil_processing.OilHammerItem;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerMode;
import com.drmangotea.tfmg.recipes.condition.TagFilledCondition;
import com.drmangotea.tfmg.registry.TFMGTags.Items;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Arrays;

import static com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenade.ChemicalColor.*;
import static com.simibubi.create.AllTags.AllItemTags.CREATE_INGOTS;
import static com.simibubi.create.AllTags.AllItemTags.PLATES;

@SuppressWarnings("unused")
public class TFMGItems {

    public static final TFMGRegistrate REGISTRATE = (TFMGRegistrate) TFMG.REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_MAIN);
    //materials


    public static final ItemEntry<Item>
            STEEL_INGOT = taggedIngredient("steel_ingot", CommonMetal.STEEL.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            CAST_IRON_INGOT = taggedIngredient("cast_iron_ingot", Items.INGOTS_CAST_IRON.tag, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            ALUMINUM_INGOT = taggedIngredient("aluminum_ingot", CommonMetal.ALUMINUM.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            PLASTIC_SHEET = taggedIngredient("plastic_sheet", Items.INGOTS_PLASTIC.tag, Items.PLATES_PLASTIC.tag, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            HEAVY_PLATE = taggedIngredient("heavy_plate", CommonMetal.STEEL.plates, PLATES.tag),
            ALUMINUM_SHEET = taggedIngredient("aluminum_sheet", CommonMetal.ALUMINUM.plates, PLATES.tag),
            NICKEL_SHEET = taggedIngredient("nickel_sheet", CommonMetal.NICKEL.plates, PLATES.tag),
            CAST_IRON_SHEET = taggedIngredient("cast_iron_sheet", Items.PLATES_CAST_IRON.tag, PLATES.tag),
            LEAD_SHEET = taggedIngredient("lead_sheet", CommonMetal.LEAD.plates, PLATES.tag),
            LEAD_INGOT = taggedIngredient("lead_ingot", CommonMetal.LEAD.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            NICKEL_INGOT = taggedIngredient("nickel_ingot", CommonMetal.NICKEL.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            CONSTANTAN_INGOT = taggedIngredient("constantan_ingot", CommonMetal.CONSTANTAN.ingots, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            LITHIUM_INGOT = taggedIngredient("lithium_ingot", Items.INGOTS_LITHIUM.tag, Tags.Items.INGOTS, CREATE_INGOTS.tag),
            ALUMINUM_NUGGET = taggedIngredient("aluminum_nugget", CommonMetal.ALUMINUM.nuggets, Tags.Items.NUGGETS),
            STEEL_NUGGET = taggedIngredient("steel_nugget", CommonMetal.STEEL.nuggets, Tags.Items.NUGGETS),
            CAST_IRON_NUGGET = taggedIngredient("cast_iron_nugget", Items.NUGGETS_CAST_IRON.tag, Tags.Items.NUGGETS),
            CONSTANTAN_NUGGET = taggedIngredient("constantan_nugget", CommonMetal.CONSTANTAN.nuggets, Tags.Items.NUGGETS),
            LEAD_NUGGET = taggedIngredient("lead_nugget", CommonMetal.LEAD.nuggets, Tags.Items.NUGGETS),
            NICKEL_NUGGET = taggedIngredient("nickel_nugget", CommonMetal.NICKEL.nuggets, Tags.Items.NUGGETS),
            LITHIUM_NUGGET = taggedIngredient("lithium_nugget", Items.NUGGETS_LITHIUM.tag, Tags.Items.NUGGETS),
            RAW_LEAD = taggedIngredient("raw_lead", CommonMetal.LEAD.rawOres, Tags.Items.RAW_MATERIALS),
            RAW_NICKEL = taggedIngredient("raw_nickel", CommonMetal.NICKEL.rawOres, Tags.Items.RAW_MATERIALS),
            RAW_LITHIUM = taggedIngredient("raw_lithium", Items.RAW_LITHIUM.tag, Tags.Items.RAW_MATERIALS),
            SYNTHETIC_LEATHER = taggedIngredient("synthetic_leather", Tags.Items.LEATHERS),
            LIMESAND = taggedIngredient("limesand", Items.FLUX.tag),
            SULFUR_DUST = taggedIngredient("sulfur_dust", Items.DUSTS_SULFUR.tag, Tags.Items.DUSTS),
            NITRATE_DUST = taggedIngredient("nitrate_dust", Items.DUSTS_SALTPETER.tag, Tags.Items.DUSTS),
            RUBBER_SHEET = taggedIngredient("rubber_sheet", Items.INGOTS_RUBBER.tag, PLATES.tag),
            SILICON_INGOT = taggedIngredient("silicon_ingot", Items.INGOTS_SILICON.tag, CREATE_INGOTS.tag),
            CRUSHED_LITHIUM = taggedIngredient("crushed_raw_lithium", AllTags.AllItemTags.CRUSHED_RAW_MATERIALS.tag)
            ;


    public static final ItemEntry<Item>
            REBAR = REGISTRATE.item("rebar", Item::new)
                    .tag(Items.RODS_STEEL.tag, Tags.Items.RODS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.STEEL.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2))
                    .register(),
            SYNTHETIC_STRING = REGISTRATE.item("synthetic_string", Item::new)
                    .tag(Tags.Items.STRINGS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(Items.INGOTS_RUBBER.tag), RecipeCategory.MISC, c, 4))
                    .register();

    public static final ItemEntry<Item>
            COPPER_WIRE = REGISTRATE.item("copper_wire", Item::new).tag(Items.WIRES_COPPER.tag, Items.WIRES.tag)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.COPPER.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2)).register(),
            ALUMINUM_WIRE = REGISTRATE.item("aluminum_wire", Item::new).tag(Items.WIRES_ALUMINUM.tag, Items.WIRES.tag)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.ALUMINUM.ingots), RecipeCategory.BUILDING_BLOCKS, c, 2)).register(),
            CONSTANTAN_WIRE = REGISTRATE.item("constantan_wire", Item::new).tag(Items.WIRES_CONSTANTAN.tag, Items.WIRES.tag)
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
            BAUXITE_POWDER = REGISTRATE.item("bauxite_powder", Item::new).tag(Items.DUSTS_BAUXITE.tag).register(),
            EMPTY_CIRCUIT_BOARD = REGISTRATE.item("empty_circuit_board", Item::new).register(),
            COATED_CIRCUIT_BOARD = REGISTRATE.item("coated_circuit_board", Item::new).register(),
            ETCHED_CIRCUIT_BOARD = REGISTRATE.item("etched_circuit_board", Item::new).register(),
            CIRCUIT_BOARD = REGISTRATE.item("circuit_board", Item::new).register(),
            TRANSISTOR = REGISTRATE.item("transistor", Item::new).register(),
            CAPACITOR = REGISTRATE.item("capacitor", Item::new).register(),
            COPPER_SULFATE = REGISTRATE.item("copper_sulfate", Item::new).register(),
            LITHIUM_CHARGE = REGISTRATE.item("lithium_charge", Item::new).register(),

            TURBO = REGISTRATE.item("turbo", Item::new).properties(p -> p.component(TFMGDataComponents.ENGINE_UPGRADE, new EngineUpgrade.Stored(TFMGEngineUpgrades.TURBO))).register(),
            GOLDEN_TURBO = REGISTRATE.item("golden_turbo", Item::new).properties(p -> p.component(TFMGDataComponents.ENGINE_UPGRADE, new EngineUpgrade.Stored(TFMGEngineUpgrades.GOLDEN_TURBO))).register(),
            CINDERBLOCK = REGISTRATE.item("cinderblock", Item::new)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.CONCRETE.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 8))
                    .register(),
            CINDERFLOURBLOCK = REGISTRATE.item("cinderflourblock", Item::new).register(),
            NAPALM_POTATO = REGISTRATE.item("napalm_potato", Item::new).register(),
            MIXER_BLADE = REGISTRATE.item("mixer_blade", Item::new).properties(p -> p.component(TFMGDataComponents.MIXER_MODE, new MixerMode.Stored(TFMGMixerModes.MIXING))).register(),
            CENTRIFUGE = REGISTRATE.item("centrifuge", Item::new).properties(p -> p.component(TFMGDataComponents.MIXER_MODE, new MixerMode.Stored(TFMGMixerModes.CENTRIFUGE))).register(),
            CRANKSHAFT = REGISTRATE.item("crankshaft", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/crankshaft_model")))
                    .register(),
            P_SEMICONDUCTOR = REGISTRATE.item("p_semiconductor", Item::new)
                    .lang("P-Semiconductor").register(),
            N_SEMICONDUCTOR = REGISTRATE.item("n_semiconductor", Item::new)
                    .lang("N-Semiconductor").register(),
            UNFINISHED_ELECTROMAGNETIC_COIL = REGISTRATE.item("unfinished_electromagnetic_coil", Item::new).register(),
            COPPER_ELECTRODE = REGISTRATE.item("copper_electrode", Item::new)
                    .properties(p -> p.component(TFMGDataComponents.ELECTRODE, new Electrode.Stored(TFMGElectrodes.COPPER)).stacksTo(1))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.COPPER.storageBlocks.items()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/copper_electrode_model"))).register(),
            ZINC_ELECTRODE = REGISTRATE.item("zinc_electrode", Item::new)
                    .properties(p -> p.component(TFMGDataComponents.ELECTRODE, new Electrode.Stored(TFMGElectrodes.ZINC)).stacksTo(1))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(CommonMetal.ZINC.storageBlocks.items()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/zinc_electrode_model"))).register(),
            GRAPHITE_ELECTRODE = REGISTRATE.item("graphite_electrode", Item::new)
                    .properties(p -> p.component(TFMGDataComponents.ELECTRODE, new Electrode.Stored(TFMGElectrodes.GRAPHITE)).stacksTo(1))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(Items.STORAGE_BLOCKS_COAL_COKE.tag), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/graphite_electrode_model"))).register(),
            UNFIRED_INSULATOR = REGISTRATE.item("unfired_insulator", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/unfired_insulator_model")))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(Blocks.CLAY.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .register(),
            UNFINISHED_INSULATOR = REGISTRATE.item("unfinished_insulator", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/unfinished_insulator_model"))).register(),
            GLASS_INSULATOR_SEGMENT = REGISTRATE.item("glass_insulator_segment", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/glass_insulator_segment_model"))).register();
	
    public static final ItemEntry<MagnetItem>
            MAGNET = REGISTRATE.item("magnet", MagnetItem::new).register();
	
    public static final ItemEntry<AccumulatorCellItem>
            LEAD_ACID_BATTERY = REGISTRATE.item("lead_acid_battery",p-> new AccumulatorCellItem(p.stacksTo(1),1000000)).register(),
            SINGLE_USE_BATTERY = REGISTRATE.item("single_use_battery",p-> new AccumulatorCellItem(p.stacksTo(1),10000)).register(),
            SUPERCAPACITOR = REGISTRATE.item("supercapacitor",p-> new AccumulatorCellItem(p.stacksTo(1),500000)).register(),
            LITHIUM_CELL = REGISTRATE.item("lithium_cell",p-> new AccumulatorCellItem(p.stacksTo(1),1500000)).register();


    public static final ItemEntry<SpoolItem>
		EMPTY_SPOOL = spoolItem("empty", 0x000000)
            .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.HARDENED_PLANKS.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
            .register(),
		STEEL_REINFORCED_CABLE = fullSpoolItem("steel_reinforced", 0x4D5457, CommonMetal.STEEL.nuggets),
		COPPER_SPOOL = fullSpoolItem("copper", 0xD8735A, Items.WIRES_COPPER.tag),
		ALUMINUM_SPOOL = fullSpoolItem("aluminum", 0xEDEFEF, Items.WIRES_ALUMINUM.tag),
		CONSTANTAN_SPOOL = fullSpoolItem("constantan", 0xCFC2A8, Items.WIRES_CONSTANTAN.tag);



    public static final ItemEntry<ElectromagneticCoilItem> ELECTROMAGNETIC_COIL =
            REGISTRATE.item("electromagnetic_coil", ElectromagneticCoilItem::new)
                    .properties(p -> p.stacksTo(1))
                    .register();
    public static final ItemEntry<Item> COAL_COKE_DUST = REGISTRATE.item("coal_coke_dust", Item::new)
            .tag(Items.DUSTS_COAL_COKE.tag, Items.BLAST_FURNACE_FUEL.tag, Tags.Items.DUSTS)
            .register();

    public static final ItemEntry<OilHammerItem> OIL_HAMMER = REGISTRATE.item("oil_hammer", OilHammerItem::new)
            .register();

    public static final ItemEntry<Item> COAL_COKE = REGISTRATE.item("coal_coke", Item::new)
            .model((ctx, prov) -> prov
                    .generated(ctx::get)
                    .override().predicate(TFMG.asResource("april_fools"), 1)
                    .model(prov
                            .getBuilder(ctx.getName() + "_fools")
                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
                            .texture("layer0", TFMG.asResource("item/coal_cake")))
                    .end())
            .register();

    public static final ItemEntry<DebugCinderBlockItem> DEBUG_CINDERBLOCK = REGISTRATE.item("debug_cinderblock", DebugCinderBlockItem::new)
            .properties(p -> p.rarity(Rarity.EPIC).stacksTo(1))
            .register();
    public static final ItemEntry<ScrewdriverItem> SCREWDRIVER = REGISTRATE.item("screwdriver", ScrewdriverItem::new)
            .properties(p -> p.stacksTo(1)
                    .durability(256))
            .register();

    public static final ToolSet
		STEEL_TOOLS = ToolSet.builder(REGISTRATE, "steel", CommonMetal.STEEL.ingots, TFMGTiers.STEEL)
			.all().build(),
		ALUMINUM_TOOLS = ToolSet.builder(REGISTRATE, "aluminum", CommonMetal.ALUMINUM.ingots, TFMGTiers.ALUMINUM)
			.all().build(),
		LEAD_TOOLS = ToolSet.builder(REGISTRATE, "lead", CommonMetal.LEAD.ingots, TFMGTiers.LEAD)
			.sword(LeadSwordItem::new, 2, -2.4f)
			.axe(LeadAxeItem::new, 6F, -3.2F)
			.pickaxe()
			.shovel()
			.hoe()
			.build();

    public static final ItemEntry<LithiumBladeItem> LITHIUM_BLADE =
            REGISTRATE.item("lithium_blade", p -> new LithiumBladeItem(TFMGTiers.STEEL, p))
                    .properties(p -> p.attributes(AxeItem.createAttributes(TFMGTiers.STEEL, 2, -2.4F)))
                    .tag(ItemTags.SWORDS)
                    .model((ctx, prov) -> prov
                            .withExistingParent("lithium_blade", "minecraft:item/handheld")
                            .texture("layer0", "tfmg:item/lithium_blade")
                            .override().predicate(TFMG.asResource("lithium_charged"), 1)
                            .model(prov.withExistingParent("lithium_blade_lit", "minecraft:item/handheld").texture("layer0", "tfmg:item/lithium_blade_lit"))
                    )
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

    public static final ItemEntry<MultimeterItem> MULTIMETER =
		REGISTRATE.item("multimeter", p -> new MultimeterItem(p, 0xCFD300)) //todo: make a more gold-ish color
			.tag(Items.MULTIMETERS.tag, Items.CURIOS_BELT.tag).register();

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

    public static final ItemEntry<Item>
		DIESEL_ENGINE_CYLINDER = cylinder("diesel_engine_cylinder", (ctx, prov) ->
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("   ").pattern("P P").pattern("P P")
                    .define('P', CommonMetal.ALUMINUM.plates)
                    .unlockedBy("has_plate", DataIngredient.tag(CommonMetal.ALUMINUM.plates).getCriterion(prov))
                    .save(prov, ctx.getId().withPrefix("crafting/")), TFMGEngineFuelTypes.DIESEL),
		SIMPLE_ENGINE_CYLINDER = cylinder("simple_engine_cylinder", (ctx, prov) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                        .pattern(" F ").pattern("P P").pattern("P P")
                        .define('P', CommonMetal.IRON.plates)
                        .define('F', net.minecraft.world.item.Items.FLINT_AND_STEEL)
                        .unlockedBy("has_plate", DataIngredient.tag(CommonMetal.IRON.plates).getCriterion(prov))
                        .save(prov, ctx.getId().withPrefix("crafting/")), TFMGEngineFuelTypes.CREOSOTE, TFMGEngineFuelTypes.FURNACE_GAS),
		ENGINE_CYLINDER = cylinder("engine_cylinder", (ctx, prov) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                        .pattern("   ").pattern(" S ").pattern(" C ")
                        .define('S', SPARK_PLUG)
                        .define('C', DIESEL_ENGINE_CYLINDER)
                        .unlockedBy("has_cylinder", DataIngredient.items(DIESEL_ENGINE_CYLINDER.asItem()).getCriterion(prov))
                        .save(prov, ctx.getId().withPrefix("crafting/")), TFMGEngineFuelTypes.GASOLINE, TFMGEngineFuelTypes.KEROSENE, TFMGEngineFuelTypes.NAPHTHA),
		AUTOGAS_ENGINE_CYLINDER = cylinder("autogas_engine_cylinder", (ctx, prov) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                        .pattern("   ").pattern(" C ").pattern(" P ")
                        .define('C', ENGINE_CYLINDER)
                        .define('P', TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getPipe())
                        .unlockedBy("has_cylinder", DataIngredient.items(ENGINE_CYLINDER.asItem()).getCriterion(prov))
                        .save(prov, ctx.getId().withPrefix("crafting/")), TFMGEngineFuelTypes.LPG),
		TURBINE_BLADE = cylinder("turbine_blade", (ctx, prov) ->
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                        .pattern("PPP").pattern("PSP").pattern("PPP")
                        .define('P', CommonMetal.IRON.plates)
                        .define('S', AllBlocks.SHAFT)
                        .unlockedBy("has_plate", DataIngredient.tag(CommonMetal.IRON.plates).getCriterion(prov))
                        .save(prov, ctx.getId().withPrefix("crafting/")), TFMGEngineFuelTypes.KEROSENE);


    public static final ItemEntry<FluidContainingItem>
            OIL_CAN = REGISTRATE.item("oil_can", p -> new FluidContainingItem(p, (s) -> s.is(TFMGTags.Fluids.LUBRICATION_OIL.tag)))
            .properties(p -> p.stacksTo(1))
            .model((c, p) -> p.withExistingParent(c.getName(), TFMG.asResource("item/oil_can_model")))
            .register(),
            COOLING_FLUID_BOTTLE = REGISTRATE.item("cooling_fluid_bottle", p -> new FluidContainingItem(p, (s) -> s.is(TFMGTags.Fluids.COOLING_FLUID.tag)))
                    .properties(p -> p.stacksTo(1))
                    .register();
	
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

    //Dyed Items
    public static final DyedItemList<MultimeterItem> MULTIMETERS = new DyedItemList<>(colour -> {
        String colourName = colour.getSerializedName();
        boolean optional = colour.getId() > 15;
		ICondition[] conditions = optional ? new ICondition[]{new TagFilledCondition(colour.getTag())} : new ICondition[]{};
        return REGISTRATE.item(colourName + "_multimeter", p -> new MultimeterItem(p, colour.getTextureDiffuseColor()))
                .tag(Items.MULTIMETERS.tag, Items.CURIOS_BELT.tag)
                .onRegister(item -> ItemDescription.referKey(item, () -> MULTIMETER))
                .model((ctx, prov) -> prov.generated(ctx::get, TFMG.asResource("item/multimeter/" + colourName)))
                .recipe((c, p) ->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, c.get())
                            .requires(colour.getTag())
                            .requires(Items.MULTIMETERS.tag)
                            .unlockedBy("has_multimeter", RegistrateRecipeProvider.has(Items.MULTIMETERS.tag))
							.save(p.withConditions(conditions), TFMG.asResource("crafting/materials/" + c.getName() + "_from_other_multimeter"))
                )
                .register();
    });

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

    public static final ItemEntry<SteelVerticalGearboxItem> STEEL_VERTICAL_GEARBOX =
            REGISTRATE.item("steel_vertical_gearbox", SteelVerticalGearboxItem::new)
                    .model(AssetLookup.customBlockItemModel("steel_gearbox", "item_vertical"))
                    .lang("Steel Vertical Gearbox")
                    .register();



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

	public static ItemBuilder<SpoolItem, CreateRegistrate> spoolItem(String name, int barColor) {
        return REGISTRATE.item(name + "_spool", p -> new SpoolItem(p, barColor, TFMG.asResource(name)))
                .tag(Items.SPOOLS.tag)
                .properties(p -> p.stacksTo(1));
    }
	
	public static ItemEntry<SpoolItem> fullSpoolItem(String name, int barColor, TagKey<Item> wire) {
		return spoolItem(name, barColor)
            .properties(p -> p.component(TFMGDataComponents.SPOOL_AMOUNT, 1000))
            .recipe((ctx, prov) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                            .pattern("WWW").pattern("WSW").pattern("WWW")
                            .define('W', wire)
                            .define('S', EMPTY_SPOOL)
                            .unlockedBy("has_wire", DataIngredient.tag(wire).getCriterion(prov))
                            .save(prov, ctx.getId().withPrefix("crafting/")))
			.register();
	}
	
	@SafeVarargs
    public static ItemEntry<Item> cylinder(String name, NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateRecipeProvider> recipe, ResourceKey<EngineFuelType>... fuelTypes) {
		return REGISTRATE.item(name, Item::new)
                .properties(p -> p.component(TFMGDataComponents.ENGINE_CYLINDER, new CylinderFuels(Arrays.asList(fuelTypes))))
                .recipe(recipe)
                .register();
	}

    private static ItemEntry<ThermiteGrenadeItem> thermiteGrenade(String name, ThermiteGrenade.ChemicalColor color) {
        return REGISTRATE.item(name, p -> new ThermiteGrenadeItem(p, color))
                .register();
    }

    public static void init() {
    }
}
