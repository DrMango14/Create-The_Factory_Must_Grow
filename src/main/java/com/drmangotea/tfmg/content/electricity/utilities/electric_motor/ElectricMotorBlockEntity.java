package com.drmangotea.tfmg.content.electricity.utilities.electric_motor;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.MachineConfig;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class ElectricMotorBlockEntity extends KineticElectricBlockEntity {

    public static final int DEFAULT_SPEED = 64;
    public static final int MAX_SPEED = 256;
    protected ScrollValueBehaviour generatedSpeed;

    public boolean delayedUpdate = false;

    public float testSpeed = 0;

    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        updateGeneratedRotation();
    }

    @Override
    public void tick() {
        super.tick();
        if(delayedUpdate){
            updateNextTick();
            delayedUpdate = false;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = MAX_SPEED;
        generatedSpeed = new KineticScrollValueBehaviour(CreateLang.translateDirect("kinetics.creative_motor.rotation_speed"),
                this, new MotorValueBox());
        generatedSpeed.between(-max, max);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
        behaviours.add(generatedSpeed);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite() || (direction.getAxis().isHorizontal() && direction == Direction.DOWN);
    }

    @Override
    public void notifyNetworkAboutSpeedChange() {
        super.notifyNetworkAboutSpeedChange();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.putFloat("MotorSpeed",getSpeed());
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        testSpeed = compound.getFloat("MotorSpeed");
    }

    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        if (oldPower != getPowerUsage() || oldVoltage != data.voltage) {
            updateGeneratedRotation();
        }
    }

    @Override
    public float getGeneratedSpeed() {

        MachineConfig machineConfig = TFMGConfigs.common().machines;

        //if(getPowerUsage() <machineConfig.electricMotorMinimumPower.get())
        //    return 0;
        if (!canWork())
            return 0;
        //if(getPowerUsage() >= machineConfig.electricMotorMinimumPower.get()){
        float speed = generatedSpeed.getValue() <0 ? -Math.min(Math.abs(data.getVoltage()/2),Math.abs(generatedSpeed.getValue())) : Math.min(Math.abs(data.getVoltage()/2),Math.abs(generatedSpeed.getValue()));

        if(speed==0){
            return testSpeed;
        }

        testSpeed = 0;
        return speed;


        //}

        //return 0;
    }


    @Override
    public boolean canBeInGroups() {
        return true;
    }



    @Override
    public float resistance() {

        return TFMGConfigs.common().machines.electricMotorInternalResistance.getF();
    }

    @Override
    public int getPowerUsage() {

        if (Math.min(generatedSpeed.getValue(), data.getVoltage() / 2) == 0)
            return super.getPowerUsage();

        float speedModifier = (Math.min(Math.abs(generatedSpeed.getValue()), data.getVoltage()) / 256f) * 5;


        return (int) ((float) super.getPowerUsage() * speedModifier);
    }

    class MotorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(FACING);
            return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
            Direction facing = state.getValue(FACING);
            if (facing.getAxis() == Direction.Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.of(ms)
                    .rotateZ(-AngleHelper.horizontalAngle(facing) + 180);
        }



        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(FACING);
            if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN || direction == Direction.UP)
                return false;
            return direction.getAxis() != facing.getAxis();
        }

    }
}
