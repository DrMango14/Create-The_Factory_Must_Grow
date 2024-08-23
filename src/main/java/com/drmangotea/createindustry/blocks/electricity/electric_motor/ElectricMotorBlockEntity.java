package com.drmangotea.createindustry.blocks.electricity.electric_motor;

import com.drmangotea.createindustry.base.util.TFMGUtils;
import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import com.drmangotea.createindustry.blocks.electricity.base.TFMGForgeEnergyStorage;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class ElectricMotorBlockEntity extends GeneratingKineticBlockEntity implements IElectricBlock, IHaveGoggleInformation {

    public static final int DEFAULT_SPEED = 16;
    public static final int MAX_SPEED = 256;


    public int voltage=0;

    private int voltageLastTick = voltage;
    public int current =0;

    private int energyUsed=0;

    private boolean gotFElastTick = false;

    public int distanceFromSource = Integer.MAX_VALUE;

    protected ScrollValueBehaviour generatedSpeed;

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public final TFMGForgeEnergyStorage energy = createEnergyStorage();
    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
        behaviours.add(generatedSpeed);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        if (!TFMGBlocks.ELECTRIC_MOTOR.has(getBlockState()))
            return 0;

        if(energy.getEnergyStored()<energy.getMaxEnergyStored()*0.25)
            return 0;


        energyUsed = energy.extractEnergy((int) Math.abs(speed*1.7),false);


        //if(energyUsed<(int) Math.abs(speed*2))
        //    return 0;

        if(energy.getEnergyStored() == 0)
            return 0;

        //if(energyUsed<(int) Math.abs(speed*2))
        //    return 0;





        return convertToDirection(Math.min(generatedSpeed.getValue(),voltage*2), getBlockState().getValue(FACING));
    }

    public void tick(){
        super.tick();




        manageVoltage();
        sendCharge(level,getBlockPos());





      //  if(speed>0){



        //}




      if(energy.getEnergyStored()>0) {
          updateGeneratedRotation();
      }else sendData();
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








    public TFMGForgeEnergyStorage getEnergy() {
        return energy;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energy);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {


        if (cap == ForgeCapabilities.ENERGY&&side == null) {
            return lazyEnergyHandler.cast();
        } else

        if (cap == ForgeCapabilities.ENERGY&&hasElectricitySlot(side)) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }


    public void manageVoltage(){



        if(voltageLastTick!=voltage)
            updateGeneratedRotation();

        if(voltageGeneration()>0) {
            voltage = voltageGeneration();
            distanceFromSource = 0;
        }
        if(distanceFromSource == Integer.MAX_VALUE)
            voltage = 0;


        current = energy.getEnergyStored()/(voltage+1);



        if(voltage>maxVoltage()){
            explode();
            voltage = 0;
        }

        voltageLastTick = voltage;

    }





    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {



         super.addToGoggleTooltip(tooltip, isPlayerSneaking);


        //Lang.translate("goggles.motor.usage",      energy.extractEnergy((int) Math.abs(speed*1.7),false),true)
        //        .style(ChatFormatting.AQUA)
        //        .forGoggles(tooltip, 1);
//

        return true;
    }





    @Override
    public float internalResistance() {
        return 0;
    }

    @Override
    public int getVoltage() {
        return voltage;
    }

    @Override
    public boolean gotFElastTick(int value) {



        gotFElastTick = value == 3 ? gotFElastTick : value == 1;

        return gotFElastTick;
    }

    @Override
    public int getCurrent() {
        return current;
    }





    @Override
    public void addVoltage(float amount) {

        if(voltageGeneration()>0)
            return;

        voltage = (int) amount;

    }




    @Override
    public TFMGForgeEnergyStorage getForgeEnergy() {
        return energy;
    }








    @Override
    public float maxVoltage() {
        return 1000;
    }

    @Override
    public void explode() {
        TFMGUtils.createFireExplosion(level,null, getBlockPos(),10,1.5f);
    }

    @Override
    public int FECapacity() {
        return 5000;
    }

    @Override
    public int getDistanceFromSource() {
        return distanceFromSource;
    }



    @Override
    public void setDistanceFromSource(int value) {
        distanceFromSource = value;
        sendStuff();
    }

    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        writeElectrity(compound);

        ;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        voltage = compound.getInt("Voltage");
        current = compound.getInt("Current");

        distanceFromSource = compound.getInt("DistanceFromSource");



        energy.setEnergy(compound.getInt("ForgeEnergy"));

    }



    @Override
    public int feGeneration() {
        return 0;
    }

    @Override
    public int voltageGeneration() {
        return 0;
    }

    @Override
    public int transferSpeed() {
        return 1000;
    }
}
