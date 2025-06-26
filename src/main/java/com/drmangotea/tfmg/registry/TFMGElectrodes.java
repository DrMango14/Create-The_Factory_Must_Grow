package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.ElectrodeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGElectrodes {

    public static final ElectrodeEntry<Electrode> none = REGISTRATE.electrode("none", Electrode::new)
            .properties((p) -> p)
            .register();

    public static final ElectrodeEntry<Electrode> copper = REGISTRATE.electrode("copper", Electrode::new)
            .properties((p) -> p
                    .resistance(100)
                    .item(TFMGItems.COPPER_ELECTRODE)
                    .operationId("tfmg:electrode")
            )
            .register();

    public static final ElectrodeEntry<Electrode> zinc = REGISTRATE.electrode("zinc", Electrode::new)
            .properties((p) -> p
                    .resistance(100)
                    .item(TFMGItems.ZINC_ELECTRODE)
                    .operationId("tfmg:electrode")
            )
            .register();

    public static final ElectrodeEntry<Electrode> graphite = REGISTRATE.electrode("graphite", Electrode::new)
            .properties((p) -> p
                    .resistance(300)
                    .item(TFMGItems.GRAPHITE_ELECTRODE)
                    .operationId("tfmg:graphite_electrode")
            )
            .register();

    //static {
    //    register("none", new Electrode(new Electrode.Properties(TFMG.asResource("none"))));
    //    register("copper", 100, TFMGItems.COPPER_ELECTRODE, "tfmg:electrode");
    //    register("zinc", 100, TFMGItems.ZINC_ELECTRODE, "tfmg:electrode");
    //    register("graphite", 300, TFMGItems.GRAPHITE_ELECTRODE, "tfmg:graphite_electrode");
    //}
//
    //private static <T extends Electrode> void register(String name, int resistance, ItemEntry<?> item, String operationId) {
    //    ResourceLocation key = TFMG.asResource(name);
    //    Electrode electrode = new Electrode(new Electrode.Properties(key).resistance(resistance).item(item).operationId(operationId));
    //    register(name, electrode);
    //}
//
    //private static <T extends Electrode> void register(String name, T type) {
    //    Registry.register(TFMGBuiltinRegistries.ELECTRODE, TFMG.asResource(name), type);
    //}

    public static void register() {

    }
}
