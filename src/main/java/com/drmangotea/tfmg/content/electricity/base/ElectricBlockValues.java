package com.drmangotea.tfmg.content.electricity.base;

public class ElectricBlockValues {


    public long electricalNetworkId;
    public boolean destroyed = false;
    public boolean connectNextTick = false;
    public boolean checkForLoopsNextTick = false;
    public boolean updatePowerNextTick = false;
    public boolean updateNextTick = false;
    public boolean getsOutsidePower = false;
    public int networkResistance = 0;
    public int voltage = 0;
    public int frequency = 0;
    public int voltageSupply = 0;
    public int networkPowerGeneration =0;
    public float highestCurrent=0;

    public boolean notEnoughtPower=false;

    public boolean setVoltageNextTick = false;

    public int failTimer = 0;

    public ElectricalGroup group = new ElectricalGroup(0);

    public ElectricBlockValues(long pos){
        this.electricalNetworkId = pos;
    }

    public long getId(){
        return electricalNetworkId;
    }
    public boolean destroyed(){
        return destroyed;
    }
    public int getVoltage(){
        return voltage;
    }
}
