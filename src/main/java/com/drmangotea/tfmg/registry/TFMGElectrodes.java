package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.ArcElectrode;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.ElectrodeEntry;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.SparkingElectrode;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGElectrodes {
    public static final ElectrodeEntry<Electrode> NONE = REGISTRATE.electrode("none", Electrode::new)
            .properties((p) -> p)
            .register();

    public static final ElectrodeEntry<SparkingElectrode> COPPER = REGISTRATE.electrode("copper", SparkingElectrode::new)
            .properties((p) -> p.resistance(10).operationId(TFMGVatOperations.ELECTRODE))
            .register();

    public static final ElectrodeEntry<SparkingElectrode> ZINC = REGISTRATE.electrode("zinc", SparkingElectrode::new)
            .properties((p) -> p.resistance(10).operationId(TFMGVatOperations.ELECTRODE))
            .register();

    public static final ElectrodeEntry<ArcElectrode> GRAPHITE = REGISTRATE.electrode("graphite", ArcElectrode::new)
            .properties((p) -> p.resistance(300))
            .register();

    public static void init() { }
}
