package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackContraption;
import com.simibubi.create.content.contraptions.ContraptionType;

public class TFMGContraptions {

        public static final ContraptionType
                PUMPJACK_CONTRAPTION = ContraptionType.register(CreateTFMG.asResource("pumpjack").toString(), PumpjackContraption::new);

        public static void prepare() {}
}
