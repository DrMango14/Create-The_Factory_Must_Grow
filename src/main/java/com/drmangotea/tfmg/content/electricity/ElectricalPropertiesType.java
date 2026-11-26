package com.drmangotea.tfmg.content.electricity;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public record ElectricalPropertiesType<T extends ElectricalProperties>(ResourceLocation id, PropertiesFactory<T> factory) {
	
	public T create(BlockPos pos, Direction direction, List<Direction> d) {
		return factory.make(pos, direction, d);
	}
	
	public interface PropertiesFactory<T extends ElectricalProperties> {
		T make (BlockPos pos, Direction direction, List<Direction> d);
	}
	
	public static class Entry<P extends ElectricalPropertiesType<?>> extends RegistryEntry<ElectricalPropertiesType<?>, P> {
		
		public Entry(AbstractRegistrate<?> owner, DeferredHolder<ElectricalPropertiesType<?>, P> delegate) {
			super(owner, delegate);
		}
		
		public static <P extends ElectricalPropertiesType<?>> Entry<P> cast(RegistryEntry<ElectricalPropertiesType<?>, P> entry) {
			return RegistryEntry.cast(Entry.class, entry);
		}
	}
}
