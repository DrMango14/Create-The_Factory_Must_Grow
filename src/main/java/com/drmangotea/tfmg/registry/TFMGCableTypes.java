package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableTypeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGCableTypes {

    public static final CableTypeEntry<CableType> EMPTY = REGISTRATE.cableType("empty", CableType::new)
            .properties((p) -> p.spool(TFMGItems.COPPER_SPOOL).wire(TFMGItems.COPPER_WIRE::asItem).color(0xffffff).resistance(Double.MAX_VALUE))
            .register();

    public static final CableTypeEntry<CableType> STEEL_REINFORCED = REGISTRATE.cableType("steel_reinforced", CableType::new)
		.properties((p) -> p.color(0x4D5457)
			.spool(TFMGItems.STEEL_REINFORCED_CABLE)
			.wire(TFMGItems.STEEL_NUGGET::asItem)
			.model(TFMGPartialModels.STEEL_REINFORCED_CABLE)
			.resistance(0.0027f)
		).register();

    public static final CableTypeEntry<CableType> COPPER = REGISTRATE.cableType("copper", CableType::new)
		.properties((p) -> p.color(0xD8735A)
			.spool(TFMGItems.COPPER_SPOOL).wire(TFMGItems.COPPER_WIRE::asItem)
			.model(TFMGPartialModels.COPPER_CABLE)
			.resistance(0.00188f)
		).register();

    public static final CableTypeEntry<CableType> ALUMINUM = REGISTRATE.cableType("aluminum", CableType::new)
		.properties((p) -> p.color(0xEDEFEF)
			.spool(TFMGItems.ALUMINUM_SPOOL)
			.wire(TFMGItems.ALUMINUM_WIRE::asItem)
			.model(TFMGPartialModels.ALUMINUM_CABLE)
			.resistance(0.0027f)
		).register();

    public static final CableTypeEntry<CableType> CONSTANTAN = REGISTRATE.cableType("constantan", CableType::new)
		.properties((p) -> p.color(0xCFC2A8).spool(TFMGItems.CONSTANTAN_SPOOL)
			.wire(TFMGItems.CONSTANTAN_WIRE::asItem)
			.model(TFMGPartialModels.CONSTANTAN_CABLE)
			.resistance(1f)
		).register();


    public static void init() {

    }
}
