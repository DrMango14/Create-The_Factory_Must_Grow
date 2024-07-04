package com.drmangotea.createindustry.ponder;



import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.foundation.ponder.PonderTag;
import net.minecraft.resources.ResourceLocation;

public class TFMGPonderTag extends PonderTag {

        public static final PonderTag OIL = create("oil")
                .defaultLang("Oil Related Machines", "Machines used to get, refine and use oil")
                .item(TFMGFluids.CRUDE_OIL.getBucket().get(), true, false).addToIndex();

    public static final PonderTag METALLURGY = create("metallurgy")
            .defaultLang("Metal processing", "Machines related to metal")
            .item(TFMGItems.STEEL_INGOT.get(), true, false).addToIndex();

    public static final PonderTag ELECTRICITY = create("electricity")
            .defaultLang("Electric Machinery", "")
            .item(TFMGItems.STEEL_INGOT.get(), true, false).addToIndex();

        public TFMGPonderTag(ResourceLocation id) {
            super(id);
        }

        private static PonderTag create(String id) {
            return new PonderTag(CreateTFMG.asResource(id));
        }
    }

