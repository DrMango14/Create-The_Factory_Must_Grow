package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.config.TFMGResistivity;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableTypeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGCableTypes {

    public static final CableTypeEntry<CableType> EMPTY = REGISTRATE.cableType("empty", CableType::new)
            .properties((p) -> p.spool(TFMGItems.COPPER_SPOOL))
            .register();

    public static final CableTypeEntry<CableType> STEEL_REINFORCED = REGISTRATE.cableType("steel_reinforced", CableType::new)
            .properties((p) -> p.color(0x4D5457)
                    .spool(TFMGItems.STEEL_REINFORCED_CABLE)
                    .wire(TFMGItems.STEEL_NUGGET)
                    .model(TFMGPartialModels.STEEL_REINFORCED_CABLE))
            .transform(TFMGResistivity.setResistivity(0.0027f))
            .register();

    public static final CableTypeEntry<CableType> COPPER = REGISTRATE.cableType("copper", CableType::new)
            .properties((p) -> p.color(0xD8735A)
                    .spool(TFMGItems.COPPER_SPOOL)
                    .wire(TFMGItems.COPPER_WIRE)
                    .model(TFMGPartialModels.COPPER_CABLE))
            .transform(TFMGResistivity.setResistivity(0.00188f))
            .register();

    public static final CableTypeEntry<CableType> ALUMINUM = REGISTRATE.cableType("aluminum", CableType::new)
            .properties((p) -> p.color(0xEDEFEF)
                    .spool(TFMGItems.ALUMINUM_SPOOL)
                    .wire(TFMGItems.ALUMINUM_WIRE)
                    .model(TFMGPartialModels.ALUMINUM_CABLE))
            .transform(TFMGResistivity.setResistivity(0.0027f))
            .register();

    public static final CableTypeEntry<CableType> CONSTANTAN = REGISTRATE.cableType("constantan", CableType::new)
            .properties((p) -> p.color(0xCFC2A8)
                    .spool(TFMGItems.CONSTANTAN_SPOOL)
                    .wire(TFMGItems.CONSTANTAN_WIRE)
                    .model(TFMGPartialModels.CONSTANTAN_CABLE))
            .transform(TFMGResistivity.setResistivity(1f))
            .register();


    public static void init() {

    }
}
