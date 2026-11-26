package com.drmangotea.tfmg.base.capabilities.pressure;

import com.drmangotea.tfmg.base.pressure.Pressure;

public interface IPressureHandler {
    int getTanks();
    Pressure getPressureInTank(int index);
    int getTankCapacity(int index);
    int fill(Pressure pressure, boolean simulate);

    Pressure drain(Pressure pressure, boolean simulate);
    Pressure drain(int maxDrain, boolean simulate);
}
