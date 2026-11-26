package com.drmangotea.tfmg.content.engines.types;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.registries.DeferredHolder;

public class EngineTypeEntry<T extends EngineType> extends RegistryEntry<EngineType, T> {
    public EngineTypeEntry(AbstractRegistrate<?> owner, DeferredHolder<EngineType, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends EngineType> EngineTypeEntry<T> cast(RegistryEntry<EngineType, T> entry) {
        return RegistryEntry.cast(EngineTypeEntry.class, entry);
    }
}
