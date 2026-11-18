package com.drmangotea.tfmg.content.electricity.content.power_transmission.cables;

import com.drmangotea.tfmg.content.electricity.IElectric;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractCableBlockEntity extends SmartBlockEntity implements IElectric {

    public CableProperties p;

    public boolean connectNextTick = false;

    public AbstractCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (connectNextTick) {
            connectNextTick = IElectric.connectToNeighbors(level, this);
        }
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    public CableProperties getProperties() {
        return p;
    }

    @Override
    public void onPlace() {
        IElectric.super.onPlace();
        connectNextTick = IElectric.connectToNeighbors(level, this);
        updateNetwork(getBlockPos());
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }
}
