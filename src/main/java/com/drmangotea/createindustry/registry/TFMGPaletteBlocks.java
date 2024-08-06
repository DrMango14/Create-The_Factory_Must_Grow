package com.drmangotea.createindustry.registry;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;

public class TFMGPaletteBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BUILDING_BLOCKS);
    }


    static {
        TFMGPaletteStoneTypes.register(REGISTRATE);
    }

    public static void register() {}

}