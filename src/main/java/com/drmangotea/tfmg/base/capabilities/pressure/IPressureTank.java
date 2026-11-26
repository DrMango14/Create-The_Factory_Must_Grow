package com.drmangotea.tfmg.base.capabilities.pressure;

import com.drmangotea.tfmg.base.pressure.Pressure;

public interface IPressureTank {
    Pressure getPressure();
    int getCapacity();
    int fill(Pressure pressure, boolean simulate);

    Pressure drain(int index, boolean simulate);
    Pressure drain(Pressure pressure, boolean simulate);
}
