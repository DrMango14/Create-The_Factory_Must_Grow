package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.drmangotea.tfmg.CreateTFMG;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ElectricalNetwork {



    public List<IElectric> members = new ArrayList<>();

    public long id;

    public int voltage=0;




    public ElectricalNetwork(long id){
        this.id = id;
    }



    public void updateNetworkVoltage(){

        int maxVoltage = 0;

        for(IElectric member : members){

            if(!member.destroyed())
                maxVoltage = Math.max(member.voltageGeneration(), maxVoltage);





        }

        voltage = maxVoltage;

        updateVoltageFromNetwork();

    }

    public void updateVoltageFromNetwork(){
        for(IElectric member : members) {

                    member.needsVoltageUpdate();
        }

    }



    public void add(IElectric be) {
        List<Long> posList = new ArrayList<>();

        members.forEach(member->posList.add(member.getId()));

        if(posList.contains(be.getId()))
            return;

        members.add(be);

        //updateFromNetwork(be);
        //be.setNetworkDirty(true);
    }


    public void requestEnergy(SmartBlockEntity blockEntity){
        if(blockEntity instanceof IElectric be) {
            for (IElectric member : members){
                if(member.getForgeEnergy()!=null&&!member.equals(be)&&voltage != 0)
                    if(member.outputAllowed()&&member.getForgeEnergy().getEnergyStored()>0){

                        sendEnergy(member,be);

                        //if(be.getForgeEnergy().getEnergyStored()==be.getForgeEnergy().getMaxEnergyStored())
                        //    return;


                    }

            }



        }

    }


    public void transferEnergy(SmartBlockEntity blockEntity){
        if(blockEntity instanceof IElectric be){

            if(!be.outputAllowed())
                return;

            CreateTFMG.LOGGER.debug("Started  Transfer");

            for (IElectric member : members){
                if(member.getForgeEnergy()!=null&&!member.equals(be))
                    if(member.voltageGeneration()==0){

                    sendEnergy(be,member);


                    if(be.getForgeEnergy().getEnergyStored()==0)
                        return;

                }
            }


        }



    }

    public static int sendEnergy(IElectric sender, IElectric reciever){
        int maxTransfer1 = sender.getForgeEnergy().extractEnergy(sender.getForgeEnergy().getEnergyStored(),true);
        int maxTransfer2 = reciever.getForgeEnergy().receiveEnergy(sender.getForgeEnergy().getEnergyStored(),true);

                sender.getForgeEnergy().extractEnergy(Math.min(maxTransfer1,maxTransfer2),false);
        return reciever.getForgeEnergy().receiveEnergy(Math.min(maxTransfer1,maxTransfer2),false);
    }




    public void remove(IElectric be) {
        if (!members.contains(be))
            return;

        members.remove(be);


        if (members.isEmpty()) {
            ElectricNetworkManager.networks.get(be.getLevel())
                    .remove(this.id);
            return;
        }

        //members.keySet()
        //        .stream()
        //        .findFirst()
        //        .map(member -> member.setNetworkDirty(true));
    }



}
