package com.drmangotea.tfmg.content.electricity.utilities.electric_motor;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.MachineConfig;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
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



    public boolean delayedUpdate = false;



    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public void onPlaced() {
        super.onPlaced();
        for(IElectric member : getOrCreateElectricNetwork().members){
            if(member instanceof ElectricMotorBlockEntity be)
                be.delayedUpdate = true;

        }
    }



    @Override
    public void tick() {
        super.tick();



        if(delayedUpdate){
            updateGeneratedRotation();
            delayedUpdate = false;
        }

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
                CreateLang.translateDirect("contraptions.windmill.rotation_direction"), this, new MotorValueBox());

        movementDirection.withCallback($ -> onDirectionChanged());
        behaviours.add(movementDirection);
    }

    private void onDirectionChanged() {
        updateNextTick();
    }



    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite() || (direction.getAxis().isHorizontal() && direction == Direction.DOWN);
    }




    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        //if (oldPower != getPowerUsage() || oldVoltage != data.voltage) {
        delayedUpdate = true;
        notifyUpdate();
        // }
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getPowerUsage()>0)
            updateNextTick();
    }

    @Override
    public float getGeneratedSpeed() {
        if(networkUndersupplied())
            return 0;
        if (!canWork())
            return 0;

        int rotation = movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.CLOCKWISE ? 1 : -1;

        float speed = Math.min(255,data.getVoltage()*.8f)*rotation;

        return speed;



    }

    @Override
    public float calculateAddedStressCapacity() {
        float speedModifier = getSpeed()/256;


        return (int)(super.calculateAddedStressCapacity()*speedModifier);
    }

    //@Override
    //public boolean canBeInGroups() {
    //    return true;
    //}
    @Override
    public float resistance() {

        return TFMGConfigs.common().machines.electricMotorInternalResistance.getF();
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
