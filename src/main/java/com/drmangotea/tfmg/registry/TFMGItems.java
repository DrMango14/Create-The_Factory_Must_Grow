package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.items.gadgets.explosives.thermite_grenades.ChemicalColor;
import com.drmangotea.tfmg.items.gadgets.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.drmangotea.tfmg.items.gadgets.quad_potato_cannon.QuadPotatoCannonItem;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_basin.CastingMoldItem;
import com.drmangotea.tfmg.items.CoalCokeItem;
import com.drmangotea.tfmg.items.ScrewdriverItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;
import static com.drmangotea.tfmg.items.gadgets.explosives.thermite_grenades.ChemicalColor.*;
import static com.simibubi.create.AllTags.AllItemTags.CREATE_INGOTS;
import static com.simibubi.create.AllTags.forgeItemTag;

public class TFMGItems {

    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }



    public static final ItemEntry<Item>
            STEEL_INGOT = taggedIngredient("steel_ingot", forgeItemTag("ingots/steel"), CREATE_INGOTS.tag),
            CAST_IRON_INGOT = taggedIngredient("cast_iron_ingot", forgeItemTag("ingots/cast_iron"), CREATE_INGOTS.tag),
            ALUMINUM_INGOT = taggedIngredient("aluminum_ingot", forgeItemTag("ingots/aluminum"), CREATE_INGOTS.tag)
          //  LEAD_INGOT = taggedIngredient("lead_ingot", forgeItemTag("ingots/lead"), CREATE_INGOTS.tag)
      ;

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

    private static ItemEntry<ThermiteGrenadeItem> thermiteGrenade(String name, ChemicalColor color) {
        return REGISTRATE.item(name,  p -> new ThermiteGrenadeItem(p, color))
                .register();
    }


    public static void register() {}
}
