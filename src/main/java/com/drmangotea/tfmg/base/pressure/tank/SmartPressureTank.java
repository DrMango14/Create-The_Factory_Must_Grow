package com.drmangotea.tfmg.base.pressure.tank;

import com.drmangotea.tfmg.base.capabilities.pressure.PressureTank;
import com.drmangotea.tfmg.base.pressure.Pressure;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SmartPressureTank extends PressureTank {
    private final Consumer<Pressure> updateCallback;

    public SmartPressureTank(int capacity, Consumer<Pressure> updateCallback) {
        super(capacity);
        this.updateCallback = updateCallback;
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        updateCallback.accept(getPressure());
    }

    @Override
    public void setPressure(@NotNull Pressure pressure) {
        super.setPressure(pressure);
        updateCallback.accept(pressure);
    }
}
