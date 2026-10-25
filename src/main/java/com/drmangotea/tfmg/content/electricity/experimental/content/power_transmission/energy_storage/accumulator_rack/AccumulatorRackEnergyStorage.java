package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack;


import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.cells.AccumulatorCellItem;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;

import java.util.List;

public abstract class AccumulatorRackEnergyStorage extends EnergyStorage {

    final AccumulatorRackBlockEntity be;

    public AccumulatorRackEnergyStorage(int capacity, int maxTransfer, AccumulatorRackBlockEntity be) {
        super(capacity, maxTransfer);
        this.be = be;

    }

    public void setMaxEnergyStorage(int max) {
        this.capacity = max;
    }

    @Override
    public boolean canExtract() {
        return true;
    }


    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int max = maxExtract;
        int extractedEnergy = 0;

        List<ItemStack> cells = be.getAllStorageItems();
        for (ItemStack s : cells) {
            int energy = s.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE, 0);

            int toExtract = Math.min(max, energy);
            if (!simulate)
                s.set(TFMGDataComponents.ACCUMULATOR_STORAGE, energy -toExtract);
            max -= toExtract;
            extractedEnergy += toExtract;
        }


        be.updateStorage();
        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(maxReceive == 0)
            return 0;

        int max = maxReceive;
        int receiveEnergy = 0;
        List<ItemStack> cells = be.getAllStorageItems();
        for (ItemStack s : cells) {
            int maxStorage = ((AccumulatorCellItem) s.getItem()).capacity;
            int energy = s.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE, 0);


            int toRecieve = Math.min(max, maxStorage - energy);
            if (!simulate)
                s.set(TFMGDataComponents.ACCUMULATOR_STORAGE, energy + toRecieve);
            max -= toRecieve;
            receiveEnergy += toRecieve;
        }


        be.updateStorage();
        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;

        if (energy > 0)
            onEnergyChanged(energy, this.energy);

        return 0;
    }

    public abstract void onEnergyChanged(int amount, int oldAmount);
}