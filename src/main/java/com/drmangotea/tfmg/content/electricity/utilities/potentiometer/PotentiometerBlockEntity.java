package com.drmangotea.tfmg.content.electricity.utilities.potentiometer;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.utilities.diode.ElectricDiodeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PotentiometerBlockEntity extends ElectricDiodeBlockEntity {


    protected ScrollValueBehaviour outputPercentage;
    public PotentiometerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 100;
        outputPercentage = new ScrollValueBehaviour(CreateLang.translateDirect("resistor.allowed_voltage"),
                this, new PotentiometerValueBox());
        outputPercentage.between(0, max);
        outputPercentage.value = 100;
        outputPercentage.withCallback(i ->this.updateNextTick());
        outputPercentage.withCallback(i ->this.updateInFrontNextTick());
        behaviours.add(outputPercentage);

    }

    public Direction getDirection(){
        if(!getBlockState().hasProperty(DirectionalBlock.FACING)){
            return getBlockState().getValue(TFMGHorizontalDirectionalBlock.FACING).getCounterClockWise();
        }

        return getBlockState().getValue(DirectionalBlock.FACING);
    }


    @Override
    public int getOutputVoltage() {
        return (int) (((float)getData().getVoltage()/100)* outputPercentage.getValue());
    }


    static class PotentiometerValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(DirectionalKineticBlock.FACING);
            return super.getLocalOffset(level,pos,state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level,pos,state, ms);
            Direction facing = state.getValue(DirectionalKineticBlock.FACING);
            if (facing.getAxis() == Direction.Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.of(ms)
                    .rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(DirectionalKineticBlock.FACING);
            if (facing.getAxis().isVertical()) {
                return direction.getAxis().isHorizontal();
            } else return direction == Direction.UP;
        }

    }



}
