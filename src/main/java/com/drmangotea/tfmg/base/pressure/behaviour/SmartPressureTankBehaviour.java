package com.drmangotea.tfmg.base.pressure.behaviour;

import com.drmangotea.tfmg.base.capabilities.pressure.IPressureHandler;
import com.drmangotea.tfmg.base.pressure.Pressure;
import com.drmangotea.tfmg.base.pressure.tank.CombinedPressureTankWrapper;
import com.drmangotea.tfmg.base.pressure.tank.SmartPressureTank;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.function.Consumer;

public class SmartPressureTankBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<SmartPressureTankBehaviour> TYPE = new BehaviourType<>(),
            INPUT = new BehaviourType<>("PressureInput"),
            OUTPUT = new BehaviourType<>("PressureOutput");

    private static final int SYNC_RATE = 8;

    protected int syncCooldown;
    protected boolean queuedSync;
    protected SmartPressureTankBehaviour.TankSegment[] tanks;
    protected IPressureHandler capability;
    protected boolean extractionAllowed;
    protected boolean insertionAllowed;
    protected Runnable pressureUpdateCallback;
    private BehaviourType<SmartPressureTankBehaviour> behaviourType;

    public SmartPressureTankBehaviour(SmartBlockEntity be) {
        super(be);
    }

    public SmartPressureTankBehaviour(BehaviourType<SmartPressureTankBehaviour> type, SmartBlockEntity be, int tanks, int tankCapacity) {
        super(be);
        insertionAllowed = true;
        extractionAllowed = true;
        behaviourType = type;
        this.tanks = new TankSegment[tanks];
        IPressureHandler[] handlers = new IPressureHandler[tanks];
        for (int i = 0; i < tanks; i++) {
            TankSegment tankSegment = new TankSegment(tankCapacity);
            this.tanks[i] = tankSegment;
            handlers[i] = tankSegment.tank;
        }
        capability = new InternalPressureHandler(handlers);
        pressureUpdateCallback = () -> {};
    }

    public SmartPressureTankBehaviour whenPressureUpdates(Runnable pressureUpdateCallback) {
        this.pressureUpdateCallback = pressureUpdateCallback;
        return this;
    }

    public SmartPressureTankBehaviour allowInsertion() {
        insertionAllowed = true;
        return this;
    }

    public SmartPressureTankBehaviour allowExtraction() {
        extractionAllowed = true;
        return this;
    }

    public SmartPressureTankBehaviour forbidInsertion() {
        insertionAllowed = false;
        return this;
    }

    public SmartPressureTankBehaviour forbidExtraction() {
        extractionAllowed = false;
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync) updatePressures();
        }
    }

    @Override
    public BehaviourType<?> getType() {
        return behaviourType;
    }

    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        updatePressures();
    }

    public void sendDataLazily() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        updatePressures();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    protected void updatePressures() {
        pressureUpdateCallback.run();
        blockEntity.sendData();
        blockEntity.setChanged();
    }

    @Override
    public void initialize() {
        super.initialize();
        if (getWorld().isClientSide) return;
        forEach(TankSegment::onPressureChanged);
    }

    @Override
    public void unload() {
        super.unload();
        if (blockEntity.getLevel() == null) return;
        blockEntity.getLevel().invalidateCapabilities(getPos());
    }

    public SmartPressureTank getPrimaryHandler() {
        return getPrimaryTank().tank;
    }

    public TankSegment getPrimaryTank() {
        return tanks[0];
    }

    public TankSegment[] getTanks() {
        return tanks;
    }

    public boolean isEmpty() {
        for (SmartPressureTankBehaviour.TankSegment tankSegment : tanks)
            if (!tankSegment.tank.isEmpty())
                return false;
        return true;
    }

    public void forEach(Consumer<SmartPressureTankBehaviour.TankSegment> action) {
        for (SmartPressureTankBehaviour.TankSegment tankSegment : tanks)
            action.accept(tankSegment);
    }

    public IPressureHandler getCapability() {
        return this.capability;
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(nbt, registries, clientPacket);
        ListTag tanksData = new ListTag();
        forEach(ts -> tanksData.add(ts.writeNBT()));
        nbt.put(getType().getName() + "Tanks", tanksData);
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(nbt, registries, clientPacket);
        MutableInt index = new MutableInt(0);
        NBTHelper.iterateCompoundList(nbt.getList(getType().getName() + "Tanks", Tag.TAG_COMPOUND), c -> {
            if (index.intValue() >= tanks.length)
                return;
            tanks[index.intValue()].readNBT(c);
            index.increment();
        });
    }

    public class TankSegment {
        protected SmartPressureTank tank;

        public TankSegment(int capacity) {
            tank = new SmartPressureTank(capacity, f -> onPressureChanged());
        }

        public void onPressureChanged() {
            if (!getWorld().isClientSide())
                sendDataLazily();
        }

        public CompoundTag writeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.put("TankContent", this.tank.writeToNBT(new CompoundTag()));
            return compound;
        }

        public void readNBT(CompoundTag compound) {
            this.tank.readFromNBT(compound.getCompound("TankContent"));
        }

        public SmartPressureTank getTank() {
            return tank;
        }
    }

    public class InternalPressureHandler extends CombinedPressureTankWrapper {
        public InternalPressureHandler(IPressureHandler[] handlers) {
            super(handlers);
        }

        @Override
        public int fill(Pressure pressure, boolean simulate) {
            return insertionAllowed ? super.fill(pressure, simulate) : 0;
        }

        public int forceFill(Pressure pressure, boolean simulate) {
            return super.fill(pressure, simulate);
        }

        @Override
        public Pressure drain(int maxDrain, boolean simulate) {
            return extractionAllowed ? super.drain(maxDrain, simulate) : Pressure.EMPTY;
        }

        @Override
        public Pressure drain(Pressure pressure, boolean simulate) {
            return extractionAllowed ? super.drain(pressure, simulate) : Pressure.EMPTY;
        }
    }
}
