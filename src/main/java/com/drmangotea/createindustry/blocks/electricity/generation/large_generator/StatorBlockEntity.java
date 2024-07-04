package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.createindustry.blocks.electricity.generation.large_generator.StatorBlock.STATOR_STATE;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;


public class StatorBlockEntity extends ElectricBlockEntity {


    public BlockPos rotor=null;

    public boolean hasOutput;

    public float generation;


    public StatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(){
        super.tick();

        if(rotor!=null)
            if(!(level.getBlockEntity(rotor) instanceof RotorBlockEntity))
                rotor = null;

        if(rotor == null||getBlockState().getValue(STATOR_STATE) != StatorBlock.StatorState.SIDE)
            return;


    }


    @Override
    public float maxVoltage() {
        return 10000;
    }


    public void setRotor(RotorBlockEntity be){
        rotor = be.getBlockPos();

    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putBoolean("Output",hasOutput);

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        hasOutput = compound.getBoolean("Output");


    }
    @Override
    public int feGeneration() {
        return (int) generation*30;
    }

    @Override
    public int voltageGeneration() {
        return (int) (generation);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite()&&hasOutput;
    }


    @Override
    public int FECapacity() {
        return 25000;
    }







}
