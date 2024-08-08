package com.drmangotea.createindustry.base.util;

import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;

import java.util.Arrays;

public class CombinedStorageWrapper extends CombinedStorage<ItemVariant, Storage<ItemVariant>> {

    @SafeVarargs
    public CombinedStorageWrapper(Storage<ItemVariant>... parts) {
        super(Arrays.stream(parts).toList());
    }

//    public CombinedStorageWrapper(SmartInventory... invs) {
//        super();
//    }
}
