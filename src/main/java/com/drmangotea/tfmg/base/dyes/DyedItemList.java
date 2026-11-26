package com.drmangotea.tfmg.base.dyes;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class DyedItemList<T extends Item> implements Iterable<ItemEntry<T>> {
	private final ItemEntry<T>[] values;

	@SuppressWarnings("unchecked")
    public DyedItemList(Function<DyeColor, ItemEntry<T>> filler) {
		values = Arrays.stream(DyeColor.values()).map(filler).toArray(ItemEntry[]::new);
    }

    public ItemEntry<T> get(DyeColor color) {
        return values[color.ordinal()];
    }

    public boolean contains(Item item) {
        for (ItemEntry<?> entry : values) {
            if (entry.is(item)) {
                return true;
            }
        }
        return false;
    }

    public ItemEntry<T>[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override @NothingNullByDefault
    public Iterator<ItemEntry<T>> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public ItemEntry<T> next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return values[index++];
            }
        };
    }
}
