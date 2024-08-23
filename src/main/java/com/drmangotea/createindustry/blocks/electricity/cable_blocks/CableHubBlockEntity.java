package com.drmangotea.createindustry.blocks.electricity.cable_blocks;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayList;

public class CableHubBlockEntity extends ElectricBlockEntity {

    int signal;
    boolean signalChanged;

    byte[] directions = new byte[6];





    public CableHubBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        neighbourChanged();
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {

        return true;
    }

    @Override
    public void tick() {



        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }

        super.tick();
    }

    public void neighbourChanged() {
        if (!hasLevel())
            return;

        ArrayList<Byte> list = new ArrayList<>();

       // for(Direction direction1 : Direction.values()) {
       //     if(level.getBlockEntity(getBlockPos().relative(direction1))instanceof IElectricBlock be){
       //             if(be.hasElectricitySlot(direction1.getOpposite())){
       //                 list.add((byte) 1);
       //             }else    list.add((byte) 0);
//
       //         }else {
       //         if(level.getBlockEntity(getBlockPos().relative(direction1))==null){
       //             list.add((byte) 0);
       //             continue;
       //         }
//
//
       //         if (level.getBlockEntity(getBlockPos().relative(direction1)).getCapability(ForgeCapabilities.ENERGY).isPresent()) {
       //             list.add((byte) 1);
       //         } else list.add((byte) 0);
//
       //     }
       //     }
//
       // directions = new byte[]{list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5)};


        int power = level.getBestNeighborSignal(worldPosition);
        if (power != signal)
            signalChanged = true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();
    }


    protected void analogSignalChanged(int newSignal) {
        //removeSource();
        signal = newSignal;
    }


    @Override
    public boolean hasSignal() {
        return signal>0;
    }

    @Override
    public float maxVoltage() {
        return 6000;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Signal", signal);


        compound.putByteArray("Directions",directions);


        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {


        signal = compound.getInt("Signal");

        directions = compound.getByteArray("Directions");



        super.read(compound, clientPacket);
    }




    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
