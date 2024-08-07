package com.drmangotea.createindustry.blocks.electricity.api;

import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class EnergyStorage extends SingleVariantStorage<Energy> {


    public EnergyStorage(long i) {
        this.variant = new Energy();
        this.amount = i;
    }

    @Override
    protected Energy getBlankVariant() {
        return null;
    }

    @Override
    protected long getCapacity(Energy variant) {
        return 0;
    }
}
