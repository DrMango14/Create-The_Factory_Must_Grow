package com.drmangotea.tfmg.content.electricity.storage;


import net.neoforged.neoforge.energy.EnergyStorage;

public abstract class TFMGForgeEnergyStorage extends EnergyStorage {

    public TFMGForgeEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);

    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int oldAmount = this.energy;

        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if(extractedEnergy != 0) {
            onEnergyChanged(maxExtract*-1,oldAmount);
        }
        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int oldAmount = this.energy;
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if(receiveEnergy != 0) {
            onEnergyChanged(receiveEnergy,oldAmount);
        }
        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;

        if(energy>0)
            onEnergyChanged(energy,this.energy);

        return 0;
    }

    public abstract void onEnergyChanged(int amount,int oldAmount);
}