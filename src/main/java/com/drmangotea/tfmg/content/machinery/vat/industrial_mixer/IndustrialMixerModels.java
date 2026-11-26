package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class IndustrialMixerModels {
    public static PartialModel getCentrifugeModel(int currentHeight, int totalHeight, IndustrialMixerBlockEntity be) {
        boolean large = be.vatSize > 1;

        return switch (totalHeight == 1 ? 0 : currentHeight == 0 ? 1 : currentHeight == totalHeight ? 2 : 3) {
            case 0 -> large ? TFMGPartialModels.LARGE_CENTRIFUGE_ALONE : TFMGPartialModels.SMALL_CENTRIFUGE_ALONE;
            case 1 -> large ? TFMGPartialModels.LARGE_CENTRIFUGE_TOP : TFMGPartialModels.SMALL_CENTRIFUGE_TOP;
            case 2 -> large ? TFMGPartialModels.LARGE_CENTRIFUGE_BOTTOM : TFMGPartialModels.SMALL_CENTRIFUGE_BOTTOM;
            default -> large ? TFMGPartialModels.LARGE_CENTRIFUGE_MIDDLE : TFMGPartialModels.SMALL_CENTRIFUGE_MIDDLE;
        };
    }

    public static PartialModel getMixerModel(int i, int height, IndustrialMixerBlockEntity be) {
        return i == height - 1
                ? (be.vatSize > 1 ? TFMGPartialModels.MIXER : TFMGPartialModels.SMALL_MIXER)
                : TFMGPartialModels.MIXER_SHAFT;
    }
}
