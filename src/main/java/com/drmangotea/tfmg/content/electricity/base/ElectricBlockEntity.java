package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import java.util.ArrayList;
import java.util.List;

public class ElectricBlockEntity extends SmartBlockEntity implements IElectric, IHaveHoveringInformation {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());


    public ElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        data.connectNextTick = true;
        if (!canBeInGroups()) {
            data.group = new ElectricalGroup(-1);
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        lazyTickElectricity();
    }

    @Override
    public ElectricBlockValues getData() {
        return data;
    }


    @Override
    public void sendStuff() {
        sendData();
    }



    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public void remove() {
        super.remove();
        onRemoved();

    }

    @Override
    public void tick() {
        super.tick();
        tickElectricity();

    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        writeElectricity(compound,clientPacket);

    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        readElectricity(compound,clientPacket);

    }
}
