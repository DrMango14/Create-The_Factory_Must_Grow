package com.drmangotea.tfmg.content.machines.oil_processing.pumpjack.crank;


import com.drmangotea.tfmg.content.machines.oil_processing.pumpjack.hammer_holder.PumpjackHammerHolderBlockEntity;
import com.drmangotea.tfmg.content.machines.oil_processing.pumpjack.machine_input.MachineInputBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackCrankBlockEntity extends KineticBlockEntity {
    float	targetSpeed;


    public float angle=0;
    public Direction direction;
    public BlockPos hammerPos;


    public PumpjackCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        angle=177;
        if(direction==Direction.NORTH)
            hammerPos =this.getBlockPos().north(2).above();
        if(direction==Direction.SOUTH)
            hammerPos =this.getBlockPos().south(2).above();
        if(direction==Direction.WEST)
            hammerPos =this.getBlockPos().west(2).above();
        if(direction==Direction.EAST)
            hammerPos =this.getBlockPos().east(2).above();
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (clientPacket) {
            compound.putFloat("Angle", angle);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket) {

            angle = compound.getFloat("Angle");
        }
    }

    @Override
    public void tick() {
        super.tick();


        direction = this.getBlockState().getValue(FACING);

        if (!level.isClientSide)
            return;


        if(direction==Direction.NORTH)
            hammerPos =this.getBlockPos().north(2).above();
        if(direction==Direction.SOUTH)
            hammerPos =this.getBlockPos().south(2).above();
        if(direction==Direction.WEST)
            hammerPos =this.getBlockPos().west(2).above();
        if(direction==Direction.EAST)
            hammerPos =this.getBlockPos().east(2).above();


        if(!isValid()) {
            angle = 177;
            return;
        }

        if(level.getBlockEntity(this.getBlockPos().below())instanceof MachineInputBlockEntity) {
            if(((MachineInputBlockEntity)level.getBlockEntity(this.getBlockPos().below())).powerLevel!=0) {
                angle += 3;
            }else    angle=177;
        }else
            angle=177;



            targetSpeed= 10;



        angle%=360;

    }

    public boolean isValid(){
        if(hammerPos==null)
            return false;

        if(!(level.getBlockEntity(hammerPos) instanceof PumpjackHammerHolderBlockEntity))
            return false;
        if(!(direction==level.getBlockEntity(hammerPos).getBlockState().getValue(FACING)))
            return false;

        return true;
    }
/*
    private void moveConnectionPos() {
        connectionPos = new BlockPos(this.getBlockPos().getX()+0.5f,this.getBlockPos().getY()+0.25f,this.getBlockPos().getZ()+0.5f);

        float y=0.8f;
        float x=0.8f;
     //   connectionPos.



    }

 */
}
