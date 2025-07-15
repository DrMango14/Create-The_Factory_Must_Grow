package com.drmangotea.tfmg.content.electricity.utilities.diode;


import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.UpdateInFrontPacket;
import com.drmangotea.tfmg.content.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class ElectricDiodeBlockEntity extends VoltageAlteringBlockEntity {

    public boolean updateInFront = false;


    public ElectricDiodeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getOutputVoltage() {
        return getData().getVoltage();
    }

    @Override
    public int getOutputPower() {
        return getPowerUsage();
    }

    @Override
    public void tick() {
        super.tick();
        if (updateInFront) {
            updateInFront();
            updateInFront = false;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

    }

    @Override
    public int getPowerUsage() {
        getOrCreateElectricNetwork().checkForLoops(getBlockPos());
        Direction facing = getDirection();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite()))

                    return Math.max(be.getNetworkPowerUsage(this), 0);

        }

        return 0;

    }

    @Override
    public IElectric getControlledBlock() {
        Direction facing = getBlockState().hasProperty(DirectionalBlock.FACING) ? getBlockState().getValue(DirectionalBlock.FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            return be;
        }
        return null;
    }

    @Override
    public float resistance() {
        Direction facing = getDirection();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite()))
                return Math.max(be.getNetworkResistance(), 0);
        }
        return 0;
    }

    public Direction getDirection(){
        if(!getBlockState().hasProperty(FACING)){
            return getBlockState().getValue(TFMGHorizontalDirectionalBlock.FACING).getCounterClockWise();
        }

        return getBlockState().getValue(FACING);
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return getDirection().getOpposite() == direction;
    }

    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);

        if (oldVoltage != getData().getVoltage() || oldPower != getPowerUsage()) {
            updateInFront = true;
        }
        sendStuff();
        setChanged();
    }



    @Override
    public void remove() {

        super.remove();
        updateInFront();
    }

    @Override
    public void onPlaced() {

        super.onPlaced();
        updateInFront = true;
    }

    public void updateInFrontNextTick(){
        updateInFront = true;
    }

    public void updateInFront() {

        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition),new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().hasProperty(FACING) ? getBlockState().getValue(FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {
                be.updateNextTick();

            }
        }
        sendStuff();
        setChanged();
    }
    public void updateBehind() {

        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition),new UpdateInFrontPacket(BlockPos.of(getPos())));
        Direction facing = getBlockState().hasProperty(FACING) ? getBlockState().getValue(FACING) : getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise();
        facing = facing.getOpposite();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
            if (be.hasElectricitySlot(facing.getOpposite())) {
                be.updateNextTick();

            }
        }
        sendStuff();
        setChanged();
    }
}
