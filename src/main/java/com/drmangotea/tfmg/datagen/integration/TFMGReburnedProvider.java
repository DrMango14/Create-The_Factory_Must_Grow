package com.drmangotea.tfmg.datagen.integration;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.forsteri.createliquidfuel.core.datagen.LiquidFuelProvider;
import net.minecraft.data.PackOutput;

public class TFMGReburnedProvider extends LiquidFuelProvider {
    public TFMGReburnedProvider(PackOutput output) {
        super(output, TFMG.MOD_ID);
    }

    @Override
    protected void generate() {
        // Maybe these should just be the fluids itself and not tags?

        add(TFMGTags.Fluids.DIESEL.tag)
                .burnTime(30);

        add(TFMGTags.Fluids.GASOLINE.tag)
                .burnTime(20);

        add(TFMGTags.Fluids.HEAVY_OIL.tag)
                .burnTime(10);

        add(TFMGTags.Fluids.KEROSENE.tag)
                .burnTime(25);

        add(TFMGFluids.NAPALM.get())
                .superHeats()
                .burnTime(30);

        add(TFMGTags.Fluids.NAPHTHA.tag)
                .burnTime(35);
    }
}
