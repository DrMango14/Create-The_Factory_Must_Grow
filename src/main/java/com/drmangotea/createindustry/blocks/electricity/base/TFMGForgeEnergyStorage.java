package com.drmangotea.createindustry.blocks.electricity.base;

import net.minecraftforge.energy.EnergyStorage;

public abstract class TFMGForgeEnergyStorage extends EnergyStorage {







    public TFMGForgeEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);

    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if(extractedEnergy != 0) {
            onEnergyChanged(maxExtract*-1,false);
        }

        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if(receiveEnergy != 0) {
            onEnergyChanged(receiveEnergy,false);
        }




        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;

        if(energy>0)
            onEnergyChanged(energy,true);

        return 0;
    }

    public abstract void onEnergyChanged(int amount, boolean setEnergy);
}