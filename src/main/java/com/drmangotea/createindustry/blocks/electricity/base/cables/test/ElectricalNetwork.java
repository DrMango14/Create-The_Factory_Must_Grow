package com.drmangotea.createindustry.blocks.electricity.base.cables.test;

import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;

import java.util.ArrayList;

public class ElectricalNetwork {

    public ArrayList<IElectricBlock> machines = new ArrayList<>();


    public ElectricalNetwork(IElectricBlock startBE){
        machines.add(startBE);
    }



}
