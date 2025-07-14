package com.drmangotea.tfmg.base;



import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.PumpjackContraption;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.util.function.Supplier;

import static com.simibubi.create.AllContraptionTypes.BY_LEGACY_NAME;

public class TFMGContraptions {

        public static final Holder.Reference<ContraptionType>
                PUMPJACK_CONTRAPTION = register("pumpjack", PumpjackContraption::new);
        private static Holder.Reference<ContraptionType> register(String name, Supplier<? extends Contraption> factory) {
                ContraptionType type = new ContraptionType(factory);
                BY_LEGACY_NAME.put(name, type);

                return Registry.registerForHolder(CreateBuiltInRegistries.CONTRAPTION_TYPE, TFMG.asResource(name), type);
        }
        public static void prepare() {}
}
