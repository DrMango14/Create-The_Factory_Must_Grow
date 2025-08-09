package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.config.TFMGResistivity;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGCableTypes {

    public static final CableTypeEntry<CableType> EMPTY = REGISTRATE.cableType("empty", CableType::new)
            .properties((p) -> p.spool(TFMGItems.COPPER_SPOOL))
            .register();

    public static final CableTypeEntry<CableType> COPPER = REGISTRATE.cableType("copper", CableType::new)
            .properties((p) -> p.color(0xD8735A).spool(TFMGItems.COPPER_SPOOL))
            .transform(TFMGResistivity.setResistivity(0.00188f))
            .register();

    public static final CableTypeEntry<CableType> ALUMINUM = REGISTRATE.cableType("aluminum", CableType::new)
            .properties((p) -> p.color(0xEDEFEF).spool(TFMGItems.ALUMINUM_SPOOL))
            .transform(TFMGResistivity.setResistivity(0.0027f))
            .register();

    public static final CableTypeEntry<CableType> CONSTANTAN = REGISTRATE.cableType("constantan", CableType::new)
            .properties((p) -> p.color(0xCFC2A8).spool(TFMGItems.CONSTANTAN_SPOOL))
            .transform(TFMGResistivity.setResistivity(1f))
            .register();

    // Why is this a thing? I'll leave it here in case you do need it. - Krystal
    //public static final CableTypeEntry<CableType> steelReinforcedAluminum = REGISTRATE.cableType("steel_reinforced_aluminum", CableType::new)
    //        .properties((p) -> p.color(0xB8A08D).spool(TFMGItems.COPPER_SPOOL))
    //        .transform(TFMGResistivity.setResistivity(0.0027f))
    //        .register();

    public static void init() {

    }
}
