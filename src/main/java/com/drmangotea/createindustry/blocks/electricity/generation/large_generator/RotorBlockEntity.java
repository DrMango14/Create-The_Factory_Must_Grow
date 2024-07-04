package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;
import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static net.minecraft.core.Direction.Axis.Z;

public class RotorBlockEntity extends KineticBlockEntity {
    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;

    List<BlockPos> statorBlocks = new ArrayList<>();

    public boolean valid = false;

    public RotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
            visualSpeed.chase(getGeneratedSpeed(), 1 / 128f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public void tick() {
        super.tick();



            float targetSpeed = getSpeed();
            visualSpeed.updateChaseTarget(targetSpeed);
            visualSpeed.tickChaser();
            angle += visualSpeed.getValue() * 3 / 10f;
            angle %= 360;



        if(valid)
           generateVoltage();


        for(BlockPos pos : statorBlocks){
            if(!level.getBlockState(pos).is(TFMGBlocks.STATOR.get()))
                statorBlocks = new ArrayList<>();
        }


        if(statorBlocks.toArray().length ==8){
            valid = true;
            valid = true;
        } else {
            valid = false;
            manageStator();
        }

    }


    public void generateVoltage(){
        float power = 2.5f*getSpeed()*(getSpeed()/25f);

        for(BlockPos pos : statorBlocks){

            if(level.getBlockEntity(pos)instanceof StatorBlockEntity be){
                if(be.hasOutput){
                    be.generation = power;
                }
            }

        }

    }


    public void manageStator(){


        Direction.Axis axis = getBlockState().getValue(AXIS);

        statorBlocks = new ArrayList<>();



        for(Direction direction : Direction.values()){
            if(direction.getAxis() == axis)
                continue;

            BlockPos pos1 = getBlockPos().relative(direction);

            if (canUseStator(pos1)) {
                if (axis == Z && direction.getAxis().isVertical()) {
                    level.setBlock(pos1, TFMGBlocks.STATOR.getDefaultState().setValue(FACING, direction.getOpposite()).setValue(StatorBlock.STATOR_STATE, StatorBlock.StatorState.SIDE).setValue(StatorBlock.VALUE, true), 2);
                    statorBlocks.add(pos1);


                } else {
                    level.setBlock(pos1, TFMGBlocks.STATOR.getDefaultState().setValue(FACING, direction.getOpposite()).setValue(StatorBlock.STATOR_STATE, StatorBlock.StatorState.SIDE).setValue(StatorBlock.VALUE, false), 2);
                    statorBlocks.add(pos1);
                }
                ((StatorBlockEntity)level.getBlockEntity(pos1)).setRotor(this);
            }


            if(direction.getAxis().isVertical())
                for(Direction direction2 : Direction.values()) {
                    if (direction2.getAxis() == axis||direction2.getAxis() == direction.getAxis())
                        continue;

                    BlockPos pos = pos1.relative(direction2);


                        if(canUseStator(pos)) {

                            BlockState state = TFMGBlocks.STATOR.getDefaultState().setValue(FACING, direction.getOpposite()).setValue(StatorBlock.STATOR_STATE, StatorBlock.StatorState.CORNER).setValue(StatorBlock.VALUE, false);


                            if (direction == Direction.UP)
                                state = state.setValue(StatorBlock.VALUE,true);

                            if(direction2 == Direction.EAST)
                                state = state.setValue(FACING,Direction.SOUTH);

                            if(direction2 == Direction.NORTH)
                                state = state.setValue(FACING,Direction.EAST);

                            if(direction2 == Direction.SOUTH)
                                state = state.setValue(FACING,Direction.WEST);


                            level.setBlock(pos, state, 2);
                            statorBlocks.add(pos);
                            ((StatorBlockEntity)level.getBlockEntity(pos)).setRotor(this);
                        }
                }

        }




    }



    public boolean canUseStator(BlockPos pos){
        return level.getBlockState(pos).is(TFMGBlocks.STATOR.get())&&(
                ((StatorBlockEntity)level.getBlockEntity(pos)).rotor==null||
                ((StatorBlockEntity)level.getBlockEntity(pos)).rotor==this.getBlockPos());
    }







}