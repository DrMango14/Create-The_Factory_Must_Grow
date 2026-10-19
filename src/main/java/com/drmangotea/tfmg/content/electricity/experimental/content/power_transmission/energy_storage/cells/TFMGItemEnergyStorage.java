package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.cells;


import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;

public abstract class TFMGItemEnergyStorage extends EnergyStorage {

    public final ItemStack stack ;

    public TFMGItemEnergyStorage(int capacity, int maxTransfer, ItemStack stack) {
        super(capacity, maxTransfer);
        this.stack = stack;
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
        this.energy = stack.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE,0);

        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        stack.set(TFMGDataComponents.ACCUMULATOR_STORAGE,Math.max(0,this.energy-extractedEnergy));
        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        this.energy = stack.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE,0);
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);

        stack.set(TFMGDataComponents.ACCUMULATOR_STORAGE,Math.min(getMaxEnergyStored(),this.energy+receiveEnergy));

        return receiveEnergy;
    }



    public abstract void onEnergyChanged(int amount,int oldAmount);
}