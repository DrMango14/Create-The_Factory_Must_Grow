package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.ChemicalColor;
import com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.tterrag.registrate.util.entry.ItemEntry;

import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;
import static com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.ChemicalColor.*;

public class TFMGItems {

    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }

    public static final ItemEntry<ThermiteGrenadeItem>
            THERMITE_GRENADE = thermiteGrenade("thermite_grenade",BASE);
    public static final ItemEntry<ThermiteGrenadeItem>
            ZINC_GRENADE = thermiteGrenade("zinc_grenade",GREEN);
    public static final ItemEntry<ThermiteGrenadeItem>
            COPPER_GRENADE = thermiteGrenade("copper_grenade",BLUE);

//////////////////////////
    private static ItemEntry<ThermiteGrenadeItem> thermiteGrenade(String name, ChemicalColor color) {
        return REGISTRATE.item(name,  p -> new ThermiteGrenadeItem(p, color))
                .register();
    }


    public static void register() {}
}
