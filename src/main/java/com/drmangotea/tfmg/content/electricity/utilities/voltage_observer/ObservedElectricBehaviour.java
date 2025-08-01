package com.drmangotea.tfmg.content.electricity.utilities.voltage_observer;

import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class ObservedElectricBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<ObservedElectricBehaviour> TYPE = new BehaviourType<>();

    BlockPos observedPos;

    public ObservedElectricBehaviour(SmartBlockEntity be) {
        super(be);
    }

    public BlockPos getObservedPos() {
        return observedPos;
    }

    public void setObservedPos(BlockPos observedPos) {
        this.observedPos = observedPos;
    }

    @Override
    public void tick() {
        super.tick();
        if (getObservedPos() != null && !isObservingElectric()) setObservedPos(null);
    }

    public boolean isObservingElectric() {
        if (getObservedPos() == null) return false;
        BlockEntity be = getWorld().getBlockEntity(getObservedPos());
        return be instanceof IElectric;
    }

    public IElectric getObservedElectric() {
        if (!isObservingElectric()) return null;
        BlockEntity be = getWorld().getBlockEntity(getObservedPos());
        if (be instanceof IElectric electric) {
            return electric;
        } else {
            throw new IllegalStateException("Observed position does not contain an IElectric block entity: " + getObservedPos());
        }
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(nbt, registries, clientPacket);
        if (observedPos != null) {
            nbt.put("ObservedPos", NbtUtils.writeBlockPos(observedPos));
        }
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(nbt, registries, clientPacket);
        Optional<BlockPos> pos = NbtUtils.readBlockPos(nbt, "ObservedPos");
        setObservedPos(pos.orElse(null));
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
}
