package com.drmangotea.tfmg.base;


import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer.PumpjackContraption;
import com.simibubi.create.content.contraptions.ContraptionType;

public class TFMGContraptions {

        public static final ContraptionType
                PUMPJACK_CONTRAPTION = ContraptionType.register(CreateTFMG.asResource("pumpjack").toString(), PumpjackContraption::new);

        public static void prepare() {}
}
