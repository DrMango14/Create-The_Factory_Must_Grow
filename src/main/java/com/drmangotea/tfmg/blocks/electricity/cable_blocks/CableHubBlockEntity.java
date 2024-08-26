package com.drmangotea.tfmg.blocks.electricity.cable_blocks;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ElectricNetworkManager;
import com.drmangotea.tfmg.blocks.electricity.base.cables.IElectric;
import com.drmangotea.tfmg.blocks.electricity.base.cables.WireConnection;
import com.drmangotea.tfmg.blocks.electricity.base.cables.WireManager;
import com.drmangotea.tfmg.blocks.electricity.resistors.ResistorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableHubBlockEntity extends ElectricBlockEntity {

    boolean signalChanged;

    public boolean hasSignal;
    public CableHubBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }
    public void onPlaced(){



        networkUpdate = true;
        if(!hasSignal)
            connectNeighbors();

        // needsVoltageUpdate = true;

        setVoltage(voltageGeneration());

    }
    @Override
    public void tick() {
        super.tick();
        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }




    }



    public void neighbourChanged() {
        if (!hasLevel())
            return;
        boolean powered = level.getBestNeighborSignal(worldPosition)>0;
        if (powered != hasSignal)
            signalChanged = true;
    }
    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();


    }
    protected void analogSignalChanged(int newSignal) {

        hasSignal = newSignal > 0;

        if(newSignal == 0) {

            for(Direction direction : Direction.values()){
                if(hasElectricitySlot(direction)) {
                    BlockPos pos = getBlockPos().relative(direction);
                    if (level.getBlockEntity(pos) instanceof IElectric be){
                        if(be instanceof CableHubBlockEntity be1 && (be1.hasSignal||be1 == this))
                            continue;

                        if (be.hasElectricitySlot(direction.getOpposite()) ) {
                            be.addConnection(WireManager.Conductor.COPPER, getBlockPos(), false, true);
                            be.sendStuff();
                            addConnection(WireManager.Conductor.COPPER, pos, false, true);
                            sendData();
                            setChanged();


                            be.makeControllerAndSpread();

                        }
                    }
                }
            }


        }else {
            getOrCreateElectricNetwork().remove(this);
            ElectricNetworkManager.networks.get(level)
                    .remove(getId());
            needsNetworkUpdate();
            needsVoltageUpdate();


            onVoltageChanged();

            for(WireConnection connection : wireConnections){
                BlockPos pos = connection.point1 == getBlockPos() ? connection.point2 : connection.point1;

                if(level.getBlockEntity(pos) instanceof IElectric be){




                    be.makeControllerAndSpread();


                    be.getOrCreateElectricNetwork().updateNetworkVoltage();
                    be.getOrCreateElectricNetwork().updateVoltageFromNetwork();


                    if(be instanceof ResistorBlockEntity){
                        be.getOrCreateElectricNetwork().voltage = 0;
                    }


                }


            }
        }
    }


    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.CABLE_HUB.get());
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return !hasSignal;
    }
}
