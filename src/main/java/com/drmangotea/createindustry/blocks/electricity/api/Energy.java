package com.drmangotea.createindustry.blocks.electricity.api;

import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;

import static org.apache.commons.lang3.ObjectUtils.min;

public class Energy {
    long voltage;
    long current;

    public Energy(long voltage, long current) {
        this.current = current;
        this.voltage = current;
    }

    public long getVoltage() {
        return voltage;
    }

    public long getCurrent() {
        return current;
    }

    public long discharge(long amount, boolean simulate) {

        return amount;
    }

    public long charge(long amount, boolean simulate) {

        return amount;
    }

    public long transfer(IElectricBlock to, long amount, boolean simulate) {
        long discharge = discharge(amount, true);
        long charge = to.getStorage().charge(amount, true);
        return min(discharge, charge);
    }
}
