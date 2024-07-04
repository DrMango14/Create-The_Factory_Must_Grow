package com.drmangotea.createindustry.blocks.electricity.resistors;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class ResistorBlockEntity extends ElectricBlockEntity {

    protected ScrollValueBehaviour outputVoltage;
    public ResistorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 250;
        outputVoltage = new ScrollValueBehaviour(Lang.translateDirect("resistor.allowed_voltage"),
                this, new ResistorValueBox());
        outputVoltage.between(0, max);
        outputVoltage.value = 50;
        //outputVoltage.withCallback(i -> this.updateVoltageOutput());
        behaviours.add(outputVoltage);
    }


    public void updateVoltageOutput(){



    }


    public int getVoltageOutput(){
        return Math.min(outputVoltage.getValue(),voltage);
    }

    @Override
    public void tick() {
        super.tick();


                BlockEntity be1 = level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING)));
                if (be1 instanceof IElectricBlock be2)
                    if (be2.hasElectricitySlot(getBlockState().getValue(FACING).getOpposite())) {
                        sendCharge(be2);


                    }

    }


    public void sendCharge(IElectricBlock be){



        int maxPossibleTransfer = Math.min(transferSpeed(),Math.min(energy.getEnergyStored(),be.getForgeEnergy().getMaxEnergyStored()-be.getForgeEnergy().getEnergyStored()));


        if(be.getForgeEnergy().getEnergyStored() < getForgeEnergy().getEnergyStored()) {
            be.getForgeEnergy().receiveEnergy(maxPossibleTransfer, false);
            getForgeEnergy().extractEnergy(maxPossibleTransfer, false);
        }
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    class ResistorValueBox extends ValueBoxTransform.Sided {

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
            if (direction!=Direction.UP)
                return false;
            return direction.getAxis() != facing.getAxis();
        }

    }
    @Override
    public float maxVoltage() {
        return 1500;
    }
    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }
}
