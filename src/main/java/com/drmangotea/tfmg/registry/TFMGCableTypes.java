package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.config.TFMGResistivity;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGCableTypes {

    public static final CableTypeEntry<CableType> empty = REGISTRATE.cableType("empty", CableType::new)
            .properties((p) -> p.spool(TFMGItems.COPPER_SPOOL))
            .register();

    public static final CableTypeEntry<CableType> copper = REGISTRATE.cableType("copper", CableType::new)
            .properties((p) -> p.color(0xD8735A).spool(TFMGItems.COPPER_SPOOL))
            .transform(TFMGResistivity.setResistivity(0.00188f))
            .register();

    public static final CableTypeEntry<CableType> aluminum = REGISTRATE.cableType("aluminum", CableType::new)
            .properties((p) -> p.color(0xEDEFEF).spool(TFMGItems.ALUMINUM_SPOOL))
            .transform(TFMGResistivity.setResistivity(0.0027f))
            .register();

    public static final CableTypeEntry<CableType> constantan = REGISTRATE.cableType("constantan", CableType::new)
            .properties((p) -> p.color(0xCFC2A8).spool(TFMGItems.CONSTANTAN_SPOOL))
            .transform(TFMGResistivity.setResistivity(1f))
            .register();

    // Why is this a thing? I'll leave it her in case you do need it. - Krystal
    //public static final CableTypeEntry<CableType> steelReinforcedAluminum = REGISTRATE.cableType("steel_reinforced_aluminum", CableType::new)
    //        .properties((p) -> p.color(0xB8A08D).spool(TFMGItems.COPPER_SPOOL))
    //        .transform(TFMGResistivity.setResistivity(0.0027f))
    //        .register();

    public static void register() {

    }

    //static {
    //    register("empty", new CableType(new CableType.Properties(TFMG.asResource("empty"))));
    //    register("copper", 0.00188f, 0xD8735A, TFMGItems.COPPER_SPOOL);
    //    register("aluminum", 0.0027f, 0xEDEFEF, TFMGItems.ALUMINUM_SPOOL);
    //    register("constantan", 1f, 0xCFC2A8, TFMGItems.CONSTANTAN_SPOOL);
    //}
//
    //private static <T extends CableType> void register(String name, float resistivity, int color, ItemEntry<?> spool) {
    //    ResourceLocation key = TFMG.asResource(name);
    //    CableType cableType = new CableType(new CableType.Properties(key).color(color).spool(spool).resistivity(resistivity));
    //    register(name, cableType);
    //}
//
    //private static <T extends CableType> void register(String name, T type) {
    //    Registry.register(TFMGBuiltinRegistries.CABLE_TYPE, TFMG.asResource(name), type);
    //}
//
    //public static void init() {
    //}
}
