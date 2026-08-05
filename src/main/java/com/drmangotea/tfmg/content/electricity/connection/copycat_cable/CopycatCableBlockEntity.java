package com.drmangotea.tfmg.content.electricity.connection.copycat_cable;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockValues;
import com.drmangotea.tfmg.content.electricity.base.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.VoltageAlteringBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CopycatCableBlockEntity extends CopycatBlockEntity implements IElectric, IHaveHoveringInformation {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());

    public CopycatCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        data.connectNextTick = true;
    }

    @Override
    public boolean hasCustomMaterial() {
        return !AllBlocks.COPYCAT_BASE.has(getMaterial());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    // ===== IElectric implementation =====

    @Override
    public ElectricBlockValues getData() {
        return data;
    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public float resistance() {
        return 0;
    }

    @Override
    public boolean isCable() {
        return true;
    }

    @Override
    public int getMaxVoltage() {
        return 5000;
    }

    @Override
    public int voltageGeneration() {
        int voltageGeneration = 0;
        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {
                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be)
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                voltageGeneration = Math.max(voltageGeneration, be.getOutputVoltage());
                                data.getsOutsidePower = true;
                            }
            }
        }
        if (voltageGeneration == 0) data.getsOutsidePower = false;
        return voltageGeneration;
    }

    @Override
    public int powerGeneration() {
        int powerGeneration = 0;
        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {
                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be && be.canWork()) {
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                powerGeneration = Math.max(powerGeneration, be.getMaxPowerOutput());
                                if (powerGeneration > be.getNetworkPowerGeneration()) {
                                    powerGeneration = 0;
                                    be.data.updatePowerNextTick = true;
                                }
                            }
                }
            }
        }
        return powerGeneration;
    }

    @Override
    public void sendStuff() {
        sendData();
    }

    @Override
    public void setNetwork(long network) {
        data.electricalNetworkId = network;
        if (network != getPos())
            ElectricNetworkManager.networks.get(getLevel()).remove(getPos());
    }

    @Override
    public int getNetworkResistance() {
        return data.networkResistance;
    }

    // ===== Lifecycle — delegate to IElectric defaults =====

    @Override
    public void lazyTick() {
        super.lazyTick();
        lazyTickElectricity();
    }

    @Override
    public void tick() {
        super.tick();
        tickElectricity();
    }

    @Override
    public void remove() {
        super.remove();
        onRemoved();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (getMaterial() != null && !clientPacket) {
            BlockState blockState = getBlockState();
            if (blockState == null) return;
            if (!(blockState.getBlock() instanceof CopycatBlock cb)) return;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(level, worldPosition, ItemStack.EMPTY, null);
            if (acceptedBlockState != null && getMaterial().is(acceptedBlockState.getBlock())) return;
            setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        }
        if (!clientPacket)
            data.connectNextTick = true;
    }
}
