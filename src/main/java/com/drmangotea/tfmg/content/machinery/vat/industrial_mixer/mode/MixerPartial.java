package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode;

import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

@FunctionalInterface
public interface MixerPartial {
    PartialModel getPartial(int currentHeight, int totalHeight, IndustrialMixerBlockEntity mixer);
}
