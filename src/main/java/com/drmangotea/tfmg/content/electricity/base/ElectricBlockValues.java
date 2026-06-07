package com.drmangotea.tfmg.content.electricity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ElectricBlockValues {


    public long electricalNetworkId;

    public boolean destroyed = false;

    public boolean connectNextTick = false;

    public boolean checkForLoopsNextTick = false;

    public boolean updatePowerNextTick = false;

    public boolean updateNextTick = false;

    public boolean getsOutsidePower = false;

    public float networkResistance = 0;

    public int voltage = 0;

    public int voltageSupply = 0;

    public int networkPowerGeneration = 0;

    public float highestCurrent = 0;

    public boolean notEnoughPower = false;

    public int tickUntilConnectFE = -1;

    public boolean waitingForNextCharge = false;

    public boolean setVoltageNextTick = false;

    public int failTimer = 0;

    public int energyGiven = 0;

    public int energyTakenPerTick = 0;

    public int energyTaken = 0;

    public boolean importsFE = false;

    public ElectricBlockValues(long pos) {
        this.electricalNetworkId = pos;
    }

    public long getId() {
        return electricalNetworkId;
    }

    public boolean destroyed() {
        return destroyed;
    }

    public int getVoltage() {
        return voltage;
    }

    public Map<Direction, IEnergyStorage> energyOutputs = new HashMap<>();

    public List<Consumer<Integer>> scheduledActions = new ArrayList<>();
}
