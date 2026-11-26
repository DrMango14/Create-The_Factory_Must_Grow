package com.drmangotea.tfmg.base.dyes;

import com.drmangotea.tfmg.base.MaterialSet;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class DyedMaterialSetList implements Iterable<MaterialSet> {
    private final MaterialSet[] values;

    public DyedMaterialSetList(Function<DyeColor, MaterialSet> filler) {
		values = Arrays.stream(DyeColor.values()).map(filler).toArray(MaterialSet[]::new);
    }

    public MaterialSet get(DyeColor color) {
        return values[color.ordinal()];
    }

    public boolean contains(Block block) {
        for (MaterialSet set : values) {
            if (set.contains(block)) {
                return true;
            }
        }
        return false;
    }

    public MaterialSet[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override @NothingNullByDefault
    public Iterator<MaterialSet> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public MaterialSet next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return values[index++];
            }
        };
    }
}
