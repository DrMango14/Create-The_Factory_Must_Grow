package com.drmangotea.tfmg.content.electricity.network.large_switch;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock.IS_MAIN_PART;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class LargeSwitchBlockEntity extends KineticElectricBlockEntity {
    public boolean updateInFront = false;

    public boolean closed = false;

    public LerpedFloat visualAngle = LerpedFloat.angular();
    public float angle = 900;

    final boolean isMainPart;

    public LargeSwitchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        isMainPart = state.getValue(IS_MAIN_PART);
        visualAngle.setValue(90);
    }


    public int voltageGeneration() {

        if (isMainPart)
            return 0;

        int voltageGeneration = 0;


        if (getLevelAccessor().getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite())) instanceof LargeSwitchBlockEntity be)
            if (be.getData().getId() != getData().getId())
                if (be.getData().getVoltage() != 0)
                    if (be.closed) {
                        voltageGeneration = Math.max(voltageGeneration, be.data.getVoltage());
                        getData().getsOutsidePower = true;
                    }

        if (voltageGeneration == 0)
            getData().getsOutsidePower = false;

        return voltageGeneration;
    }

    @Override
    public int getMaxCurrent() {
        return 1000;
    }

    @Override
    public int getMaxVoltage() {
        return 100000;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        angle = compound.getFloat("Angle");
        closed = compound.getBoolean("Closed");
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("Angle",angle);
        compound.putBoolean("Closed",closed);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(data.notEnoughPower&&!getBlockState().getValue(IS_MAIN_PART))
            if(level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite()))instanceof LargeSwitchBlockEntity be)
                be.onPlaced();
    }

    public IElectric getControlledBlock() {
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if(level.getBlockEntity(getBlockPos().relative(facing))instanceof LargeSwitchBlockEntity be){
            return be;
        }
        return null;
    }
    @Override
    public float resistance() {
        if (!isMainPart)
            return 0;
        if(!closed)
            return 0;

        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            int count = getBlocksConnectedToNetworkCount(getControlledBlock().getData().getId());

            if(count!=0)
                return Math.max(be.getNetworkResistance()*count, 0);
        }
        return 0;
    }

    @Override
    public void onNetworkChanged(int oldVoltage, float oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);

        if (oldVoltage != getData().getVoltage() || oldPower != getPowerUsage()) {
            updateInFront = true;
        }
        sendStuff();
        setChanged();
    }

    public float powerGeneration() {

        if (isMainPart)
            return 0;

        if(level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite())) instanceof LargeSwitchBlockEntity be&&be.data.notEnoughPower)
            return 0;

        int powerGeneration = 0;


        if (getLevelAccessor().getBlockEntity(getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite())) instanceof LargeSwitchBlockEntity be)
            if (be.getData().getId() != getData().getId())
                if (be.getData().getVoltage() != 0)
                    if (be.closed) {
                        powerGeneration = Math.max(powerGeneration, 100000);
                        getData().getsOutsidePower = true;
                    }

        if (powerGeneration == 0)
            getData().getsOutsidePower = false;

        return powerGeneration;
    }

    public void updateInFront() {

        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition), new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            be.updateNextTick();

        }
        sendStuff();
        setChanged();
    }


    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            visualAngle.chase(angle / 10d, 1d, LerpedFloat.Chaser.EXP);
            visualAngle.tickChaser();
        }
        if(updateInFront) {
            updateInFront();
            updateInFront = false;
        }
        if (!isMainPart)
            return;
        if (angle < 0)
            angle = 0;
        if (angle > 900)
            angle = 900;
        if (getSpeed() == 0)
            return;

        if (getSpeed() < 0 && angle != 900) {
            angle += getArmSpeed();
        }
        if (getSpeed() > 0 && angle != 0) {
            angle -= getArmSpeed();
        }

        boolean oldValue = closed;

        if (data.voltage < 1000) {
            closed = angle == 0;
        } else {
            closed = angle < data.voltage / 700f;
        }

        if (oldValue != closed)
            updateInFront();




    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);



        return true;
    }

    public float getArmSpeed() {
        return Math.abs(getSpeed()) * 0.3f;
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {

        return (direction == getBlockState().getValue(HORIZONTAL_FACING).getOpposite() && getBlockState().getValue(IS_MAIN_PART)) || (direction == getBlockState().getValue(HORIZONTAL_FACING) && !getBlockState().getValue(IS_MAIN_PART));

    }
}
