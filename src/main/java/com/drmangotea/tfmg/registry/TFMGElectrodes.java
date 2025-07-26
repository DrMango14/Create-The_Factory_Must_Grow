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

    public static void init() {

    }
}
