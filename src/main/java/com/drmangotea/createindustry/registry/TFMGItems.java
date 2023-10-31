package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.items.gadgets.explosives.thermite_grenades.ChemicalColor;
import com.drmangotea.createindustry.items.gadgets.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.drmangotea.createindustry.items.gadgets.quad_potato_cannon.QuadPotatoCannonItem;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingMoldItem;
import com.drmangotea.createindustry.items.CoalCokeItem;
import com.drmangotea.createindustry.items.ScrewdriverItem;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;
import static com.drmangotea.createindustry.items.gadgets.explosives.thermite_grenades.ChemicalColor.*;
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
            PLASTIC_SHEET = taggedIngredient("plastic_sheet", forgeItemTag("ingots/plastic"), CREATE_INGOTS.tag)
          //  LEAD_INGOT = taggedIngredient("lead_ingot", forgeItemTag("ingots/lead"), CREATE_INGOTS.tag)
      ;

    public static final ItemEntry<Item>
            REBAR = REGISTRATE.item("rebar", Item::new).register();


    public static final ItemEntry<Item>
            SPARK_PLUG = REGISTRATE.item("spark_plug", Item::new).register(),
            SLAG = REGISTRATE.item("slag", Item::new).register(),
            BITUMEN = REGISTRATE.item("bitumen", Item::new).register(),
            BLASTING_MIXTURE = REGISTRATE.item("blasting_mixture", Item::new).register(),
            FIREPROOF_BRICK = REGISTRATE.item("fireproof_brick", Item::new).register(),
            FIRECLAY_BALL = REGISTRATE.item("fireclay_ball", Item::new).register(),
            SCREW = REGISTRATE.item("screw", Item::new).register(),
            HEAVY_PLATE = REGISTRATE.item("heavy_plate", Item::new).register(),
            ENGINE_CHAMBER = REGISTRATE.item("engine_chamber", Item::new).register(),
            ENGINE_BASE = REGISTRATE.item("engine_base", Item::new)
                    .model((c, p) -> p.withExistingParent(c.getName(), CreateTFMG.asResource("item/engine_base")))
                    .register(),
            TURBINE_BLADE = REGISTRATE.item("turbine_blade", Item::new).register(),
            THERMITE_POWDER = REGISTRATE.item("thermite_powder", Item::new).register(),
            STEEL_MECHANISM = REGISTRATE.item("steel_mechanism", Item::new).register(),
            CHARCOAL_DUST = REGISTRATE.item("charcoal_dust", Item::new).register(),
            NITRATE_DUST = REGISTRATE.item("nitrate_dust", Item::new).register(),
            SULFUR_DUST = REGISTRATE.item("sulfur_dust", Item::new).register();


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

    public static final ItemEntry<Item>
            COAL_COKE_DUST = taggedIngredient("coal_coke_dust", forgeItemTag("dusts/coal_coke"));



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
