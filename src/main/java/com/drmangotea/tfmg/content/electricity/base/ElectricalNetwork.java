package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.content.electricity.utilities.electric_motor.ElectricMotorBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Phase I  — reset state, find max voltage, sum resistance (1/R for parallel), sum power generation.
     * Phase II — distribute voltage/resistance to all members, notify of change.
     * Phase III — compute cable current (sum of all member currents), notify transformers/diodes.
     * Phase IV — mark network undersupplied if load > generation.
     */
    public void updateNetwork() {

        // Phase I
        int maxVoltage = 0;
        float resistance = 0;
        int powerGeneration = 0;

        for (IElectric member : members) {
            member.getData().notEnoughPower = false;
            member.getData().highestCurrent = 0;

            maxVoltage = Math.max(member.voltageGeneration(), maxVoltage);
            if (member.resistance() != 0)
                resistance += 1f / member.resistance();
            powerGeneration += member.powerGeneration();
        }

        // Phase II
        List<IElectric> list = new ArrayList<>(members);
        if (!members.isEmpty()) {
            for (IElectric member : list) {
                int oldVoltage = member.getData().getVoltage();
                int oldPower = member.getPowerUsage();
                member.getData().voltageSupply = maxVoltage;
                member.setVoltage(maxVoltage);
                member.getData().setVoltageNextTick = true;

                member.getData().networkPowerGeneration = powerGeneration;
                if (resistance != 0)
                    member.setNetworkResistance(1f / resistance);
                else
                    member.setNetworkResistance(0f);
                member.onNetworkChanged(oldVoltage, oldPower);
            }
        }

        // Phase III
        for (IElectric member : members) {
            if (member.resistance() == 0) {
                member.getData().highestCurrent = getCableCurrent(member);
            }
            if (member instanceof VoltageAlteringBlockEntity be) {
                be.updateInFront();
            }
        }

        // Phase IV
        handleInsufficientPower();
    }

    public void handleInsufficientPower() {
        if (!members.isEmpty())
            if (members.get(0).getNetworkPowerUsage() > members.get(0).getNetworkPowerGeneration()) {
                for (IElectric member : members) {
                    member.getData().notEnoughPower = true;
                    if (member instanceof ElectricMotorBlockEntity be) {
                        be.updateGeneratedRotation();
                    }
                    if (member instanceof VoltageAlteringBlockEntity be)
                        be.updateInFront = true;
                }
            }
    }

    public static float getCableCurrent(IElectric be) {
        float current = 0;
        for (IElectric member : be.getOrCreateElectricNetwork().members) {
            current += member.getCurrent();
        }
        return current;
    }

    public void checkForLoops(BlockPos pos) {
        members.forEach(member -> {
            if (member instanceof VoltageAlteringBlockEntity be) {
                if (be.getControlledBlock() != null) {
                    List<ElectricalNetwork> list = new ArrayList<>();
                    list.add(this);
                    be.getControlledBlock().getOrCreateElectricNetwork().checkForLoops(list, pos);
                }
            }
        });
    }

    public void checkForLoops(List<ElectricalNetwork> network, BlockPos pos) {
        if (network.contains(this)) {
            if (!members.isEmpty())
                members.get(0).getLevelAccessor().destroyBlock(pos, false);
            return;
        }
        network.add(this);
        members.forEach(member -> {
            if (member instanceof VoltageAlteringBlockEntity be) {
                if (be.getControlledBlock() != null) {
                    be.getControlledBlock().getOrCreateElectricNetwork().checkForLoops(network, pos);
                }
            }
        });
    }

    public List<IElectric> getMembers() {
        return members;
    }
}
