package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.utilities.diode.ElectricDiodeBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.electric_motor.ElectricMotorBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.transformer.TransformerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class ElectricalNetwork {

    public ElectricalNetwork(long id) {
        this.id = id;
    }

    public List<IElectric> members = new ArrayList<>();


    public long id;

    public long getId() {
        return id;
    }

    public void add(IElectric be) {
        List<Long> posList = new ArrayList<>();

        members.forEach(member -> posList.add(member.getData().getId()));
        if (posList.contains(be.getData().getId()))
            return;
        members.add(be);
    }

    public void updateNetwork() {



        int maxVoltage = 0;
        int power = 0;
        int resistance = 0;
        int powerGeneration = 0;


        Map<Integer, Float> groups = new HashMap<>();

        for (IElectric member : members) {
            member.getData().notEnoughtPower = false;
            int groupId = member.getData().group.id;

            maxVoltage = Math.max(member.voltageGeneration(), maxVoltage);
            power += member.powerGeneration();
            resistance += (int) member.resistance();
            powerGeneration += member.powerGeneration();
            if (member.canBeInGroups())
                groups.put(groupId, groups.containsKey(groupId) ? groups.get(groupId) + member.resistance() : member.resistance());
        }

        int powerPercentage = resistance > 0 ? (int) (Math.min(((float) power / (float) resistance * 100f), 100)) : 100;

        List<IElectric> list = new ArrayList<>(members);
        if(!members.isEmpty()) {
            float powerUsage = members.get(0).getNetworkPowerUsage();

            for (IElectric member : list) {

                int oldVoltage = member.getData().getVoltage();
                int oldPower = member.getPowerUsage();
                member.getData().voltageSupply = maxVoltage;
                member.setVoltage(maxVoltage);
                member.getData().setVoltageNextTick = true;

                member.getData().networkPowerGeneration = powerGeneration;
                member.setNetworkResistance(resistance);
                member.onNetworkChanged(oldVoltage, oldPower);


                if (groups.containsKey(member.getData().group.id))
                    member.getData().group.resistance = groups.get(member.getData().group.id);
            }
        }

        for (IElectric member : members) {
            member.getData().highestCurrent = getCableCurrent(member);

            member.updateNearbyNetworks(member);
            if(member instanceof ElectricDiodeBlockEntity be) {
                be.updateInFront();
            }
            if(member instanceof TransformerBlockEntity be) {
                be.updateInFront();
            }

          //  if (member instanceof KineticElectricBlockEntity be) {
          //      be.updateGeneratedRotation();
          //  }
        }

        handleInsufficientPower();

    }

    public void handleInsufficientPower(){
        if (!members.isEmpty())
            if (members.get(0).getNetworkPowerUsage() > members.get(0).getNetworkPowerGeneration()) {
              //  members.get(0).updateUnpowered(new ArrayList<>());
                for (IElectric member : members) {
                    member.getData().notEnoughtPower = true;
                    if (member instanceof ElectricMotorBlockEntity be) {
                        be.updateGeneratedRotation();
                    }
                    if (member instanceof ElectricDiodeBlockEntity be)
                        be.updateInFront=true;
                    if (member instanceof TransformerBlockEntity be)
                        be.updateInFront();
                }
            }
    }


    public static float getCableCurrent(IElectric be) {

        float current = 0;
        List<Integer> groups = new ArrayList<>();

        for (IElectric member : be.getOrCreateElectricNetwork().members) {

            if (member.canBeInGroups())
                if (!groups.contains(member.getData().group.id)) {
                    groups.add(member.getData().group.id);
                    if (member.resistance() != 0)

                        current += member.getData().voltage / member.resistance();
                }
        }


        return current;
    }

    public void checkForLoops(BlockPos pos ){

        members.forEach(member->{
            if(member instanceof VoltageAlteringBlockEntity be){
                if(be.getControlledBlock() !=null){
                    List<ElectricalNetwork> list = new ArrayList<>();
                    list.add(this);
                    be.getControlledBlock().getOrCreateElectricNetwork().checkForLoops(list, pos);
                }
            }});


    }

    public void checkForLoops(List<ElectricalNetwork> network, BlockPos pos){

        if(network.contains(this)) {
            if(!members.isEmpty())
                members.get(0).getLevelAccessor().destroyBlock(pos,false);
            return;
        }
        network.add(this);
        members.forEach(member->{
            if(member instanceof VoltageAlteringBlockEntity be){
                if(be.getControlledBlock() !=null){
                    be.getControlledBlock().getOrCreateElectricNetwork().checkForLoops(network, pos);
                }
            }});


    }

    public List<IElectric> getMembers() {
        return members;
    }
}
