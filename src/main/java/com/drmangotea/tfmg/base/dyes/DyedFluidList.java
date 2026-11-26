package com.drmangotea.tfmg.base.dyes;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class DyedFluidList<T extends BaseFlowingFluid> implements Iterable<FluidEntry<T>> {
    private final FluidEntry<T>[] values;
	
	@SuppressWarnings("unchecked")
    public DyedFluidList(Function<DyeColor, FluidEntry<? extends T>> filler) {
		values = Arrays.stream(DyeColor.values()).map(filler).toArray(FluidEntry[]::new);
    }

    public FluidEntry<T> get(DyeColor color) {
        return values[color.ordinal()];
    }

    public Fluid getSource(DyeColor color) {
        return get(color).getSource();
    }

    public boolean contains(Fluid fluid) {
        for (FluidEntry<?> entry : values) {
            if (entry.is(fluid)) {
                return true;
            }
        }
        return false;
    }

    public FluidEntry<T>[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override @NothingNullByDefault
    public Iterator<FluidEntry<T>> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public FluidEntry<T> next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return values[index++];
            }
        };
    }
}
