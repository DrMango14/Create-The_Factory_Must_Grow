package com.drmangotea.tfmg.base.pressure.tank;

import com.drmangotea.tfmg.base.capabilities.pressure.IPressureHandler;
import com.drmangotea.tfmg.base.pressure.Pressure;
import net.createmod.catnip.data.Iterate;

public class CombinedPressureTankWrapper implements IPressureHandler {
    protected static final IPressureHandler emptyHandler = new IPressureHandler() {
        @Override
        public int getTanks() {
            return 0;
        }

        @Override
        public Pressure getPressureInTank(int index) {
            return Pressure.EMPTY;
        }

        @Override
        public int getTankCapacity(int index) {
            return 0;
        }

        @Override
        public int fill(Pressure pressure, boolean simulate) {
            return 0;
        }

        @Override
        public Pressure drain(Pressure pressure, boolean simulate) {
            return Pressure.EMPTY;
        }

        @Override
        public Pressure drain(int maxDrain, boolean simulate) {
            return Pressure.EMPTY;
        }
    };

    protected final IPressureHandler[] pressureHandler;
    protected final int[] baseIndex;
    protected final int tankCount;

    public CombinedPressureTankWrapper(IPressureHandler... pressureHandlers) {
        this.pressureHandler = pressureHandlers;
        this.baseIndex = new int[pressureHandlers.length];
        int index = 0;
        for (int i = 0; i < pressureHandlers.length; i++) {
            index += pressureHandlers[i].getTanks();
            baseIndex[i] = index;
        }
        this.tankCount = index;
    }

    @Override
    public int getTanks() {
        return tankCount;
    }

    @Override
    public Pressure getPressureInTank(int tank) {
        int index = getIndexForSlot(tank);
        IPressureHandler handler = getHandlerFromIndex(index);
        tank = getSlotFromIndex(tank, index);
        return handler.getPressureInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        int index = getIndexForSlot(tank);
        IPressureHandler handler = getHandlerFromIndex(index);
        int localSlot = getSlotFromIndex(tank, index);
        return handler.getTankCapacity(localSlot);
    }

    @Override
    public int fill(Pressure pressure, boolean simulate) {
        if (pressure.isEmpty())
            return 0;

        int filled = 0;
        pressure = pressure.copy();

        boolean fittingHandlerFound = false;
        Outer:
        for (boolean searchPass : Iterate.trueAndFalse) {
            for (IPressureHandler iPressureHandler : pressureHandler) {

                for (int i = 0; i < iPressureHandler.getTanks(); i++)
                    if (searchPass && !iPressureHandler.getPressureInTank(i)
                        .isEmpty())
                        fittingHandlerFound = true;

                if (searchPass && !fittingHandlerFound)
                    continue;

                int filledIntoCurrent = iPressureHandler.fill(pressure, simulate);
                pressure.shrink(filledIntoCurrent);
                filled += filledIntoCurrent;

                if (pressure.isEmpty())
                    break Outer;
                if (fittingHandlerFound && filledIntoCurrent != 0)
                    break Outer;
            }
        }

        return filled;
    }

    @Override
    public Pressure drain(Pressure pressure, boolean simulate) {
        if (pressure.isEmpty())
            return Pressure.EMPTY;

        Pressure drained = Pressure.EMPTY;
        pressure = pressure.copy();

        for (IPressureHandler iPressureHandler : pressureHandler) {
            Pressure drainedFromCurrent = iPressureHandler.drain(pressure, simulate);
            int amount = drainedFromCurrent.getValue();
            pressure.shrink(amount);

            if (!drainedFromCurrent.isEmpty())
                drained = drainedFromCurrent.copyWithAmount(amount + drained.getValue());
            if (pressure.isEmpty())
                break;
        }

        return drained;
    }

    @Override
    public Pressure drain(int maxDrain, boolean simulate) {
        Pressure drained = Pressure.EMPTY;

        for (IPressureHandler iPressureHandler : pressureHandler) {
            Pressure drainedFromCurrent = iPressureHandler.drain(maxDrain, simulate);
            int amount = drainedFromCurrent.getValue();
            maxDrain -= amount;

            if (!drainedFromCurrent.isEmpty())
                drained = drainedFromCurrent.copyWithAmount(amount + drained.getValue());
            if (maxDrain == 0)
                break;
        }

        return drained;
    }

    protected int getIndexForSlot(int slot) {
        if (slot < 0)
            return -1;
        for (int i = 0; i < baseIndex.length; i++)
            if (slot - baseIndex[i] < 0)
                return i;
        return -1;
    }

    protected IPressureHandler getHandlerFromIndex(int index) {
        if (index < 0 || index >= pressureHandler.length)
            return emptyHandler;
        return pressureHandler[index];
    }

    protected int getSlotFromIndex(int slot, int index) {
        if (index <= 0 || index >= baseIndex.length)
            return slot;
        return slot - baseIndex[index - 1];
    }
}
