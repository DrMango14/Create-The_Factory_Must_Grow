package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.drmangotea.tfmg.blocks.electricity.base.TFMGForgeEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface IElectric {

    long getNetwork();

    LevelAccessor getLevel();



    long getId();

    void setNetwork(long newNetwork, boolean removeNetwork);

    ElectricalNetwork getOrCreateElectricNetwork();

    void setNetworkClient(long value);

    boolean hasElectricitySlot(Direction direction);


    void onConnected();

    int FECapacity();

    int FEProduction();

    int FETransferSpeed();

    int getVoltage();

    int maxVoltage();



    int voltageGeneration();

    void setVoltage(int value, boolean update);

    void voltageFailure();

    void connectNeighbors();

    void needsVoltageUpdate();

    boolean destroyed();

    void needsNetworkUpdate();

    default boolean outputAllowed(){
        return true;
    }

    boolean addConnection(WireManager.Conductor material, BlockPos pos, boolean shouldRender, boolean neighborConnection);

    void makeControllerAndSpread();

    void setVoltageFromNetwork();

    TFMGForgeEnergyStorage getForgeEnergy();
    default TFMGForgeEnergyStorage createEnergyStorage(){
        return new TFMGForgeEnergyStorage(FECapacity(), FETransferSpeed()) {
            @Override
            public void onEnergyChanged(int energyAmount,boolean setEnergy) {

                if(getEnergyStored() == 0){
                    getOrCreateElectricNetwork().updateNetworkVoltage();
                    getOrCreateElectricNetwork().updateVoltageFromNetwork();
                }

            }
        };

    }





    void sendStuff();
}
