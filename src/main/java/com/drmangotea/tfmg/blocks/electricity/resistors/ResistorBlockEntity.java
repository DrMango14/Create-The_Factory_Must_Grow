package com.drmangotea.tfmg.blocks.electricity.resistors;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ElectricalNetwork;
import com.drmangotea.tfmg.blocks.electricity.base.cables.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class ResistorBlockEntity extends VoltageAlteringBlockEntity {

    protected ScrollValueBehaviour outputVoltage;
    public ResistorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getOutputVoltage() {
        return outputVoltage.value;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 250;
        outputVoltage = new ScrollValueBehaviour(Lang.translateDirect("resistor.allowed_voltage"),
                this, new ResistorValueBox());
        outputVoltage.between(0, max);
        outputVoltage.value = 50;
        outputVoltage.withCallback(i -> setVoltage(voltageGeneration(),true));
        behaviours.add(outputVoltage);
    }


    //public void updateVoltage(){
    //    needsNetworkUpdate();
    //    needsVoltageUpdate();
    //    if(level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING))) instanceof IElectric be){
    //        be.needsNetworkUpdate();
    //        be.needsVoltageUpdate();
//
    //    }
    //}

    @Override
    public boolean outputAllowed() {
        return false;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();



        getOrCreateElectricNetwork().requestEnergy(this);

        Direction facing = getBlockState().getValue(FACING);
        if(level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be){
            if(be.hasElectricitySlot(facing.getOpposite())){



                ElectricalNetwork.sendEnergy(this,be);


            }
        }

    }


    @Override
    public void setVoltage(int value, boolean update) {
        super.setVoltage(value, update);


        Direction facing = getBlockState().getValue(FACING);
        if(level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be){
            if(be.hasElectricitySlot(facing.getOpposite())){

                be.getOrCreateElectricNetwork().updateNetworkVoltage();


            }
        }
    }

    public int voltageOutput() {
       // return  0;
        return Math.min(outputVoltage.getValue(),getVoltage());
    }

    //@Override
    //public void onPlaced() {
    //    super.onPlaced();
//
    //    Direction facing = getBlockState().getValue(FACING);
    //    if(level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be){
    //        if(be.hasElectricitySlot(facing.getOpposite())){
    //            be.needsVoltageUpdate();
//
    //        }
    //    }
//
//
    //}

    //@Override
    //public void destroy() {
//
    //    outputVoltage.setValue(0);
//
    //    Direction facing = getBlockState().getValue(FACING);
    //    if(level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be){
    //        if(be.hasElectricitySlot(facing.getOpposite())){
    //            be.getOrCreateElectricNetwork().updateNetworkVoltage();
    //            be.needsVoltageUpdate();
//
//
    //        }
    //    }
//
//
    //    super.destroy();
//
//
    //}

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
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.RESISTOR.get());
    }
    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }
}
