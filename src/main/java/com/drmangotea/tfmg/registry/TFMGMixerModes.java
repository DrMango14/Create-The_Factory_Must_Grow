package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerModels;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerMode;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerModeEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGMixerModes {

    public static final MixerModeEntry<MixerMode> NONE = REGISTRATE.mixerMode("none", MixerMode::new)
            .properties((p) -> p)
            .register();

    public static final MixerModeEntry<MixerMode> MIXING = REGISTRATE.mixerMode("mixing", MixerMode::new)
            .properties((p) -> p.operation(TFMGVatOperations.MIXER).partial(IndustrialMixerModels::getMixerModel))
            .register();

    public static final MixerModeEntry<MixerMode> CENTRIFUGE = REGISTRATE.mixerMode("centrifuge", MixerMode::new)
            .properties((p) -> p.operation(TFMGVatOperations.CENTRIFUGE).partial(IndustrialMixerModels::getCentrifugeModel))
            .register();

    public static void init() { }
}
