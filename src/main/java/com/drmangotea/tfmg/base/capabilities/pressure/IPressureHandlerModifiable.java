package com.drmangotea.tfmg.base.capabilities.pressure;

import com.drmangotea.tfmg.base.pressure.Pressure;

public interface IPressureHandlerModifiable extends IPressureHandler {
    void setPressureInTank(int index, Pressure pressure);
}
