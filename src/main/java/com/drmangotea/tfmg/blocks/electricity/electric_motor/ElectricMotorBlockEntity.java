package com.drmangotea.tfmg.blocks.electricity.electric_motor;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class ElectricMotorBlockEntity extends KineticElectricBlockEntity implements IHaveGoggleInformation {

    public static final int DEFAULT_SPEED = 16;
    public static final int MAX_SPEED = 256;


    protected ScrollValueBehaviour generatedSpeed;



    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }




    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = MAX_SPEED;
        generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.creative_motor.rotation_speed"),
                this, new MotorValueBox());
        generatedSpeed.between(-max, max);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
        generatedSpeed.withCallback(i ->this.needsNetworkUpdate());
        behaviours.add(generatedSpeed);
    }



    @Override
    public void lazyTick() {
        super.lazyTick();


        if(energy.getEnergyStored()>0&&getVoltage()>0) {
            updateGeneratedRotation();
        }else sendData();
    }


    @Override
    public void tick() {
        super.tick();
        getOrCreateElectricNetwork().requestEnergy(this);


    }

    @Override
    public void setVoltage(int value, boolean update) {
        super.setVoltage(value, update);
        updateGeneratedRotation();
    }



    @Override
    public float getGeneratedSpeed() {

        if(voltage == 0)
            return 0;


        if (!TFMGBlocks.ELECTRIC_MOTOR.has(getBlockState()))
            return 0;



        energy.extractEnergy((int) Math.abs(speed/4),false);




        if(energy.getEnergyStored() == 0)
            return 0;




        return convertToDirection(Math.min(generatedSpeed.getValue(),voltage*2), getBlockState().getValue(FACING));
    }

    @Override
    public boolean outputAllowed() {
        return false;
    }





    class MotorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            Direction facing = state.getValue(FACING);
            return super.getLocalOffset(state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            super.rotate(state, ms);
            Direction facing = state.getValue(FACING);
            if (facing.getAxis() == Direction.Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.cast(ms)
                    .rotateZ(-AngleHelper.horizontalAngle(facing) + 180);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(FACING);
            if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN||direction == Direction.UP)
                return false;
            return direction.getAxis() != facing.getAxis();
        }

    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }


    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.ELECTRIC_MOTOR.get());
    }




    @Override
    public int FECapacity() {
        return 10000;
    }



}
