package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.decoration.kinetics.SteelVerticalGearboxItem;
import com.drmangotea.createindustry.blocks.electricity.base.cables.WireItem;
import com.drmangotea.createindustry.blocks.electricity.base.cables.WireManager;
import com.drmangotea.createindustry.items.*;
import com.drmangotea.createindustry.items.weapons.LeadAxeItem;
import com.drmangotea.createindustry.items.weapons.LeadSwordItem;
import com.drmangotea.createindustry.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonItem;
import com.drmangotea.createindustry.items.weapons.explosives.pipe_bomb.PipeBombItem;
import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.ChemicalColor;
import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.drmangotea.createindustry.items.weapons.flamethrover.FlamethrowerItem;
import com.drmangotea.createindustry.items.weapons.lithium_blade.LitLithiumBladeItem;
import com.drmangotea.createindustry.items.weapons.lithium_blade.LithiumBladeItem;
import com.drmangotea.createindustry.items.weapons.quad_potato_cannon.QuadPotatoCannonItem;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingMoldItem;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.gearbox.VerticalGearboxItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;
import static com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.ChemicalColor.*;
import static com.simibubi.create.AllTags.AllItemTags.CREATE_INGOTS;
import static com.simibubi.create.AllTags.forgeItemTag;

public class TFMGItems {

    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }



    public static final ItemEntry<Item>
            STEEL_INGOT = taggedIngredient("steel_ingot", forgeItemTag("ingots/steel"), CREATE_INGOTS.tag),
            CAST_IRON_INGOT = taggedIngredient("cast_iron_ingot", forgeItemTag("ingots/cast_iron"), CREATE_INGOTS.tag),
            ALUMINUM_INGOT = taggedIngredient("aluminum_ingot", forgeItemTag("ingots/aluminum"), CREATE_INGOTS.tag),
            PLASTIC_SHEET = taggedIngredient("plastic_sheet", forgeItemTag("ingots/plastic"), CREATE_INGOTS.tag),

            HEAVY_PLATE = taggedIngredient("heavy_plate",forgeItemTag("plates/steel")),
            LEAD_INGOT = taggedIngredient("lead_ingot", forgeItemTag("ingots/lead"), CREATE_INGOTS.tag),

            NICKEL_INGOT = taggedIngredient("nickel_ingot", forgeItemTag("ingots/nickel"), CREATE_INGOTS.tag),

            LITHIUM_INGOT = taggedIngredient("lithium_ingot", forgeItemTag("ingots/lithium"), CREATE_INGOTS.tag),

            RAW_LEAD = taggedIngredient("raw_lead", forgeItemTag("raw_materials/lead"), forgeItemTag("raw_materials")),
            RAW_NICKEL = taggedIngredient("raw_nickel", forgeItemTag("raw_materials/nickel"), forgeItemTag("raw_materials")),
            RAW_LITHIUM = taggedIngredient("raw_lithium", forgeItemTag("raw_materials/lithium"), forgeItemTag("raw_materials")),

            COPPER_WIRE = taggedIngredient("copper_wire",forgeItemTag("wires/copper")),
            ALUMINUM_WIRE = taggedIngredient("aluminum_wire",forgeItemTag("wires/aluminum")),

            SYNTHETIC_LEATHER = taggedIngredient("synthetic_leather", Tags.Items.LEATHER),

            SYNTHETIC_STRING = taggedIngredient("synthetic_string", Tags.Items.STRING)








      ;

    public static final ItemEntry<ArmorItem>
                STEEL_HELMET = armor("steel_helmet",TFMGArmorMaterials.STEEL,EquipmentSlot.HEAD),
                STEEL_CHESTPLATE = armor("steel_chestplate",TFMGArmorMaterials.STEEL,EquipmentSlot.CHEST),
                STEEL_LEGGINGS = armor("steel_leggings",TFMGArmorMaterials.STEEL,EquipmentSlot.LEGS),
                STEEL_BOOTS = armor("steel_boots",TFMGArmorMaterials.STEEL,EquipmentSlot.FEET);

    public static final ItemEntry<WireItem> COPPER_CABLE =
            REGISTRATE.item("copper_cable",p->new WireItem(p, WireManager.Conductor.COPPER))
                    .register();

    public static final ItemEntry<SteelVerticalGearboxItem> STEEL_VERTICAL_GEARBOX =
            REGISTRATE.item("steel_vertical_gearbox", SteelVerticalGearboxItem::new)
                    .model(AssetLookup.customBlockItemModel("steel_gearbox", "item_vertical"))
                    .lang("Steel Vertical Gearbox")
                    .register();
    public static final ItemEntry<Item>
            REBAR = REGISTRATE.item("rebar", Item::new).register();


    public static final ItemEntry<SwordItem>
            STEEL_SWORD = toolset("steel",TFMGTiers.STEEL),
            ALUMINUM_SWORD = toolset("aluminum",TFMGTiers.ALUMINUM);

    public static final ItemEntry<LeadSwordItem> LEAD_SWORD = leadToolset();


    public static final ItemEntry<LithiumBladeItem> LITHIUM_BLADE =
            REGISTRATE.item("lithium_blade",p -> new LithiumBladeItem(TFMGTiers.STEEL,3, -2.4F,p))
                    .model((ctx, prov) -> prov
                            .withExistingParent("lithium_blade","minecraft:item/handheld")
                            .texture("layer0","createindustry:item/lithium_blade"))
                    .register();


    public static final ItemEntry<LitLithiumBladeItem> LIT_LITHIUM_BLADE =
            REGISTRATE.item("lit_lithium_blade",p -> new LitLithiumBladeItem(TFMGTiers.STEEL,4, -2.4F,p))
                    .model((ctx, prov) -> prov
                            .withExistingParent("lit_lithium_blade","minecraft:item/handheld")
                            .texture("layer0","createindustry:item/lithium_blade_lit"))
                    .lang("Lithium Blade")
                    .register();

    public static final ItemEntry<Item>
            SPARK_PLUG = REGISTRATE.item("spark_plug", Item::new).register(),
            SLAG = REGISTRATE.item("slag", Item::new).register(),
            BITUMEN = REGISTRATE.item("bitumen", Item::new).register(),
            BLASTING_MIXTURE = REGISTRATE.item("blasting_mixture", Item::new).register(),
            FIREPROOF_BRICK = REGISTRATE.item("fireproof_brick", Item::new).register(),
            FIRECLAY_BALL = REGISTRATE.item("fireclay_ball", Item::new).register(),
            SCREW = REGISTRATE.item("screw", Item::new).register(),

            ENGINE_CHAMBER = REGISTRATE.item("engine_chamber", Item::new).register(),
            ENGINE_BASE = REGISTRATE.item("engine_base", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), CreateTFMG.asResource("item/unfinished_engine")))
                    .register(),
            TURBINE_BLADE = REGISTRATE.item("turbine_blade", Item::new).register(),
            THERMITE_POWDER = REGISTRATE.item("thermite_powder", Item::new).register(),
            STEEL_MECHANISM = REGISTRATE.item("steel_mechanism", Item::new).register(),

            NITRATE_DUST = REGISTRATE.item("nitrate_dust", Item::new).register(),
            SULFUR_DUST = REGISTRATE.item("sulfur_dust", Item::new).register(),

            LIMESAND = REGISTRATE.item("limesand", Item::new).register(),

            CONCRETE_MIXTURE = REGISTRATE.item("concrete_mixture", Item::new).register(),

            MAGNETIC_INGOT = REGISTRATE.item("magnetic_ingot", Item::new).register(),

            RESISTOR = REGISTRATE.item("resistor_", Item::new).lang("Resistor").register(),

            CAPACITOR = REGISTRATE.item("capacitor_", Item::new).lang("Capacitor").register(),

            ZINC_SULFATE = REGISTRATE.item("zinc_sulfate", Item::new).register(),

            COPPER_SULFATE = REGISTRATE.item("copper_sulfate", Item::new).register(),

            LITHIUM_CHARGE = REGISTRATE.item("lithium_charge", Item::new).register(),

            CINDERBLOCK = REGISTRATE.item("cinderblock", Item::new)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.CONCRETE.get()), c::get, 4))
                    .register(),

            CINDERFLOURBLOCK = REGISTRATE.item("cinderflourblock", Item::new).register()


                    ;

    public static final ItemEntry<BatteryAcidBottleItem>
        BOTTLE_OF_BATTERY_ACID = REGISTRATE.item("bottle_of_battery_acid", BatteryAcidBottleItem::new).lang("Bottle o' Battery Acid")
            .properties(p -> p.stacksTo(16))
            .register();
    public static final ItemEntry<ConcreteBottleItem>
        BOTTLE_OF_CONCRETE = REGISTRATE.item("bottle_of_concrete", ConcreteBottleItem::new)
            .properties(p -> p.stacksTo(16))
            .register();


    public static final ItemEntry<SequencedAssemblyItem>

            UNFINISHED_STEEL_MECHANISM = sequencedIngredient("unfinished_steel_mechanism"),
            UNPROCESSED_HEAVY_PLATE = sequencedIngredient("unprocessed_heavy_plate");

    public static final ItemEntry<SequencedAssemblyItem>
    UNFINISHED_GASOLINE_ENGINE = REGISTRATE.item("unfinished_gasoline_engine", SequencedAssemblyItem::new)
            .model((c, p) -> p.withExistingParent(c.getName(), CreateTFMG.asResource("item/unfinished_engine")))
            .register(),
    UNFINISHED_LPG_ENGINE = REGISTRATE.item("unfinished_lpg_engine", SequencedAssemblyItem::new)
            .model((c, p) -> p.withExistingParent(c.getName(), CreateTFMG.asResource("item/unfinished_engine")))
            .register(),
    UNFINISHED_TURBINE_ENGINE = REGISTRATE.item("unfinished_turbine_engine", SequencedAssemblyItem::new)
            .model((c, p) -> p.withExistingParent(c.getName(), CreateTFMG.asResource("item/unfinished_engine")))
            .register();

    public static final ItemEntry<CoalCokeItem>
            COAL_COKE_DUST = REGISTRATE.item("coal_coke_dust", CoalCokeItem::new)
            .tag(forgeItemTag("dusts/coal_coke"))
            .register();

    public static final ItemEntry<PipeBombItem>
            PIPE_BOMB = REGISTRATE.item("pipe_bomb",PipeBombItem::new)
            .register();
    public static final ItemEntry<Item>
            NAPALM_POTATO = REGISTRATE.item("napalm_potato",Item::new)
            .register();


    public static final ItemEntry<AdvancedPotatoCannonItem> ADVANCED_POTATO_CANNON =
            REGISTRATE.item("advanced_potato_cannon", AdvancedPotatoCannonItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .register();

    public static final ItemEntry<FlamethrowerItem> FLAMETHROWER =
            REGISTRATE.item("flamethrower", FlamethrowerItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .properties(p->p.stacksTo(1))
                    .register();


    public static final ItemEntry<QuadPotatoCannonItem> QUAD_POTATO_CANNON =
            REGISTRATE.item("quad_potato_cannon", QuadPotatoCannonItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .register();
    public static final ItemEntry<CastingMoldItem>
            BLOCK_MOLD = REGISTRATE.item("block_mold", p -> new CastingMoldItem(p, CastingBasinBlockEntity.MoldType.BLOCK)).properties(p -> p.stacksTo(1)).register(),
            INGOT_MOLD = REGISTRATE.item("ingot_mold", p -> new CastingMoldItem(p, CastingBasinBlockEntity.MoldType.INGOT)).properties(p -> p.stacksTo(1)).register();
    public static final ItemEntry<CoalCokeItem> COAL_COKE = REGISTRATE.item("coal_coke", CoalCokeItem::new)
            .register();

    public static final ItemEntry<ScrewdriverItem> SCREWDRIVER = REGISTRATE.item("screwdriver", ScrewdriverItem::new)
            .properties(p -> p.stacksTo(1)
                    .durability(256))
            .register();
    public static final ItemEntry<ThermiteGrenadeItem>
            THERMITE_GRENADE = thermiteGrenade("thermite_grenade",BASE);
    public static final ItemEntry<ThermiteGrenadeItem>
            ZINC_GRENADE = thermiteGrenade("zinc_grenade",GREEN);
    public static final ItemEntry<ThermiteGrenadeItem>
            COPPER_GRENADE = thermiteGrenade("copper_grenade",BLUE);



//////////////////////////


    private static ItemEntry<ArmorItem>  armor(String name, ArmorMaterial material, EquipmentSlot slot){
        return REGISTRATE.item(name,p -> new ArmorItem(material,slot,p)).register();
    }

    private static ItemEntry<SwordItem> toolset(String material, Tier tier){

        REGISTRATE.item(material+"_axe",p -> new AxeItem(tier,6.0F, -3.2F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent(material+"_axe","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/"+material+"_axe"))
                .register();
        REGISTRATE.item(material+"_hoe",p -> new HoeItem(tier,0, -3.0F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent(material+"_hoe","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/"+material+"_hoe"))
                .register();
        REGISTRATE.item(material+"_shovel",p -> new ShovelItem(tier,1.5F, -3.0F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent(material+"_shovel","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/"+material+"_shovel"))
                .register();
        REGISTRATE.item(material+"_pickaxe",p -> new PickaxeItem(tier,1, -2.8F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent(material+"_pickaxe","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/"+material+"_pickaxe"))
                .register();

        return  REGISTRATE.item(material+"_sword",p -> new SwordItem(tier,3, -2.4F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent(material+"_sword","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/"+material+"_sword"))
                .register();


    }



    private static ItemEntry<LeadSwordItem> leadToolset(){

        REGISTRATE.item("lead_axe",p -> new LeadAxeItem(TFMGTiers.LEAD,6.0F, -3.2F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_axe","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/lead_axe"))
                .register();;
        REGISTRATE.item("lead_hoe",p -> new HoeItem(TFMGTiers.LEAD,0, -3.0F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_hoe","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/lead_hoe"))
                .register();
        REGISTRATE.item("lead_shovel",p -> new ShovelItem(TFMGTiers.LEAD,1.5F, -3.0F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_shovel","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/lead_shovel"))
                .register();
        REGISTRATE.item("lead_pickaxe",p -> new PickaxeItem(TFMGTiers.LEAD,1, -2.8F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_pickaxe","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/lead_pickaxe"))
                .register();

        return  REGISTRATE.item("lead_sword",p -> new LeadSwordItem(TFMGTiers.LEAD,3, -2.4F,p))
                .model((ctx, prov) -> prov
                        .withExistingParent("lead_sword","minecraft:item/handheld")
                        .texture("layer0","createindustry:item/lead_sword"))
                .register();


    }

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }
    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }

    private static ItemEntry<ThermiteGrenadeItem> thermiteGrenade(String name, ChemicalColor color) {
        return REGISTRATE.item(name,  p -> new ThermiteGrenadeItem(p, color))
                .register();
    }


    public static void register() {}
}
