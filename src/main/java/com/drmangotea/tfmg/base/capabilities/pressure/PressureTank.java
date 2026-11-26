package com.drmangotea.tfmg.base.capabilities.pressure;

import com.drmangotea.tfmg.base.pressure.Pressure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PressureTank implements IPressureHandler, IPressureTank {
    protected Pressure pressure;
    protected int capacity;

    public static final Codec<PressureTank> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Pressure.CODEC.fieldOf("pressure").forGetter(PressureTank::getPressure),
            Codec.INT.fieldOf("capacity").forGetter(PressureTank::getCapacity)
    ).apply(instance, PressureTank::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PressureTank> STREAM_CODEC = StreamCodec.composite(
            Pressure.STREAM_CODEC, PressureTank::getPressure,
            ByteBufCodecs.INT, PressureTank::getCapacity,
            PressureTank::new
    );

    private PressureTank(Pressure pressure, int capacity) {
        this.capacity = capacity;
        this.pressure = pressure;
    }

    public PressureTank(int capacity) {
        this.pressure = Pressure.EMPTY;
        this.capacity = capacity;
    }

    public static PressureTank create(int capacity) {
        return new PressureTank(capacity);
    }

    public PressureTank setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public PressureTank setPressureInSlot(int index, Pressure pressure) {
        this.pressure = pressure;
        return this;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public Pressure getPressureInTank(int index) {
        return this.pressure;
    }

    @Override
    public int getTankCapacity(int index) {
        return this.capacity;
    }

    @Override
    public Pressure getPressure() {
        return this.pressure;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    public PressureTank readFromNBT(CompoundTag nbt) {
        this.pressure = Pressure.of(nbt.getInt("Pressure"));
        return this;
    }

    public boolean isEmpty() {
        return this.pressure.isEmpty();
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        if (!this.pressure.isEmpty()) {
            this.pressure.save(nbt);
            nbt.putInt("Capacity", this.capacity);
        }

        return nbt;
    }

    @Override
    public int fill(Pressure pressure, boolean simulate) {
        if (!pressure.isEmpty()) {
            if (simulate) {
                return Math.min(this.capacity, pressure.getValue());
            } else if (this.pressure.isEmpty()) {
                this.pressure = pressure.copyWithAmount(Math.min(this.capacity, pressure.getValue()));
                this.onContentsChanged();
                return this.pressure.getValue();
            }  else {
                int filled = this.capacity - this.pressure.getValue();
                if (pressure.getValue() < filled) {
                    this.pressure.grow(pressure.getValue());
                    filled = pressure.getValue();
                } else {
                    this.pressure.setPressure(this.capacity);
                }
                if (filled > 0) {
                    this.onContentsChanged();
                }
                return filled;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Pressure drain(Pressure resource, boolean simulate) {
        return !resource.isEmpty() ? this.drain(resource.getValue(), simulate) : Pressure.EMPTY;
    }

    @Override
    public Pressure drain(int maxDrain, boolean simulate) {
        int drained = Math.min(this.pressure.getValue(), maxDrain);
        Pressure stack = this.pressure.copyWithAmount(drained);
        if (!simulate && drained > 0) {
            this.pressure.shrink(drained);
            this.onContentsChanged();
        }
        return stack;
    }

    public Pressure copy() {
        return this.pressure.copy();
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    protected void onContentsChanged() {
    }
}
