package com.drmangotea.tfmg.base.pressure;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

/**
 * A pressure value in kPa, stored as an int.
 * <ul>
 *     <li>1 kPa = 1 (base unit)</li>
 *     <li>1 MPa = 1,000 kPa</li>
 *     <li>1 GPa = 1,000,000 kPa</li>
 * </ul>
 * <p>
 * Pressure is mutable, so using {@link #copy()} is recommended when sharing
 */
public class Pressure {
    public static final Pressure EMPTY = new Pressure(0);

    private int kpa;

    public Pressure(int kpa) {
        this.kpa = kpa;
    }

    public static Pressure of(int kpa) {
        return new Pressure(kpa);
    }

    public static Pressure from(CompoundTag tag) {
        return new Pressure(tag.getInt("Pressure"));
    }

    public static final Codec<Pressure> CODEC = Codec.INT.xmap(
            Pressure::new,
            Pressure::getValue
    );

    public static final StreamCodec<ByteBuf, Pressure> STREAM_CODEC = ByteBufCodecs.INT.map(
            Pressure::new,
            Pressure::getValue
    );

    public Pressure copy() {
        return new Pressure(this.kpa);
    }

    public int getValue() {
        return this.kpa;
    }

    public void setPressure(int kpa) {
        this.kpa = kpa;
    }

    public boolean isEmpty() {
        return this.kpa == 0;
    }

    public void grow(int kpa) {
        this.setPressure(this.kpa + kpa);
    }

    public void shrink(int kpa) {
        this.grow(-kpa);
    }

    public Pressure copyWithAmount(int kpa) {
        if (this.kpa == 0) {
            return EMPTY;
        }
        Pressure pressure = this.copy();
        pressure.setPressure(kpa);
        return pressure;
    }

    public String getFormatted() {
        return getFormatted(this.kpa);
    }

    public void save(CompoundTag tag) {
        tag.putInt("Pressure", this.kpa);
    }

    public static @NotNull String getFormatted(int kpa) {
        if (Math.abs(kpa) >= 1_000_000) {
            return String.format("%.2f GPa", kpa / 1_000_000.0);
        } else if (Math.abs(kpa) >= 1_000) {
            return String.format("%.2f MPa", kpa / 1_000.0);
        } else {
            return kpa + " kPa";
        }
    }
}
